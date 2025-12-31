import React from 'react';
import { Clock, Edit, Trash2, Calendar } from 'lucide-react';
import { formatRelativeTime, formatDate } from '../utils/formatters';

const LogCard = ({ log, onEdit, onDelete }) => {
  // Format work time
  const formatWorkTime = (minutes) => {
    if (!minutes) return null;
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours === 0) return `${mins}분`;
    if (mins === 0) return `${hours}시간`;
    return `${hours}시간 ${mins}분`;
  };

  // Get project color or default gradient
  const getProjectColor = () => {
    if (!log.projectName) return 'from-gray-500 to-gray-600';

    // Hash project name to get consistent color
    const colors = [
      'from-purple-500 to-pink-500',
      'from-blue-500 to-cyan-500',
      'from-green-500 to-emerald-500',
      'from-orange-500 to-red-500',
      'from-indigo-500 to-purple-500',
      'from-pink-500 to-rose-500',
    ];

    const hash = log.projectName.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
    return colors[hash % colors.length];
  };

  return (
    <div className="glass rounded-2xl p-6 card-hover group relative overflow-hidden">
      {/* Project color bar on left */}
      <div className={`absolute left-0 top-0 bottom-0 w-1 bg-gradient-to-b ${getProjectColor()}`}></div>

      <div className="flex items-start justify-between mb-4">
        {/* Project and Time Info */}
        <div className="flex items-start space-x-4 flex-1">
          {/* Project Icon */}
          {log.projectName && (
            <div className={`flex-shrink-0 w-12 h-12 rounded-xl bg-gradient-to-br ${getProjectColor()} flex items-center justify-center shadow-glow`}>
              <span className="text-white font-bold text-lg">
                {log.projectName.charAt(0).toUpperCase()}
              </span>
            </div>
          )}

          {/* Content */}
          <div className="flex-1 min-w-0">
            {/* Project Name and Date */}
            <div className="flex items-center space-x-2 mb-2">
              {log.projectName && (
                <span className={`text-sm font-medium bg-gradient-to-r ${getProjectColor()} bg-clip-text text-transparent`}>
                  {log.projectName}
                </span>
              )}
              <span className="text-white/40">•</span>
              <div className="flex items-center space-x-1 text-white/60 text-sm">
                <Calendar className="w-3 h-3" />
                <span>{formatDate(log.logDate)}</span>
              </div>
              <span className="text-white/40 text-xs">
                ({formatRelativeTime(log.logDate)})
              </span>
            </div>

            {/* Title */}
            <a
              href={`/logs/${log.id}`}
              className="block mb-2"
            >
              <h3 className="text-xl font-semibold text-white group-hover:text-blue-400 transition-colors line-clamp-2">
                {log.title}
              </h3>
            </a>

            {/* Description */}
            {log.description && (
              <p className="text-white/70 text-sm line-clamp-2 mb-3">
                {log.description}
              </p>
            )}

            {/* Work Time */}
            {log.workMinutes > 0 && (
              <div className="flex items-center space-x-2 mb-3">
                <Clock className="w-4 h-4 text-blue-400" />
                <span className="text-sm text-white/80">
                  작업 시간: <span className="font-semibold text-blue-400">{formatWorkTime(log.workMinutes)}</span>
                </span>
              </div>
            )}

            {/* Tech Tags */}
            {log.techTags && log.techTags.length > 0 && (
              <div className="flex flex-wrap gap-2">
                {log.techTags.map((tag) => (
                  <span
                    key={tag.id}
                    className="px-3 py-1 rounded-lg text-xs font-medium transition-all hover:scale-105"
                    style={{
                      backgroundColor: tag.color ? `${tag.color}20` : 'rgba(255, 255, 255, 0.1)',
                      color: tag.color || '#ffffff',
                      border: `1px solid ${tag.color ? `${tag.color}40` : 'rgba(255, 255, 255, 0.2)'}`,
                    }}
                  >
                    {tag.name}
                  </span>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center space-x-2 ml-4 opacity-0 group-hover:opacity-100 transition-opacity">
          <button
            onClick={(e) => {
              e.preventDefault();
              onEdit(log.id);
            }}
            className="p-2 rounded-lg bg-white/5 hover:bg-blue-500/20 text-white/60 hover:text-blue-400 transition-all"
            title="수정"
          >
            <Edit className="w-4 h-4" />
          </button>
          <button
            onClick={(e) => {
              e.preventDefault();
              onDelete(log.id);
            }}
            className="p-2 rounded-lg bg-white/5 hover:bg-red-500/20 text-white/60 hover:text-red-400 transition-all"
            title="삭제"
          >
            <Trash2 className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
};

export default LogCard;
