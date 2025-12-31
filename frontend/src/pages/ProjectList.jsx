import React, { useState, useEffect } from 'react';
import { Plus, Folder, Filter, X, Search } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import ProjectCard from '../components/ProjectCard';
import { ListSkeleton } from '../components/Skeleton';
import { projectApi, statisticsApi } from '../services/api';
import Toast from '../components/Toast';

const ProjectList = () => {
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [filteredProjects, setFilteredProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [toast, setToast] = useState(null);

  // Filters
  const [selectedStatus, setSelectedStatus] = useState('ALL');
  const [searchQuery, setSearchQuery] = useState('');

  // Status options
  const statusOptions = [
    { value: 'ALL', label: '전체', color: 'from-gray-500 to-slate-500' },
    { value: 'ACTIVE', label: '진행중', color: 'from-green-500 to-emerald-500' },
    { value: 'COMPLETED', label: '완료', color: 'from-blue-500 to-cyan-500' },
    { value: 'ON_HOLD', label: '보류', color: 'from-yellow-500 to-orange-500' },
    { value: 'ARCHIVED', label: '보관', color: 'from-gray-600 to-slate-600' },
  ];

  useEffect(() => {
    fetchProjects();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [projects, selectedStatus, searchQuery]);

  const fetchProjects = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await projectApi.getAll();
      const projectsData = response.data || [];

      // Fetch stats for each project
      const projectsWithStats = await Promise.all(
        projectsData.map(async (project) => {
          try {
            const statsResponse = await statisticsApi.getProject(project.id);
            const stats = statsResponse.data;
            return {
              ...project,
              totalLogs: stats.totalLogs || 0,
              totalWorkMinutes: stats.totalWorkMinutes || 0,
            };
          } catch (err) {
            console.error(`Error fetching stats for project ${project.id}:`, err);
            return {
              ...project,
              totalLogs: 0,
              totalWorkMinutes: 0,
            };
          }
        })
      );

      setProjects(projectsWithStats);
    } catch (err) {
      console.error('Error fetching projects:', err);
      setError('프로젝트를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...projects];

    // Status filter
    if (selectedStatus !== 'ALL') {
      filtered = filtered.filter((project) => project.status === selectedStatus);
    }

    // Search filter
    if (searchQuery.trim()) {
      filtered = filtered.filter(
        (project) =>
          project.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
          (project.description &&
            project.description.toLowerCase().includes(searchQuery.toLowerCase()))
      );
    }

    setFilteredProjects(filtered);
  };

  const handleCreateProject = () => {
    navigate('/projects/new');
  };

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
  };

  const closeToast = () => {
    setToast(null);
  };

  // Get stats for display
  const getStats = () => {
    const total = projects.length;
    const active = projects.filter((p) => p.status === 'ACTIVE').length;
    const completed = projects.filter((p) => p.status === 'COMPLETED').length;

    return { total, active, completed };
  };

  const stats = getStats();

  return (
    <div className="space-y-6 animate-slide-up">
      {/* Toast */}
      {toast && (
        <Toast message={toast.message} type={toast.type} onClose={closeToast} />
      )}

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-4xl font-display font-bold text-white mb-2">
            프로젝트
          </h2>
          <p className="text-white/60 text-lg">
            {stats.total}개의 프로젝트 • 진행중 {stats.active}개 • 완료 {stats.completed}개
          </p>
        </div>
        <button
          onClick={handleCreateProject}
          className="px-6 py-3 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all flex items-center space-x-2"
        >
          <Plus className="w-5 h-5" />
          <span>새 프로젝트</span>
        </button>
      </div>

      {/* Filters */}
      <div className="glass rounded-2xl p-4">
        <div className="flex items-center space-x-2 mb-4">
          <Filter className="w-5 h-5 text-white/60" />
          <span className="text-white font-medium">필터</span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* Search */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-white/40" />
            <input
              type="text"
              placeholder="프로젝트 검색..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all"
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-white/40 hover:text-white transition-colors"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>

          {/* Status Filter */}
          <div className="flex flex-wrap gap-2">
            {statusOptions.map((option) => (
              <button
                key={option.value}
                onClick={() => setSelectedStatus(option.value)}
                className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${
                  selectedStatus === option.value
                    ? `bg-gradient-to-r ${option.color} text-white shadow-glow`
                    : 'bg-white/5 text-white/70 hover:bg-white/10'
                }`}
              >
                {option.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Loading State */}
      {loading && <ListSkeleton count={6} type="project" />}

      {/* Error State */}
      {error && (
        <div className="flex items-center justify-center py-20">
          <div className="text-center">
            <div className="inline-flex p-4 rounded-full bg-red-500/10 mb-4">
              <X className="w-8 h-8 text-red-400" />
            </div>
            <p className="text-white/80 mb-4">{error}</p>
            <button
              onClick={fetchProjects}
              className="px-6 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
            >
              다시 시도
            </button>
          </div>
        </div>
      )}

      {/* Empty State */}
      {!loading && !error && filteredProjects.length === 0 && (
        <div className="flex items-center justify-center py-20">
          <div className="text-center">
            <div className="inline-flex p-4 rounded-full bg-white/5 mb-4">
              <Folder className="w-8 h-8 text-white/40" />
            </div>
            <p className="text-white/60 mb-4">
              {searchQuery || selectedStatus !== 'ALL'
                ? '검색 결과가 없습니다.'
                : '아직 프로젝트가 없습니다.'}
            </p>
            <button
              onClick={handleCreateProject}
              className="px-6 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
            >
              첫 번째 프로젝트 만들기
            </button>
          </div>
        </div>
      )}

      {/* Project Grid */}
      {!loading && !error && filteredProjects.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredProjects.map((project, index) => (
            <div
              key={project.id}
              style={{ animationDelay: `${index * 50}ms` }}
              className="animate-slide-up"
            >
              <ProjectCard project={project} />
            </div>
          ))}
        </div>
      )}

      {/* Summary Footer */}
      {!loading && !error && filteredProjects.length > 0 && (
        <div className="glass rounded-xl p-4">
          <div className="flex items-center justify-between text-sm text-white/60">
            <span>
              {filteredProjects.length}개의 프로젝트 표시 중
              {(searchQuery || selectedStatus !== 'ALL') && ` (필터 적용)`}
            </span>
            {(searchQuery || selectedStatus !== 'ALL') && (
              <button
                onClick={() => {
                  setSearchQuery('');
                  setSelectedStatus('ALL');
                }}
                className="text-purple-400 hover:text-purple-300 transition-colors"
              >
                필터 초기화
              </button>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default ProjectList;
