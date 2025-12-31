import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Save, X } from 'lucide-react';
import { projectApi } from '../services/api';
import Toast from '../components/Toast';

const ProjectForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditMode = !!id;

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    status: 'ACTIVE',
    startDate: new Date().toISOString().split('T')[0],
    endDate: '',
    progress: 0,
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState(null);

  const statusOptions = [
    { value: 'ACTIVE', label: '진행중', color: 'from-green-500 to-emerald-500' },
    { value: 'COMPLETED', label: '완료', color: 'from-blue-500 to-cyan-500' },
    { value: 'ON_HOLD', label: '보류', color: 'from-yellow-500 to-orange-500' },
    { value: 'ARCHIVED', label: '보관', color: 'from-gray-600 to-slate-600' },
  ];

  useEffect(() => {
    if (isEditMode) {
      fetchProject();
    }
  }, [id]);

  const fetchProject = async () => {
    try {
      setLoading(true);
      const response = await projectApi.getById(id);
      const project = response.data;
      setFormData({
        name: project.name || '',
        description: project.description || '',
        status: project.status || 'ACTIVE',
        startDate: project.startDate ? project.startDate.split('T')[0] : '',
        endDate: project.endDate ? project.endDate.split('T')[0] : '',
        progress: project.progress || 0,
      });
    } catch (err) {
      console.error('Error fetching project:', err);
      showToast('프로젝트를 불러오는데 실패했습니다.', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Clear error for this field
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: '',
      }));
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.name.trim()) {
      newErrors.name = '프로젝트명을 입력하세요.';
    } else if (formData.name.length > 100) {
      newErrors.name = '프로젝트명은 100자 이내로 입력하세요.';
    }

    if (formData.description && formData.description.length > 500) {
      newErrors.description = '설명은 500자 이내로 입력하세요.';
    }

    if (!formData.startDate) {
      newErrors.startDate = '시작일을 입력하세요.';
    }

    if (formData.endDate && formData.startDate && formData.endDate < formData.startDate) {
      newErrors.endDate = '종료일은 시작일 이후여야 합니다.';
    }

    if (formData.progress < 0 || formData.progress > 100) {
      newErrors.progress = '진행률은 0~100 사이여야 합니다.';
    }

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      setLoading(true);

      const projectData = {
        ...formData,
        startDate: formData.startDate ? `${formData.startDate}T00:00:00` : null,
        endDate: formData.endDate ? `${formData.endDate}T00:00:00` : null,
      };

      if (isEditMode) {
        await projectApi.update(id, projectData);
        showToast('프로젝트가 수정되었습니다.', 'success');
      } else {
        await projectApi.create(projectData);
        showToast('프로젝트가 생성되었습니다.', 'success');
      }

      setTimeout(() => {
        navigate('/projects');
      }, 1000);
    } catch (err) {
      console.error('Error saving project:', err);
      showToast(
        isEditMode ? '프로젝트 수정에 실패했습니다.' : '프로젝트 생성에 실패했습니다.',
        'error'
      );
    } finally {
      setLoading(false);
    }
  };

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
  };

  const closeToast = () => {
    setToast(null);
  };

  return (
    <div className="space-y-6 animate-slide-up">
      {/* Toast */}
      {toast && <Toast message={toast.message} type={toast.type} onClose={closeToast} />}

      {/* Header */}
      <div className="flex items-center justify-between">
        <button
          onClick={() => navigate('/projects')}
          className="flex items-center space-x-2 text-white/70 hover:text-white transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
          <span>목록으로</span>
        </button>

        <h1 className="text-3xl font-display font-bold text-white">
          {isEditMode ? '프로젝트 수정' : '새 프로젝트'}
        </h1>
      </div>

      {/* Form */}
      <form onSubmit={handleSubmit} className="glass rounded-2xl p-8">
        <div className="space-y-6">
          {/* Project Name */}
          <div>
            <label className="block text-sm font-medium text-white mb-2">
              프로젝트명 <span className="text-red-400">*</span>
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="프로젝트 이름을 입력하세요"
              className={`w-full px-4 py-3 bg-white/5 border ${
                errors.name ? 'border-red-500' : 'border-white/10'
              } rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all`}
            />
            {errors.name && <p className="mt-1 text-sm text-red-400">{errors.name}</p>}
          </div>

          {/* Description */}
          <div>
            <label className="block text-sm font-medium text-white mb-2">설명</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="프로젝트에 대한 간단한 설명을 입력하세요"
              rows={4}
              className={`w-full px-4 py-3 bg-white/5 border ${
                errors.description ? 'border-red-500' : 'border-white/10'
              } rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all resize-none`}
            />
            {errors.description && <p className="mt-1 text-sm text-red-400">{errors.description}</p>}
          </div>

          {/* Status */}
          <div>
            <label className="block text-sm font-medium text-white mb-2">
              상태 <span className="text-red-400">*</span>
            </label>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
              {statusOptions.map((option) => (
                <button
                  key={option.value}
                  type="button"
                  onClick={() => handleChange({ target: { name: 'status', value: option.value } })}
                  className={`px-4 py-3 rounded-xl text-sm font-medium transition-all ${
                    formData.status === option.value
                      ? `bg-gradient-to-r ${option.color} text-white shadow-glow`
                      : 'bg-white/5 text-white/70 hover:bg-white/10'
                  }`}
                >
                  {option.label}
                </button>
              ))}
            </div>
          </div>

          {/* Dates */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Start Date */}
            <div>
              <label className="block text-sm font-medium text-white mb-2">
                시작일 <span className="text-red-400">*</span>
              </label>
              <input
                type="date"
                name="startDate"
                value={formData.startDate}
                onChange={handleChange}
                className={`w-full px-4 py-3 bg-white/5 border ${
                  errors.startDate ? 'border-red-500' : 'border-white/10'
                } rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all`}
              />
              {errors.startDate && <p className="mt-1 text-sm text-red-400">{errors.startDate}</p>}
            </div>

            {/* End Date */}
            <div>
              <label className="block text-sm font-medium text-white mb-2">종료일 (선택)</label>
              <input
                type="date"
                name="endDate"
                value={formData.endDate}
                onChange={handleChange}
                className={`w-full px-4 py-3 bg-white/5 border ${
                  errors.endDate ? 'border-red-500' : 'border-white/10'
                } rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all`}
              />
              {errors.endDate && <p className="mt-1 text-sm text-red-400">{errors.endDate}</p>}
            </div>
          </div>

          {/* Progress */}
          <div>
            <label className="block text-sm font-medium text-white mb-2">
              진행률: {formData.progress}%
            </label>
            <input
              type="range"
              name="progress"
              value={formData.progress}
              onChange={handleChange}
              min="0"
              max="100"
              step="5"
              className="w-full"
            />
            <div className="mt-2 h-2 bg-white/10 rounded-full overflow-hidden">
              <div
                className="h-full bg-gradient-to-r from-purple-500 to-blue-500 transition-all duration-300"
                style={{ width: `${formData.progress}%` }}
              ></div>
            </div>
            {errors.progress && <p className="mt-1 text-sm text-red-400">{errors.progress}</p>}
          </div>
        </div>

        {/* Actions */}
        <div className="flex items-center justify-end space-x-3 mt-8 pt-6 border-t border-white/10">
          <button
            type="button"
            onClick={() => navigate('/projects')}
            className="flex items-center space-x-2 px-6 py-3 bg-white/10 hover:bg-white/20 text-white rounded-xl transition-all"
          >
            <X className="w-4 h-4" />
            <span>취소</span>
          </button>
          <button
            type="submit"
            disabled={loading}
            className="flex items-center space-x-2 px-6 py-3 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <Save className="w-4 h-4" />
            <span>{loading ? '저장 중...' : isEditMode ? '수정' : '생성'}</span>
          </button>
        </div>
      </form>
    </div>
  );
};

export default ProjectForm;
