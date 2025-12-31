import React, { useState } from 'react';
import {
  User,
  Bell,
  Palette,
  Download,
  Upload,
  Trash2,
  Save,
  Moon,
  Sun,
  Monitor,
} from 'lucide-react';
import Toast from '../components/Toast';

const Settings = () => {
  const [toast, setToast] = useState(null);
  const [settings, setSettings] = useState({
    // Profile
    displayName: '개발자',
    email: 'developer@devlog.com',

    // Notifications
    emailNotifications: true,
    weeklyReport: true,

    // Appearance
    theme: 'dark', // light, dark, auto

    // Data
    autoSave: true,
  });

  const handleChange = (key, value) => {
    setSettings((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleSave = () => {
    // Save to localStorage
    localStorage.setItem('devlog-settings', JSON.stringify(settings));
    showToast('설정이 저장되었습니다.', 'success');
  };

  const handleExportData = () => {
    // Export all data as JSON
    const data = {
      logs: [], // TODO: Fetch from API
      projects: [], // TODO: Fetch from API
      settings: settings,
      exportDate: new Date().toISOString(),
    };

    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `devlog-backup-${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);

    showToast('데이터가 내보내기 되었습니다.', 'success');
  };

  const handleImportData = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (event) => {
      try {
        const data = JSON.parse(event.target.result);
        // TODO: Import data to API
        showToast('데이터가 가져오기 되었습니다.', 'success');
      } catch (err) {
        showToast('파일을 읽는데 실패했습니다.', 'error');
      }
    };
    reader.readAsText(file);
  };

  const handleClearData = () => {
    if (window.confirm('정말로 모든 데이터를 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
      // TODO: Clear all data
      localStorage.clear();
      showToast('모든 데이터가 삭제되었습니다.', 'success');
    }
  };

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
  };

  const closeToast = () => {
    setToast(null);
  };

  return (
    <div className="space-y-6 animate-slide-up">
      {/* Toast */}
      {toast && <Toast message={toast.message} type={toast.type} onClose={closeToast} />}

      {/* Header */}
      <div>
        <h2 className="text-4xl font-display font-bold text-white mb-2">설정</h2>
        <p className="text-white/60 text-lg">DevLog 애플리케이션을 사용자화하세요</p>
      </div>

      {/* Profile Settings */}
      <div className="glass rounded-2xl p-6">
        <div className="flex items-center space-x-3 mb-6">
          <User className="w-5 h-5 text-white/60" />
          <h3 className="text-xl font-semibold text-white">프로필</h3>
        </div>

        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-white mb-2">이름</label>
            <input
              type="text"
              value={settings.displayName}
              onChange={(e) => handleChange('displayName', e.target.value)}
              className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-white mb-2">이메일</label>
            <input
              type="email"
              value={settings.email}
              onChange={(e) => handleChange('email', e.target.value)}
              className="w-full px-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-white/40 focus:outline-none focus:border-purple-500/50 transition-all"
            />
          </div>
        </div>
      </div>

      {/* Notification Settings */}
      <div className="glass rounded-2xl p-6">
        <div className="flex items-center space-x-3 mb-6">
          <Bell className="w-5 h-5 text-white/60" />
          <h3 className="text-xl font-semibold text-white">알림</h3>
        </div>

        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-white font-medium">이메일 알림</p>
              <p className="text-sm text-white/60">중요한 업데이트를 이메일로 받기</p>
            </div>
            <button
              onClick={() => handleChange('emailNotifications', !settings.emailNotifications)}
              className={`relative w-12 h-6 rounded-full transition-colors ${
                settings.emailNotifications ? 'bg-gradient-to-r from-purple-500 to-blue-500' : 'bg-white/20'
              }`}
            >
              <span
                className={`absolute top-1 left-1 w-4 h-4 rounded-full bg-white transition-transform ${
                  settings.emailNotifications ? 'translate-x-6' : ''
                }`}
              ></span>
            </button>
          </div>

          <div className="flex items-center justify-between">
            <div>
              <p className="text-white font-medium">주간 리포트</p>
              <p className="text-sm text-white/60">매주 월요일 개발 활동 요약 받기</p>
            </div>
            <button
              onClick={() => handleChange('weeklyReport', !settings.weeklyReport)}
              className={`relative w-12 h-6 rounded-full transition-colors ${
                settings.weeklyReport ? 'bg-gradient-to-r from-purple-500 to-blue-500' : 'bg-white/20'
              }`}
            >
              <span
                className={`absolute top-1 left-1 w-4 h-4 rounded-full bg-white transition-transform ${
                  settings.weeklyReport ? 'translate-x-6' : ''
                }`}
              ></span>
            </button>
          </div>
        </div>
      </div>

      {/* Appearance Settings */}
      <div className="glass rounded-2xl p-6">
        <div className="flex items-center space-x-3 mb-6">
          <Palette className="w-5 h-5 text-white/60" />
          <h3 className="text-xl font-semibold text-white">테마</h3>
        </div>

        <div className="grid grid-cols-3 gap-4">
          <button
            onClick={() => handleChange('theme', 'light')}
            className={`flex flex-col items-center justify-center p-4 rounded-xl border-2 transition-all ${
              settings.theme === 'light'
                ? 'border-purple-500 bg-purple-500/10'
                : 'border-white/10 bg-white/5 hover:bg-white/10'
            }`}
          >
            <Sun className="w-6 h-6 text-white mb-2" />
            <span className="text-sm text-white">라이트</span>
          </button>

          <button
            onClick={() => handleChange('theme', 'dark')}
            className={`flex flex-col items-center justify-center p-4 rounded-xl border-2 transition-all ${
              settings.theme === 'dark'
                ? 'border-purple-500 bg-purple-500/10'
                : 'border-white/10 bg-white/5 hover:bg-white/10'
            }`}
          >
            <Moon className="w-6 h-6 text-white mb-2" />
            <span className="text-sm text-white">다크</span>
          </button>

          <button
            onClick={() => handleChange('theme', 'auto')}
            className={`flex flex-col items-center justify-center p-4 rounded-xl border-2 transition-all ${
              settings.theme === 'auto'
                ? 'border-purple-500 bg-purple-500/10'
                : 'border-white/10 bg-white/5 hover:bg-white/10'
            }`}
          >
            <Monitor className="w-6 h-6 text-white mb-2" />
            <span className="text-sm text-white">자동</span>
          </button>
        </div>
        <p className="mt-4 text-sm text-white/60">
          현재는 다크 모드만 지원합니다. 추후 라이트 모드가 추가될 예정입니다.
        </p>
      </div>

      {/* Data Management */}
      <div className="glass rounded-2xl p-6">
        <div className="flex items-center space-x-3 mb-6">
          <Download className="w-5 h-5 text-white/60" />
          <h3 className="text-xl font-semibold text-white">데이터 관리</h3>
        </div>

        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-white font-medium">자동 저장</p>
              <p className="text-sm text-white/60">작성 중인 내용을 자동으로 저장</p>
            </div>
            <button
              onClick={() => handleChange('autoSave', !settings.autoSave)}
              className={`relative w-12 h-6 rounded-full transition-colors ${
                settings.autoSave ? 'bg-gradient-to-r from-purple-500 to-blue-500' : 'bg-white/20'
              }`}
            >
              <span
                className={`absolute top-1 left-1 w-4 h-4 rounded-full bg-white transition-transform ${
                  settings.autoSave ? 'translate-x-6' : ''
                }`}
              ></span>
            </button>
          </div>

          <div className="pt-4 border-t border-white/10">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <button
                onClick={handleExportData}
                className="flex items-center justify-center space-x-2 px-4 py-3 bg-white/10 hover:bg-white/20 text-white rounded-xl transition-all"
              >
                <Download className="w-4 h-4" />
                <span>데이터 내보내기</span>
              </button>

              <label className="flex items-center justify-center space-x-2 px-4 py-3 bg-white/10 hover:bg-white/20 text-white rounded-xl transition-all cursor-pointer">
                <Upload className="w-4 h-4" />
                <span>데이터 가져오기</span>
                <input type="file" accept=".json" onChange={handleImportData} className="hidden" />
              </label>
            </div>
          </div>

          <div className="pt-4 border-t border-white/10">
            <button
              onClick={handleClearData}
              className="w-full flex items-center justify-center space-x-2 px-4 py-3 bg-red-500/20 hover:bg-red-500/30 text-red-400 rounded-xl transition-all"
            >
              <Trash2 className="w-4 h-4" />
              <span>모든 데이터 삭제</span>
            </button>
            <p className="mt-2 text-xs text-white/40 text-center">
              주의: 이 작업은 되돌릴 수 없습니다. 먼저 데이터를 내보내기 하세요.
            </p>
          </div>
        </div>
      </div>

      {/* Save Button */}
      <div className="flex items-center justify-end">
        <button
          onClick={handleSave}
          className="flex items-center space-x-2 px-6 py-3 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
        >
          <Save className="w-4 h-4" />
          <span>설정 저장</span>
        </button>
      </div>

      {/* App Info */}
      <div className="glass rounded-2xl p-6">
        <div className="text-center space-y-2">
          <h3 className="text-2xl font-display font-bold text-white">DevLog</h3>
          <p className="text-white/60">개발자를 위한 일일 로그 시스템</p>
          <p className="text-sm text-white/40">Version 1.0.0</p>
          <div className="flex items-center justify-center space-x-4 mt-4">
            <a href="#" className="text-sm text-purple-400 hover:text-purple-300 transition-colors">
              도움말
            </a>
            <span className="text-white/20">•</span>
            <a href="#" className="text-sm text-purple-400 hover:text-purple-300 transition-colors">
              피드백
            </a>
            <span className="text-white/20">•</span>
            <a href="#" className="text-sm text-purple-400 hover:text-purple-300 transition-colors">
              GitHub
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Settings;
