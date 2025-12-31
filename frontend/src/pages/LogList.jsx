import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Search, Filter, Plus, Calendar, X } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import LogCard from '../components/LogCard';
import DateNavigator from '../components/DateNavigator';
import { ListSkeleton } from '../components/Skeleton';
import { devLogApi, projectApi } from '../services/api';
import { formatDate } from '../utils/formatters';

const LogList = () => {
  const navigate = useNavigate();
  const [logs, setLogs] = useState([]);
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [hasMore, setHasMore] = useState(true);
  const [page, setPage] = useState(1);
  const observer = useRef();

  // Filters
  const [filters, setFilters] = useState({
    projectId: '',
    startDate: '',
    endDate: '',
    keyword: '',
  });

  const [showFilters, setShowFilters] = useState(false);
  const [selectedDate, setSelectedDate] = useState(null);

  // Fetch projects for filter dropdown
  useEffect(() => {
    fetchProjects();
  }, []);

  // Fetch logs when filters change
  useEffect(() => {
    fetchLogs(true);
  }, [filters]);

  const fetchProjects = async () => {
    try {
      const response = await projectApi.getAll();
      setProjects(response.data || []);
    } catch (err) {
      console.error('Error fetching projects:', err);
    }
  };

  const fetchLogs = async (reset = false) => {
    try {
      setLoading(true);
      setError(null);

      const params = {
        page: reset ? 1 : page,
        size: 10,
      };

      if (filters.projectId) params.projectId = filters.projectId;
      if (filters.startDate) params.startDate = filters.startDate;
      if (filters.endDate) params.endDate = filters.endDate;
      if (filters.keyword) params.keyword = filters.keyword;

      const response = await devLogApi.getAll(params);
      const newLogs = response.data || [];

      if (reset) {
        setLogs(newLogs);
        setPage(1);
      } else {
        setLogs((prev) => [...prev, ...newLogs]);
      }

      setHasMore(newLogs.length === params.size);
    } catch (err) {
      console.error('Error fetching logs:', err);
      setError('로그를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const loadMore = useCallback(() => {
    if (!loading && hasMore) {
      setPage((prev) => prev + 1);
      fetchLogs(false);
    }
  }, [loading, hasMore]);

  // Infinite scroll observer
  const lastLogRef = useCallback(
    (node) => {
      if (loading) return;
      if (observer.current) observer.current.disconnect();

      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasMore) {
          loadMore();
        }
      });

      if (node) observer.current.observe(node);
    },
    [loading, hasMore, loadMore]
  );

  const handleFilterChange = (key, value) => {
    setFilters((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const clearFilters = () => {
    setFilters({
      projectId: '',
      startDate: '',
      endDate: '',
      keyword: '',
    });
    setSelectedDate(null);
  };

  const hasActiveFilters = () => {
    return filters.projectId || filters.startDate || filters.endDate || filters.keyword;
  };

  const handleEdit = (logId) => {
    navigate(`/logs/${logId}/edit`);
  };

  const handleDelete = async (logId) => {
    if (!window.confirm('정말 삭제하시겠습니까?')) return;

    try {
      await devLogApi.delete(logId);
      setLogs((prev) => prev.filter((log) => log.id !== logId));
    } catch (err) {
      console.error('Error deleting log:', err);
      alert('삭제에 실패했습니다.');
    }
  };

  // Group logs by date
  const groupLogsByDate = () => {
    const grouped = {};

    logs.forEach((log) => {
      const date = formatDate(log.logDate);
      if (!grouped[date]) {
        grouped[date] = [];
      }
      grouped[date].push(log);
    });

    return Object.entries(grouped).sort((a, b) => new Date(b[0]) - new Date(a[0]));
  };

  const groupedLogs = groupLogsByDate();

  return (
    <div className="space-y-6 animate-slide-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-4xl font-display font-bold text-white mb-2">개발 로그</h2>
          <p className="text-white/60 text-lg">
            {logs.length}개의 로그
            {hasActiveFilters() && ' (필터 적용 중)'}
          </p>
        </div>
        <button
          onClick={() => navigate('/logs/new')}
          className="px-6 py-3 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all flex items-center space-x-2"
        >
          <Plus className="w-5 h-5" />
          <span>새 로그 작성</span>
        </button>
      </div>

      {/* Filter Bar */}
      <div className="glass rounded-2xl p-4">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-3">
            <Filter className="w-5 h-5 text-white/60" />
            <span className="text-white font-medium">필터</span>
            <div className="relative z-[102]">
              <DateNavigator
                selectedDate={selectedDate}
                onDateSelect={(date) => {
                  setSelectedDate(date);
                  if (date) {
                    const dateStr = date.toISOString().split('T')[0];
                    handleFilterChange('startDate', dateStr);
                    handleFilterChange('endDate', dateStr);
                  } else {
                    handleFilterChange('startDate', '');
                    handleFilterChange('endDate', '');
                  }
                }}
              />
            </div>
            {hasActiveFilters() && (
              <button
                onClick={clearFilters}
                className="text-xs px-2 py-1 rounded-lg bg-red-500/20 text-red-400 hover:bg-red-500/30 transition-all flex items-center space-x-1"
              >
                <X className="w-3 h-3" />
                <span>초기화</span>
              </button>
            )}
          </div>
          <button
            onClick={() => setShowFilters(!showFilters)}
            className="text-sm text-white/60 hover:text-white transition-colors"
          >
            {showFilters ? '숨기기' : '펼치기'}
          </button>
        </div>

        {showFilters && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            {/* Search */}
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-white/40" />
              <input
                type="text"
                placeholder="검색..."
                value={filters.keyword}
                onChange={(e) => handleFilterChange('keyword', e.target.value)}
                className="w-full pl-10 pr-4 py-2 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all"
              />
            </div>

            {/* Project Filter */}
            <div>
              <select
                value={filters.projectId || ""}
                onChange={(e) => handleFilterChange('projectId', e.target.value)}
                className="w-full px-4 py-2 border border-white/10 rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all appearance-none cursor-pointer"
                style={{
                  backgroundColor: 'rgb(97, 61, 132)',
                }}
              >
                <option value="" style={{ backgroundColor: 'rgb(97, 61, 132)', color: 'white' }}>모든 프로젝트</option>
                {projects.map((project) => (
                  <option key={project.id} value={String(project.id)} style={{ backgroundColor: 'rgb(97, 61, 132)', color: 'white' }}>
                    {project.name}
                  </option>
                ))}
              </select>
            </div>

            {/* Start Date */}
            <div className="relative">
              <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-white/40 pointer-events-none" />
              <input
                type="date"
                value={filters.startDate}
                onChange={(e) => handleFilterChange('startDate', e.target.value)}
                className="w-full pl-10 pr-4 py-2 bg-white/5 border border-white/10 rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all"
              />
            </div>

            {/* End Date */}
            <div className="relative">
              <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-white/40 pointer-events-none" />
              <input
                type="date"
                value={filters.endDate}
                onChange={(e) => handleFilterChange('endDate', e.target.value)}
                className="w-full pl-10 pr-4 py-2 bg-white/5 border border-white/10 rounded-xl text-white focus:outline-none focus:border-purple-500/50 transition-all"
              />
            </div>
          </div>
        )}
      </div>

      {/* Loading State */}
      {loading && logs.length === 0 && <ListSkeleton count={5} type="card" />}

      {/* Error State */}
      {error && (
        <div className="flex items-center justify-center py-20">
          <div className="text-center">
            <div className="inline-flex p-4 rounded-full bg-red-500/10 mb-4">
              <X className="w-8 h-8 text-red-400" />
            </div>
            <p className="text-white/80 mb-4">{error}</p>
            <button
              onClick={() => fetchLogs(true)}
              className="px-6 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
            >
              다시 시도
            </button>
          </div>
        </div>
      )}

      {/* Empty State */}
      {!loading && !error && logs.length === 0 && (
        <div className="flex items-center justify-center py-20">
          <div className="text-center">
            <div className="inline-flex p-4 rounded-full bg-white/5 mb-4">
              <Calendar className="w-8 h-8 text-white/40" />
            </div>
            <p className="text-white/60 mb-4">
              {hasActiveFilters() ? '검색 결과가 없습니다.' : '아직 로그가 없습니다.'}
            </p>
            <button
              onClick={() => navigate('/logs/new')}
              className="px-6 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
            >
              첫 번째 로그 작성하기
            </button>
          </div>
        </div>
      )}

      {/* Log List - Grouped by Date */}
      {!loading && !error && logs.length > 0 && (
        <div className="space-y-8">
          {groupedLogs.map(([date, dateLogs], groupIndex) => (
            <div key={date}>
              {/* Date Header */}
              <div className="flex items-center space-x-4 mb-4">
                <div className="flex items-center space-x-2 px-4 py-2 glass rounded-full">
                  <Calendar className="w-4 h-4 text-white/60" />
                  <span className="text-white/90 font-medium">{date}</span>
                </div>
                <div className="flex-1 h-px bg-white/10"></div>
                <span className="text-white/40 text-sm">{dateLogs.length}개</span>
              </div>

              {/* Logs for this date */}
              <div className="space-y-4">
                {dateLogs.map((log, index) => (
                  <div
                    key={log.id}
                    ref={
                      groupIndex === groupedLogs.length - 1 &&
                      index === dateLogs.length - 1
                        ? lastLogRef
                        : null
                    }
                  >
                    <LogCard log={log} onEdit={handleEdit} onDelete={handleDelete} />
                  </div>
                ))}
              </div>
            </div>
          ))}

          {/* Loading More Indicator */}
          {loading && logs.length > 0 && (
            <div className="flex items-center justify-center py-8">
              <div className="text-white/60 text-sm">더 많은 로그 불러오는 중...</div>
            </div>
          )}

          {/* No More Logs */}
          {!loading && !hasMore && logs.length > 0 && (
            <div className="flex items-center justify-center py-8">
              <div className="text-white/40 text-sm">모든 로그를 불러왔습니다.</div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default LogList;
