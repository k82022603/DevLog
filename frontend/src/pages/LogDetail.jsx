import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeft,
  Edit,
  Trash2,
  Calendar,
  Clock,
  Folder,
  Tag,
  Smile,
  Code,
  CheckCircle,
  AlertCircle,
  Lightbulb,
} from 'lucide-react';
import { devLogApi } from '../services/api';
import { formatDate, formatRelativeTime } from '../utils/formatters';
import Toast from '../components/Toast';

const LogDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [log, setLog] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [toast, setToast] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  useEffect(() => {
    fetchLog();
  }, [id]);

  const fetchLog = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await devLogApi.getById(id);
      setLog(response.data);
    } catch (err) {
      console.error('Error fetching log:', err);
      setError('로그를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    navigate(`/logs/${id}/edit`);
  };

  const handleDelete = async () => {
    try {
      await devLogApi.delete(id);
      showToast('로그가 삭제되었습니다.', 'success');
      setTimeout(() => {
        navigate('/logs');
      }, 1000);
    } catch (err) {
      console.error('Error deleting log:', err);
      showToast('로그 삭제에 실패했습니다.', 'error');
    }
    setShowDeleteModal(false);
  };

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
  };

  const closeToast = () => {
    setToast(null);
  };

  // Get mood emoji
  const getMoodEmoji = (mood) => {
    const moods = {
      EXCELLENT: '🤩',
      GOOD: '😊',
      NORMAL: '😐',
      BAD: '😔',
      TERRIBLE: '😫',
    };
    return moods[mood] || '😐';
  };

  // Format work time
  const formatWorkTime = (startTime, endTime) => {
    if (!startTime || !endTime) return null;
    const start = new Date(`2000-01-01T${startTime}`);
    const end = new Date(`2000-01-01T${endTime}`);
    const diff = (end - start) / 1000 / 60; // minutes
    const hours = Math.floor(diff / 60);
    const mins = Math.floor(diff % 60);
    if (hours === 0) return `${mins}분`;
    if (mins === 0) return `${hours}시간`;
    return `${hours}시간 ${mins}분`;
  };

  // Loading state
  if (loading) {
    return (
      <div className="space-y-6 animate-slide-up">
        <div className="glass rounded-2xl p-8">
          <div className="animate-pulse space-y-4">
            <div className="h-8 w-3/4 bg-white/10 rounded"></div>
            <div className="h-4 w-1/2 bg-white/10 rounded"></div>
            <div className="h-32 bg-white/10 rounded"></div>
          </div>
        </div>
      </div>
    );
  }

  // Error state
  if (error || !log) {
    return (
      <div className="space-y-6 animate-slide-up">
        <div className="flex items-center justify-center py-20">
          <div className="text-center">
            <div className="inline-flex p-4 rounded-full bg-red-500/10 mb-4">
              <AlertCircle className="w-8 h-8 text-red-400" />
            </div>
            <p className="text-white/80 mb-4">{error || '로그를 찾을 수 없습니다.'}</p>
            <button
              onClick={() => navigate('/logs')}
              className="px-6 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
            >
              목록으로
            </button>
          </div>
        </div>
      </div>
    );
  }

  const workTime = formatWorkTime(log.startTime, log.endTime);

  return (
    <div className="space-y-6 animate-slide-up">
      {/* Toast */}
      {toast && <Toast message={toast.message} type={toast.type} onClose={closeToast} />}

      {/* Delete Confirmation Modal */}
      {showDeleteModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/50 backdrop-blur-sm" onClick={() => setShowDeleteModal(false)}></div>
          <div className="relative glass rounded-2xl p-6 max-w-md w-full">
            <h3 className="text-xl font-bold text-white mb-2">로그 삭제</h3>
            <p className="text-white/70 mb-6">정말로 이 로그를 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.</p>
            <div className="flex space-x-3">
              <button
                onClick={() => setShowDeleteModal(false)}
                className="flex-1 px-4 py-2 bg-white/10 hover:bg-white/20 text-white rounded-xl transition-all"
              >
                취소
              </button>
              <button
                onClick={handleDelete}
                className="flex-1 px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-xl transition-all"
              >
                삭제
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Header */}
      <div className="flex items-center justify-between">
        <button
          onClick={() => navigate('/logs')}
          className="flex items-center space-x-2 text-white/70 hover:text-white transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
          <span>목록으로</span>
        </button>

        <div className="flex items-center space-x-2">
          <button
            onClick={handleEdit}
            className="flex items-center space-x-2 px-4 py-2 bg-white/10 hover:bg-white/20 text-white rounded-xl transition-all"
          >
            <Edit className="w-4 h-4" />
            <span>수정</span>
          </button>
          <button
            onClick={() => setShowDeleteModal(true)}
            className="flex items-center space-x-2 px-4 py-2 bg-red-500/20 hover:bg-red-500/30 text-red-400 rounded-xl transition-all"
          >
            <Trash2 className="w-4 h-4" />
            <span>삭제</span>
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className="glass rounded-2xl p-8">
        {/* Title */}
        <h1 className="text-4xl font-display font-bold text-white mb-4">{log.title}</h1>

        {/* Meta Info */}
        <div className="flex flex-wrap gap-4 mb-8 text-sm">
          <div className="flex items-center space-x-2 text-white/70">
            <Calendar className="w-4 h-4" />
            <span>{formatDate(log.logDate)}</span>
          </div>

          {workTime && (
            <div className="flex items-center space-x-2 text-white/70">
              <Clock className="w-4 h-4" />
              <span>{workTime}</span>
            </div>
          )}

          {log.projectName && (
            <div className="flex items-center space-x-2 text-white/70">
              <Folder className="w-4 h-4" />
              <span>{log.projectName}</span>
            </div>
          )}

          {log.mood && (
            <div className="flex items-center space-x-2 text-white/70">
              <Smile className="w-4 h-4" />
              <span>{getMoodEmoji(log.mood)}</span>
            </div>
          )}
        </div>

        {/* Description */}
        {log.description && (
          <div className="mb-8">
            <h2 className="text-xl font-semibold text-white mb-3 flex items-center space-x-2">
              <span>📝</span>
              <span>오늘의 작업</span>
            </h2>
            <div className="prose prose-invert max-w-none">
              <p className="text-white/80 leading-relaxed whitespace-pre-wrap">{log.description}</p>
            </div>
          </div>
        )}

        {/* Achievements */}
        {log.achievements && (
          <div className="mb-8 p-6 rounded-xl bg-green-500/10 border border-green-500/20">
            <h2 className="text-xl font-semibold text-white mb-3 flex items-center space-x-2">
              <CheckCircle className="w-5 h-5 text-green-400" />
              <span>성과 및 완료 사항</span>
            </h2>
            <p className="text-white/80 leading-relaxed whitespace-pre-wrap">{log.achievements}</p>
          </div>
        )}

        {/* Challenges */}
        {log.challenges && (
          <div className="mb-8 p-6 rounded-xl bg-orange-500/10 border border-orange-500/20">
            <h2 className="text-xl font-semibold text-white mb-3 flex items-center space-x-2">
              <AlertCircle className="w-5 h-5 text-orange-400" />
              <span>어려움 및 문제점</span>
            </h2>
            <p className="text-white/80 leading-relaxed whitespace-pre-wrap">{log.challenges}</p>
          </div>
        )}

        {/* Learnings */}
        {log.learnings && (
          <div className="mb-8 p-6 rounded-xl bg-blue-500/10 border border-blue-500/20">
            <h2 className="text-xl font-semibold text-white mb-3 flex items-center space-x-2">
              <Lightbulb className="w-5 h-5 text-blue-400" />
              <span>배운 점 및 개선 사항</span>
            </h2>
            <p className="text-white/80 leading-relaxed whitespace-pre-wrap">{log.learnings}</p>
          </div>
        )}

        {/* Code Snippets */}
        {log.codeSnippets && (
          <div className="mb-8">
            <h2 className="text-xl font-semibold text-white mb-3 flex items-center space-x-2">
              <Code className="w-5 h-5" />
              <span>코드 스니펫</span>
            </h2>
            <pre className="bg-black/30 rounded-xl p-4 overflow-x-auto">
              <code className="text-sm text-white/90 font-mono">{log.codeSnippets}</code>
            </pre>
          </div>
        )}

        {/* Tags */}
        {log.tags && (
          <div>
            <h2 className="text-xl font-semibold text-white mb-3 flex items-center space-x-2">
              <Tag className="w-5 h-5" />
              <span>기술 태그</span>
            </h2>
            <div className="flex flex-wrap gap-2">
              {log.tags.split(',').map((tag, index) => (
                <span
                  key={index}
                  className="px-3 py-1 rounded-lg bg-gradient-to-r from-purple-500/20 to-blue-500/20 border border-purple-500/30 text-purple-300 text-sm"
                >
                  #{tag.trim()}
                </span>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Footer Meta */}
      <div className="glass rounded-xl p-4 flex items-center justify-between text-sm text-white/50">
        <div>
          작성일: {formatDate(log.createdAt)}
          {log.updatedAt && log.updatedAt !== log.createdAt && (
            <span className="ml-4">수정일: {formatDate(log.updatedAt)}</span>
          )}
        </div>
        <div>{formatRelativeTime(log.createdAt)}</div>
      </div>
    </div>
  );
};

export default LogDetail;
