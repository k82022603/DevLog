import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Save,
  X,
  Calendar,
  Clock,
  Code,
  Tag,
  Smile,
  AlertCircle,
} from 'lucide-react';
import { devLogApi, projectApi, techTagApi } from '../services/api';
import Toast from '../components/Toast';

const LogForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEdit = !!id;

  const [loading, setLoading] = useState(false);
  const [projects, setProjects] = useState([]);
  const [availableTags, setAvailableTags] = useState([]);
  const [errors, setErrors] = useState({});
  const [toast, setToast] = useState(null);
  const [hasChanges, setHasChanges] = useState(false);

  // Moods (must match database CHECK constraint)
  const moods = [
    { emoji: '🔥', label: '최고', value: 'GREAT' },
    { emoji: '😊', label: '좋음', value: 'GOOD' },
    { emoji: '😐', label: '보통', value: 'NEUTRAL' },
    { emoji: '😓', label: '힘듦', value: 'BAD' },
    { emoji: '😤', label: '최악', value: 'TERRIBLE' },
  ];

  const [formData, setFormData] = useState({
    projectId: '',
    title: '',
    description: '',
    logDate: new Date().toISOString().split('T')[0],
    startTime: '',
    endTime: '',
    achievements: '',
    challenges: '',
    learnings: '',
    codeSnippets: '',
    techTagIds: [],
    mood: '',
  });

  const [newTagInput, setNewTagInput] = useState('');

  useEffect(() => {
    fetchProjects();
    fetchTags();
    if (isEdit) {
      fetchLog();
    } else {
      loadDraft();
    }
  }, [id]);

  // Auto-save draft every 30 seconds
  useEffect(() => {
    if (!isEdit && hasChanges) {
      const timer = setInterval(() => {
        saveDraft();
      }, 30000);

      return () => clearInterval(timer);
    }
  }, [formData, isEdit, hasChanges]);

  const fetchProjects = async () => {
    try {
      const response = await projectApi.getAll();
      setProjects(response.data || []);
    } catch (err) {
      console.error('Error fetching projects:', err);
    }
  };

  const fetchTags = async () => {
    try {
      const response = await techTagApi.getAll();
      setAvailableTags(response.data || []);
    } catch (err) {
      console.error('Error fetching tags:', err);
    }
  };

  const fetchLog = async () => {
    try {
      setLoading(true);
      const response = await devLogApi.getById(id);
      const log = response.data;

      setFormData({
        projectId: log.projectId || '',
        title: log.title || '',
        description: log.description || '',
        logDate: log.logDate ? log.logDate.split('T')[0] : '',
        startTime: log.startTime ? (log.startTime.includes('T') ? log.startTime.split('T')[1].substring(0, 5) : log.startTime.substring(0, 5)) : '',
        endTime: log.endTime ? (log.endTime.includes('T') ? log.endTime.split('T')[1].substring(0, 5) : log.endTime.substring(0, 5)) : '',
        achievements: log.achievements || '',
        challenges: log.challenges || '',
        learnings: log.learnings || '',
        codeSnippets: log.codeSnippets || '',
        techTagIds: log.techTags ? log.techTags.map((t) => t.id) : [],
        mood: log.mood || '',
      });
    } catch (err) {
      console.error('Error fetching log:', err);
      showToast('로그를 불러오는데 실패했습니다.', 'error');
    } finally {
      setLoading(false);
    }
  };

  const loadDraft = () => {
    const draft = localStorage.getItem('logDraft');
    if (draft) {
      try {
        const parsedDraft = JSON.parse(draft);
        setFormData(parsedDraft);
        showToast('임시 저장된 내용을 불러왔습니다.', 'success');
      } catch (err) {
        console.error('Error loading draft:', err);
      }
    }
  };

  const saveDraft = () => {
    localStorage.setItem('logDraft', JSON.stringify(formData));
    console.log('Draft saved');
  };

  const clearDraft = () => {
    localStorage.removeItem('logDraft');
  };

  const handleChange = (field, value) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value,
    }));
    setHasChanges(true);

    // Clear error for this field
    if (errors[field]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[field];
        return newErrors;
      });
    }
  };

  const handleTagToggle = (tagId) => {
    setFormData((prev) => ({
      ...prev,
      techTagIds: prev.techTagIds.includes(tagId)
        ? prev.techTagIds.filter((id) => id !== tagId)
        : [...prev.techTagIds, tagId],
    }));
    setHasChanges(true);
  };

  const handleAddNewTag = async () => {
    if (!newTagInput.trim()) return;

    try {
      // In a real app, you'd call an API to create the tag
      // For now, we'll just add it to the available tags
      const newTag = {
        id: Date.now(), // temporary ID
        name: newTagInput.trim(),
        category: 'TOOL',
        color: '#8B5CF6',
      };

      setAvailableTags((prev) => [...prev, newTag]);
      handleTagToggle(newTag.id);
      setNewTagInput('');
      showToast('새 태그가 추가되었습니다.', 'success');
    } catch (err) {
      console.error('Error adding tag:', err);
      showToast('태그 추가에 실패했습니다.', 'error');
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.projectId) {
      newErrors.projectId = '프로젝트를 선택해주세요.';
    }

    if (!formData.title.trim()) {
      newErrors.title = '제목을 입력해주세요.';
    }

    if (!formData.logDate) {
      newErrors.logDate = '날짜를 선택해주세요.';
    }

    if (formData.startTime && formData.endTime) {
      if (formData.endTime <= formData.startTime) {
        newErrors.endTime = '종료 시간은 시작 시간보다 늦어야 합니다.';
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) {
      showToast('필수 항목을 확인해주세요.', 'error');
      return;
    }

    try {
      setLoading(true);

      // Prepare data
      const submitData = {
        projectId: parseInt(formData.projectId),
        title: formData.title,
        description: formData.description || null,
        logDate: `${formData.logDate}T00:00:00`,
        startTime: formData.startTime
          ? `${formData.logDate}T${formData.startTime}:00`
          : null,
        endTime: formData.endTime
          ? `${formData.logDate}T${formData.endTime}:00`
          : null,
        achievements: formData.achievements || null,
        challenges: formData.challenges || null,
        learnings: formData.learnings || null,
        codeSnippets: formData.codeSnippets || null,
        techTagIds: formData.techTagIds,
        mood: formData.mood || null,
      };

      if (isEdit) {
        await devLogApi.update(id, submitData);
        showToast('로그가 수정되었습니다.', 'success');
      } else {
        await devLogApi.create(submitData);
        showToast('로그가 작성되었습니다.', 'success');
        clearDraft();
      }

      setTimeout(() => {
        navigate('/logs');
      }, 1000);
    } catch (err) {
      console.error('Error saving log:', err);
      showToast(
        isEdit ? '로그 수정에 실패했습니다.' : '로그 작성에 실패했습니다.',
        'error'
      );
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    if (hasChanges) {
      if (window.confirm('변경 사항이 저장되지 않습니다. 취소하시겠습니까?')) {
        navigate('/logs');
      }
    } else {
      navigate('/logs');
    }
  };

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
  };

  const closeToast = () => {
    setToast(null);
  };

  if (loading && isEdit) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="inline-flex p-4 rounded-full bg-white/5 mb-4 animate-pulse">
            <Code className="w-8 h-8 text-white/40" />
          </div>
          <p className="text-white/60">로그를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6 animate-slide-up">
      {/* Toast */}
      {toast && (
        <Toast message={toast.message} type={toast.type} onClose={closeToast} />
      )}

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-4xl font-display font-bold text-white mb-2">
            {isEdit ? '로그 수정' : '새 로그 작성'}
          </h2>
          <p className="text-white/60 text-lg">
            개발 활동을 기록하고 회고하세요
          </p>
        </div>
      </div>

      {/* Form */}
      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Basic Info Card */}
        <div className="glass rounded-2xl p-6">
          <h3 className="text-xl font-semibold text-white mb-4 flex items-center space-x-2">
            <Calendar className="w-5 h-5 text-purple-400" />
            <span>기본 정보</span>
          </h3>

          <div className="space-y-4">
            {/* Project and Date Row */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* Project */}
              <div>
                <label className="block text-sm font-medium text-white/80 mb-2">
                  프로젝트 <span className="text-red-400">*</span>
                </label>
                <select
                  value={formData.projectId || ""}
                  onChange={(e) => handleChange('projectId', e.target.value)}
                  className={`w-full px-4 py-3 border ${
                    errors.projectId ? 'border-red-500' : 'border-white/10'
                  } rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all`}
                  style={{
                    backgroundColor: 'rgb(97, 61, 132)',
                  }}
                >
                  <option value="" style={{ backgroundColor: 'rgb(97, 61, 132)', color: 'white' }}>프로젝트 선택</option>
                  {projects.map((project) => (
                    <option key={project.id} value={String(project.id)} style={{ backgroundColor: 'rgb(97, 61, 132)', color: 'white' }}>
                      {project.name}
                    </option>
                  ))}
                </select>
                {errors.projectId && (
                  <p className="mt-1 text-sm text-red-400 flex items-center space-x-1">
                    <AlertCircle className="w-3 h-3" />
                    <span>{errors.projectId}</span>
                  </p>
                )}
              </div>

              {/* Date */}
              <div>
                <label className="block text-sm font-medium text-white/80 mb-2">
                  날짜 <span className="text-red-400">*</span>
                </label>
                <input
                  type="date"
                  value={formData.logDate}
                  onChange={(e) => handleChange('logDate', e.target.value)}
                  className={`w-full px-4 py-3 bg-white/5 border ${
                    errors.logDate ? 'border-red-500' : 'border-white/10'
                  } rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all`}
                />
                {errors.logDate && (
                  <p className="mt-1 text-sm text-red-400 flex items-center space-x-1">
                    <AlertCircle className="w-3 h-3" />
                    <span>{errors.logDate}</span>
                  </p>
                )}
              </div>
            </div>

            {/* Time Row */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* Start Time */}
              <div>
                <label className="block text-sm font-medium text-white/80 mb-2 flex items-center space-x-1">
                  <Clock className="w-4 h-4" />
                  <span>시작 시간</span>
                </label>
                <input
                  type="time"
                  value={formData.startTime}
                  onChange={(e) => handleChange('startTime', e.target.value)}
                  className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all"
                />
              </div>

              {/* End Time */}
              <div>
                <label className="block text-sm font-medium text-white/80 mb-2 flex items-center space-x-1">
                  <Clock className="w-4 h-4" />
                  <span>종료 시간</span>
                </label>
                <input
                  type="time"
                  value={formData.endTime}
                  onChange={(e) => handleChange('endTime', e.target.value)}
                  className={`w-full px-4 py-3 bg-white/5 border ${
                    errors.endTime ? 'border-red-500' : 'border-white/10'
                  } rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all`}
                />
                {errors.endTime && (
                  <p className="mt-1 text-sm text-red-400 flex items-center space-x-1">
                    <AlertCircle className="w-3 h-3" />
                    <span>{errors.endTime}</span>
                  </p>
                )}
              </div>
            </div>

            {/* Title */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-2">
                제목 <span className="text-red-400">*</span>
              </label>
              <input
                type="text"
                value={formData.title}
                onChange={(e) => handleChange('title', e.target.value)}
                placeholder="오늘 한 작업을 간단히 요약해주세요"
                className={`w-full px-4 py-3 bg-white/5 border ${
                  errors.title ? 'border-red-500' : 'border-white/10'
                } rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all`}
              />
              {errors.title && (
                <p className="mt-1 text-sm text-red-400 flex items-center space-x-1">
                  <AlertCircle className="w-3 h-3" />
                  <span>{errors.title}</span>
                </p>
              )}
            </div>

            {/* Description */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-2">
                설명
              </label>
              <textarea
                value={formData.description}
                onChange={(e) => handleChange('description', e.target.value)}
                placeholder="작업에 대한 자세한 설명을 입력하세요"
                rows={3}
                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all resize-none"
              />
            </div>
          </div>
        </div>

        {/* Reflection Card */}
        <div className="glass rounded-2xl p-6">
          <h3 className="text-xl font-semibold text-white mb-4 flex items-center space-x-2">
            <Code className="w-5 h-5 text-blue-400" />
            <span>회고</span>
          </h3>

          <div className="space-y-4">
            {/* Achievements */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-2">
                성과 (Achievements)
              </label>
              <textarea
                value={formData.achievements}
                onChange={(e) => handleChange('achievements', e.target.value)}
                placeholder="오늘 달성한 것들을 기록하세요"
                rows={4}
                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all resize-none"
              />
            </div>

            {/* Challenges */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-2">
                도전과제 (Challenges)
              </label>
              <textarea
                value={formData.challenges}
                onChange={(e) => handleChange('challenges', e.target.value)}
                placeholder="어려웠던 점이나 해결이 필요한 문제를 기록하세요"
                rows={4}
                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all resize-none"
              />
            </div>

            {/* Learnings */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-2">
                배운 점 (Learnings)
              </label>
              <textarea
                value={formData.learnings}
                onChange={(e) => handleChange('learnings', e.target.value)}
                placeholder="새로 배운 내용이나 깨달은 점을 기록하세요"
                rows={4}
                className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all resize-none"
              />
            </div>

            {/* Code Snippets */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-2 flex items-center space-x-1">
                <Code className="w-4 h-4" />
                <span>코드 스니펫</span>
              </label>
              <textarea
                value={formData.codeSnippets}
                onChange={(e) => handleChange('codeSnippets', e.target.value)}
                placeholder="중요한 코드나 참고할 코드를 기록하세요"
                rows={6}
                className="w-full px-4 py-3 bg-black/30 border border-white/10 rounded-xl text-green-400 placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all resize-none font-mono text-sm"
              />
            </div>
          </div>
        </div>

        {/* Tags and Mood Card */}
        <div className="glass rounded-2xl p-6">
          <h3 className="text-xl font-semibold text-white mb-4 flex items-center space-x-2">
            <Tag className="w-5 h-5 text-green-400" />
            <span>태그 & 기분</span>
          </h3>

          <div className="space-y-6">
            {/* Tech Tags */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-3">
                기술 태그
              </label>

              {/* Tag Selection */}
              <div className="flex flex-wrap gap-2 mb-3">
                {availableTags.map((tag) => (
                  <button
                    key={tag.id}
                    type="button"
                    onClick={() => handleTagToggle(tag.id)}
                    className={`px-3 py-2 rounded-lg text-sm font-medium transition-all ${
                      formData.techTagIds.includes(tag.id)
                        ? 'bg-purple-500/30 text-purple-300 border-2 border-purple-500'
                        : 'bg-white/5 text-white/70 border border-white/10 hover:bg-white/10'
                    }`}
                  >
                    {tag.name}
                  </button>
                ))}
              </div>

              {/* Add New Tag */}
              <div className="flex items-center space-x-2">
                <input
                  type="text"
                  value={newTagInput}
                  onChange={(e) => setNewTagInput(e.target.value)}
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      e.preventDefault();
                      handleAddNewTag();
                    }
                  }}
                  placeholder="새 태그 추가..."
                  className="flex-1 px-4 py-2 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all"
                />
                <button
                  type="button"
                  onClick={handleAddNewTag}
                  className="px-4 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
                >
                  추가
                </button>
              </div>
            </div>

            {/* Mood */}
            <div>
              <label className="block text-sm font-medium text-white/80 mb-3 flex items-center space-x-1">
                <Smile className="w-4 h-4" />
                <span>오늘의 기분</span>
              </label>
              <div className="flex flex-wrap gap-3">
                {moods.map((mood) => (
                  <button
                    key={mood.value}
                    type="button"
                    onClick={() => handleChange('mood', mood.value)}
                    className={`flex flex-col items-center space-y-2 px-6 py-4 rounded-xl transition-all ${
                      formData.mood === mood.value
                        ? 'bg-gradient-to-br from-purple-500/30 to-blue-500/30 border-2 border-purple-500 scale-110'
                        : 'bg-white/5 border border-white/10 hover:bg-white/10 hover:scale-105'
                    }`}
                  >
                    <span className="text-3xl">{mood.emoji}</span>
                    <span className="text-sm text-white/80">{mood.label}</span>
                  </button>
                ))}
              </div>
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center justify-end space-x-4">
          <button
            type="button"
            onClick={handleCancel}
            className="px-6 py-3 bg-white/5 border border-white/10 text-white rounded-xl hover:bg-white/10 transition-all flex items-center space-x-2"
          >
            <X className="w-5 h-5" />
            <span>취소</span>
          </button>
          <button
            type="submit"
            disabled={loading}
            className="px-6 py-3 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all flex items-center space-x-2 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <Save className="w-5 h-5" />
            <span>{loading ? '저장 중...' : isEdit ? '수정하기' : '작성하기'}</span>
          </button>
        </div>
      </form>
    </div>
  );
};

export default LogForm;
