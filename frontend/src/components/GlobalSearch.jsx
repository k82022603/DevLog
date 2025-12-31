import React, { useState, useEffect, useRef } from 'react';
import { Search, X, FileText, Calendar } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { devLogApi } from '../services/api';
import { formatRelativeTime } from '../utils/formatters';

const GlobalSearch = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const inputRef = useRef(null);
  const navigate = useNavigate();

  // Keyboard shortcut: Ctrl+K or Cmd+K
  useEffect(() => {
    const handleKeyDown = (e) => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        setIsOpen(true);
      }
      if (e.key === 'Escape') {
        setIsOpen(false);
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  // Focus input when opened
  useEffect(() => {
    if (isOpen && inputRef.current) {
      inputRef.current.focus();
    }
  }, [isOpen]);

  // Search logs
  useEffect(() => {
    if (!query.trim()) {
      setResults([]);
      return;
    }

    const searchLogs = async () => {
      try {
        setLoading(true);
        const response = await devLogApi.getAll({ keyword: query });
        setResults((response.data || []).slice(0, 5)); // Limit to 5 results
      } catch (err) {
        console.error('Search error:', err);
        setResults([]);
      } finally {
        setLoading(false);
      }
    };

    const debounce = setTimeout(searchLogs, 300);
    return () => clearTimeout(debounce);
  }, [query]);

  const handleResultClick = (logId) => {
    navigate(`/logs/${logId}`);
    setIsOpen(false);
    setQuery('');
  };

  if (!isOpen) {
    return (
      <button
        onClick={() => setIsOpen(true)}
        className="flex items-center space-x-2 px-3 py-2 rounded-xl bg-white/5 hover:bg-white/10 transition-all text-white/60 hover:text-white"
      >
        <Search className="w-4 h-4" />
        <span className="text-sm hidden md:inline">검색</span>
        <kbd className="hidden lg:inline-flex items-center px-2 py-0.5 rounded bg-white/10 text-xs text-white/60">
          Ctrl+K
        </kbd>
      </button>
    );
  }

  return (
    <>
      {/* Overlay */}
      <div
        className="fixed inset-0 bg-black/50 backdrop-blur-sm z-50"
        onClick={() => setIsOpen(false)}
      ></div>

      {/* Search Modal */}
      <div className="fixed top-20 left-1/2 transform -translate-x-1/2 w-full max-w-2xl z-50 px-4">
        <div className="glass rounded-2xl shadow-2xl border border-white/20 overflow-hidden animate-slide-up">
          {/* Search Input */}
          <div className="flex items-center px-6 py-4 border-b border-white/10">
            <Search className="w-5 h-5 text-white/60" />
            <input
              ref={inputRef}
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="로그 검색... (제목, 설명, 태그)"
              className="flex-1 ml-3 bg-transparent text-white placeholder-white/40 focus:outline-none"
            />
            {query && (
              <button
                onClick={() => setQuery('')}
                className="p-1 rounded-lg hover:bg-white/10 transition-colors"
              >
                <X className="w-4 h-4 text-white/60" />
              </button>
            )}
          </div>

          {/* Results */}
          <div className="max-h-96 overflow-y-auto">
            {loading ? (
              <div className="px-6 py-8 text-center">
                <div className="inline-flex p-3 rounded-full bg-white/5 mb-3 animate-pulse">
                  <Search className="w-6 h-6 text-white/40" />
                </div>
                <p className="text-white/60 text-sm">검색 중...</p>
              </div>
            ) : results.length > 0 ? (
              <div className="py-2">
                {results.map((log) => (
                  <button
                    key={log.id}
                    onClick={() => handleResultClick(log.id)}
                    className="w-full px-6 py-3 text-left hover:bg-white/5 transition-colors flex items-start space-x-3"
                  >
                    <FileText className="w-5 h-5 text-blue-400 mt-1 flex-shrink-0" />
                    <div className="flex-1 min-w-0">
                      <h4 className="text-white font-medium line-clamp-1">
                        {log.title}
                      </h4>
                      {log.description && (
                        <p className="text-white/60 text-sm line-clamp-1 mt-1">
                          {log.description}
                        </p>
                      )}
                      <div className="flex items-center space-x-3 mt-2 text-xs text-white/40">
                        <div className="flex items-center space-x-1">
                          <Calendar className="w-3 h-3" />
                          <span>{formatRelativeTime(log.logDate)}</span>
                        </div>
                        {log.projectName && (
                          <span className="px-2 py-0.5 rounded-lg bg-purple-500/20 text-purple-300">
                            {log.projectName}
                          </span>
                        )}
                      </div>
                    </div>
                  </button>
                ))}
              </div>
            ) : query ? (
              <div className="px-6 py-8 text-center">
                <div className="inline-flex p-3 rounded-full bg-white/5 mb-3">
                  <Search className="w-6 h-6 text-white/40" />
                </div>
                <p className="text-white/60 text-sm">검색 결과가 없습니다.</p>
              </div>
            ) : (
              <div className="px-6 py-8 text-center">
                <div className="inline-flex p-3 rounded-full bg-white/5 mb-3">
                  <Search className="w-6 h-6 text-white/40" />
                </div>
                <p className="text-white/60 text-sm">
                  제목, 설명, 태그로 로그를 검색하세요
                </p>
              </div>
            )}
          </div>

          {/* Footer */}
          <div className="px-6 py-3 border-t border-white/10 flex items-center justify-between text-xs text-white/40">
            <span>Enter로 선택</span>
            <span>ESC로 닫기</span>
          </div>
        </div>
      </div>
    </>
  );
};

export default GlobalSearch;
