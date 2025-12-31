import React from 'react';
import { Clock, FileText, TrendingUp, Calendar } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { formatDate } from '../utils/formatters';

const ProjectCard = ({ project }) => {
  const navigate = useNavigate();

  // Get status badge color
  const getStatusColor = (status) => {
    const colors = {
      ACTIVE: 'from-green-500 to-emerald-500',
      COMPLETED: 'from-blue-500 to-cyan-500',
      ON_HOLD: 'from-yellow-500 to-orange-500',
      ARCHIVED: 'from-gray-500 to-slate-500',
    };
    return colors[status] || 'from-purple-500 to-pink-500';
  };

  // Get status label
  const getStatusLabel = (status) => {
    const labels = {
      ACTIVE: '진행중',
      COMPLETED: '완료',
      ON_HOLD: '보류',
      ARCHIVED: '보관',
    };
    return labels[status] || status;
  };

  // Get project color based on name hash
  const getProjectColor = () => {
    const colors = [
      'from-purple-500 to-pink-500',
      'from-blue-500 to-cyan-500',
      'from-green-500 to-emerald-500',
      'from-orange-500 to-red-500',
      'from-indigo-500 to-purple-500',
      'from-pink-500 to-rose-500',
    ];

    const hash = project.name.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    return colors[hash % colors.length];
  };

  // Format work time
  const formatWorkTime = (minutes) => {
    if (!minutes) return '0시간';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours === 0) return `${mins}분`;
    if (mins === 0) return `${hours}시간`;
    return `${hours}시간 ${mins}분`;
  };

  const handleClick = () => {
    navigate(`/logs?projectId=${project.id}`);
  };

  const projectColor = getProjectColor();
  const statusColor = getStatusColor(project.status);

  return (
    <div
      onClick={handleClick}
      className="glass rounded-2xl p-6 card-hover group relative overflow-hidden cursor-pointer"
    >
      {/* Background gradient */}
      <div className={`absolute inset-0 bg-gradient-to-br ${projectColor} opacity-5 group-hover:opacity-10 transition-opacity duration-300`}></div>

      {/* Content */}
      <div className="relative z-10">
        {/* Header */}
        <div className="flex items-start justify-between mb-4">
          {/* Project Icon */}
          <div className={`w-14 h-14 rounded-xl bg-gradient-to-br ${projectColor} flex items-center justify-center shadow-glow`}>
            <span className="text-white font-bold text-2xl">
              {project.name.charAt(0).toUpperCase()}
            </span>
          </div>

          {/* Status Badge */}
          <div className={`px-3 py-1 rounded-lg bg-gradient-to-r ${statusColor} text-white text-xs font-semibold shadow-md`}>
            {getStatusLabel(project.status)}
          </div>
        </div>

        {/* Project Name */}
        <h3 className="text-2xl font-bold text-white mb-2 group-hover:text-blue-400 transition-colors line-clamp-1">
          {project.name}
        </h3>

        {/* Description */}
        {project.description && (
          <p className="text-white/70 text-sm mb-4 line-clamp-2 min-h-[2.5rem]">
            {project.description}
          </p>
        )}

        {/* Progress Bar */}
        <div className="mb-4">
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs text-white/60">진행률</span>
            <span className="text-sm font-semibold text-white">{project.progress || 0}%</span>
          </div>
          <div className="h-2 bg-white/10 rounded-full overflow-hidden">
            <div
              className={`h-full bg-gradient-to-r ${projectColor} rounded-full transition-all duration-1000`}
              style={{ width: `${project.progress || 0}%` }}
            ></div>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-2 gap-3 mb-4">
          {/* Total Logs */}
          <div className="flex items-center space-x-2 px-3 py-2 rounded-lg bg-white/5">
            <FileText className="w-4 h-4 text-blue-400" />
            <div className="flex-1 min-w-0">
              <p className="text-xs text-white/60">로그</p>
              <p className="text-sm font-semibold text-white">{project.totalLogs || 0}개</p>
            </div>
          </div>

          {/* Total Work Time */}
          <div className="flex items-center space-x-2 px-3 py-2 rounded-lg bg-white/5">
            <Clock className="w-4 h-4 text-purple-400" />
            <div className="flex-1 min-w-0">
              <p className="text-xs text-white/60">작업시간</p>
              <p className="text-sm font-semibold text-white truncate">
                {formatWorkTime(project.totalWorkMinutes)}
              </p>
            </div>
          </div>
        </div>

        {/* Dates */}
        <div className="flex items-center justify-between text-xs text-white/50">
          <div className="flex items-center space-x-1">
            <Calendar className="w-3 h-3" />
            <span>{formatDate(project.startDate)}</span>
          </div>
          {project.endDate && (
            <div className="flex items-center space-x-1">
              <TrendingUp className="w-3 h-3" />
              <span>{formatDate(project.endDate)}</span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProjectCard;
