import React, { useEffect } from 'react';
import { Plus, Keyboard } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const QuickActions = () => {
  const navigate = useNavigate();

  // Keyboard shortcut: Ctrl+N or Cmd+N
  useEffect(() => {
    const handleKeyDown = (e) => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'n') {
        e.preventDefault();
        handleNewLog();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, []);

  const handleNewLog = () => {
    navigate('/logs/new');
  };

  return (
    <>
      {/* Floating Action Button */}
      <button
        onClick={handleNewLog}
        className="fixed bottom-8 right-8 z-30 group"
        title="오늘 로그 추가 (Ctrl+N)"
      >
        <div className="relative">
          {/* Main button */}
          <div className="w-16 h-16 rounded-full bg-gradient-to-r from-purple-500 to-blue-500 shadow-glow flex items-center justify-center transition-transform duration-300 group-hover:scale-110">
            <Plus className="w-8 h-8 text-white" />
          </div>

          {/* Ripple effect */}
          <div className="absolute inset-0 rounded-full bg-gradient-to-r from-purple-500 to-blue-500 opacity-30 animate-ping"></div>

          {/* Tooltip */}
          <div className="absolute bottom-full right-0 mb-2 opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none">
            <div className="glass px-4 py-2 rounded-xl whitespace-nowrap">
              <p className="text-sm text-white font-medium">오늘 로그 추가</p>
              <div className="flex items-center space-x-1 mt-1">
                <Keyboard className="w-3 h-3 text-white/60" />
                <kbd className="text-xs px-1.5 py-0.5 rounded bg-white/10 text-white/60">
                  Ctrl+N
                </kbd>
              </div>
            </div>
          </div>
        </div>
      </button>

      {/* Keyboard shortcuts hint */}
      <div className="fixed bottom-8 left-8 z-30 hidden xl:block">
        <div className="glass px-4 py-3 rounded-xl">
          <div className="flex items-center space-x-2 mb-2">
            <Keyboard className="w-4 h-4 text-white/60" />
            <span className="text-sm font-medium text-white/80">단축키</span>
          </div>
          <div className="space-y-1 text-xs text-white/60">
            <div className="flex items-center space-x-2">
              <kbd className="px-2 py-0.5 rounded bg-white/10">Ctrl+K</kbd>
              <span>검색</span>
            </div>
            <div className="flex items-center space-x-2">
              <kbd className="px-2 py-0.5 rounded bg-white/10">Ctrl+N</kbd>
              <span>새 로그</span>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default QuickActions;
