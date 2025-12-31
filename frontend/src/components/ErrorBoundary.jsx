import React from 'react';
import { AlertTriangle, RefreshCw, Home } from 'lucide-react';

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null, errorInfo: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    console.error('Error caught by boundary:', error, errorInfo);
    this.setState({
      error,
      errorInfo,
    });
  }

  handleReset = () => {
    this.setState({ hasError: false, error: null, errorInfo: null });
    window.location.href = '/';
  };

  handleReload = () => {
    window.location.reload();
  };

  render() {
    if (this.state.hasError) {
      return (
        <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center px-4">
          <div className="max-w-md w-full">
            {/* Error Card */}
            <div className="glass rounded-2xl p-8 text-center">
              {/* Icon */}
              <div className="inline-flex p-4 rounded-full bg-red-500/10 mb-6">
                <AlertTriangle className="w-12 h-12 text-red-400" />
              </div>

              {/* Title */}
              <h1 className="text-2xl font-bold text-white mb-2">
                앗! 문제가 발생했습니다
              </h1>

              {/* Description */}
              <p className="text-white/60 mb-6">
                예상치 못한 오류가 발생했습니다. 불편을 드려 죄송합니다.
              </p>

              {/* Error Details (Development only) */}
              {process.env.NODE_ENV === 'development' && this.state.error && (
                <div className="mb-6 p-4 rounded-xl bg-black/30 text-left">
                  <p className="text-sm font-mono text-red-400 mb-2">
                    {this.state.error.toString()}
                  </p>
                  {this.state.errorInfo && (
                    <details className="text-xs text-white/40">
                      <summary className="cursor-pointer hover:text-white/60">
                        Stack trace
                      </summary>
                      <pre className="mt-2 overflow-auto">
                        {this.state.errorInfo.componentStack}
                      </pre>
                    </details>
                  )}
                </div>
              )}

              {/* Actions */}
              <div className="flex flex-col sm:flex-row gap-3">
                <button
                  onClick={this.handleReload}
                  className="flex-1 flex items-center justify-center space-x-2 px-6 py-3 bg-white/10 hover:bg-white/20 text-white rounded-xl transition-all"
                >
                  <RefreshCw className="w-5 h-5" />
                  <span>새로고침</span>
                </button>
                <button
                  onClick={this.handleReset}
                  className="flex-1 flex items-center justify-center space-x-2 px-6 py-3 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
                >
                  <Home className="w-5 h-5" />
                  <span>홈으로</span>
                </button>
              </div>
            </div>

            {/* Help Text */}
            <p className="text-center text-white/40 text-sm mt-6">
              문제가 계속되면 브라우저 캐시를 삭제하거나<br />
              관리자에게 문의해주세요.
            </p>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
