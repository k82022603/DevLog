import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  Home,
  FileText,
  Folder,
  BarChart3,
  Settings,
  Menu,
  X,
  User,
  ChevronRight,
} from 'lucide-react';
import Logo from './Logo';
import GlobalSearch from './GlobalSearch';
import QuickActions from './QuickActions';

const Layout = ({ children }) => {
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const navItems = [
    { path: '/', label: '대시보드', icon: Home },
    { path: '/logs', label: '로그', icon: FileText },
    { path: '/projects', label: '프로젝트', icon: Folder },
    { path: '/statistics', label: '통계', icon: BarChart3 },
    { path: '/settings', label: '설정', icon: Settings },
  ];

  const isActive = (path) => {
    if (path === '/') {
      return location.pathname === '/';
    }
    return location.pathname.startsWith(path);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900">
      {/* Header */}
      <header className="glass sticky top-0 z-50 backdrop-blur-xl border-b border-white/10">
        <div className="px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            {/* Logo */}
            <Link to="/" className="flex items-center space-x-3 group">
              <Logo size="sm" className="transition-transform duration-300 group-hover:rotate-12" />
              <div className="flex flex-col">
                <h1 className="text-xl font-display font-bold text-white tracking-tight">
                  DevLog
                </h1>
                <p className="text-xs text-white/50 hidden sm:block">개발자 로그 시스템</p>
              </div>
            </Link>

            {/* Desktop Navigation */}
            <nav className="hidden lg:flex items-center space-x-1">
              {navItems.map((item) => {
                const Icon = item.icon;
                const active = isActive(item.path);
                return (
                  <Link
                    key={item.path}
                    to={item.path}
                    className={`relative inline-flex items-center px-4 py-2 rounded-xl text-sm font-medium transition-all duration-300 group ${
                      active
                        ? 'bg-gradient-to-r from-purple-500 to-blue-500 text-white shadow-glow'
                        : 'text-white/70 hover:text-white hover:bg-white/10'
                    }`}
                  >
                    <Icon className={`w-4 h-4 mr-2 transition-transform ${active ? '' : 'group-hover:scale-110'}`} />
                    {item.label}
                  </Link>
                );
              })}
            </nav>

            {/* User Profile (Desktop) */}
            <div className="hidden lg:flex items-center space-x-3">
              <GlobalSearch />
              <button className="flex items-center space-x-2 px-3 py-2 rounded-xl bg-white/5 hover:bg-white/10 transition-all">
                <div className="w-8 h-8 rounded-full bg-gradient-to-br from-purple-500 to-blue-500 flex items-center justify-center">
                  <User className="w-4 h-4 text-white" />
                </div>
                <span className="text-sm font-medium text-white">개발자</span>
              </button>
            </div>

            {/* Mobile Menu Button */}
            <button
              onClick={() => setSidebarOpen(!sidebarOpen)}
              className="lg:hidden p-2 rounded-xl text-white/70 hover:text-white hover:bg-white/10 transition-all"
            >
              {sidebarOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>
        </div>
      </header>

      {/* Mobile Sidebar Overlay */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black/50 backdrop-blur-sm z-40 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        ></div>
      )}

      {/* Mobile Sidebar */}
      <aside
        className={`fixed top-16 left-0 bottom-0 w-72 glass backdrop-blur-xl border-r border-white/10 z-40 transform transition-transform duration-300 lg:hidden ${
          sidebarOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="flex flex-col h-full">
          {/* Navigation */}
          <nav className="flex-1 px-4 py-6 space-y-2 overflow-y-auto">
            {navItems.map((item) => {
              const Icon = item.icon;
              const active = isActive(item.path);
              return (
                <Link
                  key={item.path}
                  to={item.path}
                  onClick={() => setSidebarOpen(false)}
                  className={`flex items-center justify-between px-4 py-3 rounded-xl text-sm font-medium transition-all ${
                    active
                      ? 'bg-gradient-to-r from-purple-500 to-blue-500 text-white shadow-glow'
                      : 'text-white/70 hover:text-white hover:bg-white/10'
                  }`}
                >
                  <div className="flex items-center space-x-3">
                    <Icon className="w-5 h-5" />
                    <span>{item.label}</span>
                  </div>
                  {active && <ChevronRight className="w-4 h-4" />}
                </Link>
              );
            })}
          </nav>

          {/* User Profile (Mobile) */}
          <div className="p-4 border-t border-white/10">
            <button className="w-full flex items-center space-x-3 px-4 py-3 rounded-xl bg-white/5 hover:bg-white/10 transition-all">
              <div className="w-10 h-10 rounded-full bg-gradient-to-br from-purple-500 to-blue-500 flex items-center justify-center">
                <User className="w-5 h-5 text-white" />
              </div>
              <div className="flex-1 text-left">
                <p className="text-sm font-medium text-white">개발자</p>
                <p className="text-xs text-white/50">developer@devlog.com</p>
              </div>
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="px-4 sm:px-6 lg:px-8 py-8 max-w-7xl mx-auto">
        {children}
      </main>

      {/* Decorative gradient orbs */}
      <div className="fixed top-0 left-0 w-full h-full pointer-events-none overflow-hidden -z-10">
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-purple-500/20 rounded-full blur-3xl animate-pulse"></div>
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-blue-500/20 rounded-full blur-3xl animate-pulse" style={{ animationDelay: '2s' }}></div>
      </div>

      {/* Quick Actions */}
      <QuickActions />
    </div>
  );
};

export default Layout;
