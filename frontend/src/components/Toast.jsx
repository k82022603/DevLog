import React, { useEffect } from 'react';
import { CheckCircle, XCircle, AlertCircle, X } from 'lucide-react';

const Toast = ({ message, type = 'success', onClose, duration = 3000 }) => {
  useEffect(() => {
    if (duration > 0) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);

      return () => clearTimeout(timer);
    }
  }, [duration, onClose]);

  const icons = {
    success: <CheckCircle className="w-5 h-5 text-green-400" />,
    error: <XCircle className="w-5 h-5 text-red-400" />,
    warning: <AlertCircle className="w-5 h-5 text-yellow-400" />,
  };

  const colors = {
    success: 'from-green-500/20 to-emerald-500/20 border-green-500/50',
    error: 'from-red-500/20 to-rose-500/20 border-red-500/50',
    warning: 'from-yellow-500/20 to-orange-500/20 border-yellow-500/50',
  };

  return (
    <div
      className={`fixed top-4 right-4 z-50 glass rounded-xl border bg-gradient-to-br ${colors[type]} p-4 shadow-glow animate-slide-in-right flex items-center space-x-3 min-w-[300px] max-w-md`}
    >
      {icons[type]}
      <p className="text-white flex-1">{message}</p>
      <button
        onClick={onClose}
        className="text-white/60 hover:text-white transition-colors"
      >
        <X className="w-4 h-4" />
      </button>
    </div>
  );
};

export default Toast;
