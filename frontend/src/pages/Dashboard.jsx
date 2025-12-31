import React, { useState, useEffect } from 'react';
import { Activity, Clock, TrendingUp, Calendar, Code, Zap, Target } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';
import { StatCardSkeleton, CardSkeleton } from '../components/Skeleton';
import { statisticsApi, devLogApi } from '../services/api';
import { formatDate, formatRelativeTime } from '../utils/formatters';

const Dashboard = () => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [weeklyStats, setWeeklyStats] = useState(null);
  const [recentLogs, setRecentLogs] = useState([]);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch weekly statistics and recent logs in parallel
      const [weeklyResponse, logsResponse] = await Promise.all([
        statisticsApi.getCurrentWeek(),
        devLogApi.getAll({ limit: 5 })
      ]);

      setWeeklyStats(weeklyResponse.data);
      setRecentLogs(Array.isArray(logsResponse.data) ? logsResponse.data.slice(0, 5) : []);
    } catch (err) {
      console.error('Error fetching dashboard data:', err);
      setError('데이터를 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // Calculate goal achievement rate (based on work days vs 5 days target)
  const calculateGoalRate = () => {
    if (!weeklyStats || !weeklyStats.dailyCounts) return 0;
    const workDays = weeklyStats.dailyCounts.filter(d => d.count > 0).length;
    return Math.round((workDays / 5) * 100);
  };

  // Format work minutes to hours and minutes
  const formatWorkTime = (minutes) => {
    if (!minutes) return '0시간';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours === 0) return `${mins}분`;
    if (mins === 0) return `${hours}시간`;
    return `${hours}시간 ${mins}분`;
  };

  // Prepare chart data
  const getChartData = () => {
    if (!weeklyStats || !weeklyStats.dailyCounts) return [];

    const daysOfWeek = ['월', '화', '수', '목', '금', '토', '일'];

    return weeklyStats.dailyCounts.map((day, index) => ({
      name: daysOfWeek[index] || day.date.split('-')[2] + '일',
      hours: day.workMinutes ? (day.workMinutes / 60).toFixed(1) : 0,
      count: day.count || 0
    }));
  };

  const stats = [
    {
      label: '총 작업 시간',
      value: weeklyStats ? formatWorkTime(weeklyStats.totalWorkMinutes) : '0시간',
      icon: Clock,
      gradient: 'from-purple-500 to-pink-500',
      bgGradient: 'bg-gradient-to-br from-purple-500/20 to-pink-500/20',
      progress: weeklyStats ? Math.min((weeklyStats.totalWorkMinutes / 2400) * 100, 100) : 0, // 40 hours target
    },
    {
      label: '로그 개수',
      value: weeklyStats ? weeklyStats.totalLogs : '0',
      icon: Activity,
      gradient: 'from-blue-500 to-cyan-500',
      bgGradient: 'bg-gradient-to-br from-blue-500/20 to-cyan-500/20',
      progress: weeklyStats ? Math.min((weeklyStats.totalLogs / 10) * 100, 100) : 0, // 10 logs target
    },
    {
      label: '활성 프로젝트',
      value: weeklyStats ? weeklyStats.activeProjects : '0',
      icon: TrendingUp,
      gradient: 'from-green-500 to-emerald-500',
      bgGradient: 'bg-gradient-to-br from-green-500/20 to-emerald-500/20',
      progress: weeklyStats ? Math.min((weeklyStats.activeProjects / 3) * 100, 100) : 0, // 3 projects target
    },
    {
      label: '목표 달성률',
      value: `${calculateGoalRate()}%`,
      icon: Target,
      gradient: 'from-orange-500 to-red-500',
      bgGradient: 'bg-gradient-to-br from-orange-500/20 to-red-500/20',
      progress: calculateGoalRate(),
    },
  ];

  // Loading state
  if (loading) {
    return (
      <div className="space-y-8 animate-slide-up">
        {/* Stats Cards Skeleton */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <StatCardSkeleton />
          <StatCardSkeleton />
          <StatCardSkeleton />
          <StatCardSkeleton />
        </div>

        {/* Chart Skeleton */}
        <div className="glass rounded-2xl p-6">
          <div className="h-8 w-48 bg-white/10 rounded-lg mb-6 animate-pulse"></div>
          <div className="h-64 bg-white/5 rounded-xl animate-pulse"></div>
        </div>

        {/* Recent Logs Skeleton */}
        <div className="glass rounded-2xl p-6">
          <div className="h-8 w-32 bg-white/10 rounded-lg mb-6 animate-pulse"></div>
          <div className="space-y-4">
            <CardSkeleton />
            <CardSkeleton />
            <CardSkeleton />
          </div>
        </div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="space-y-8 animate-slide-up">
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="inline-flex p-4 rounded-full bg-red-500/10 mb-4">
              <Activity className="w-8 h-8 text-red-400" />
            </div>
            <p className="text-white/80 mb-4">{error}</p>
            <button
              onClick={fetchDashboardData}
              className="px-6 py-2 bg-gradient-to-r from-purple-500 to-blue-500 text-white rounded-xl hover:shadow-glow transition-all"
            >
              다시 시도
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-8 animate-slide-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-4xl font-display font-bold text-white mb-2">
            대시보드
          </h2>
          <p className="text-white/60 text-lg">개발 활동을 한눈에 확인하세요</p>
        </div>
        <div className="flex items-center space-x-2 px-4 py-2 glass rounded-full">
          <Calendar className="w-5 h-5 text-white/80" />
          <span className="text-white/90 font-medium">
            {new Date().toLocaleDateString('ko-KR', {
              year: 'numeric',
              month: 'long',
              day: 'numeric'
            })}
          </span>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, index) => {
          const Icon = stat.icon;
          return (
            <div
              key={stat.label}
              className="glass rounded-2xl p-6 card-hover group relative overflow-hidden"
              style={{ animationDelay: `${index * 100}ms` }}
            >
              {/* Background gradient */}
              <div className={`absolute inset-0 ${stat.bgGradient} opacity-50 group-hover:opacity-70 transition-opacity duration-300`}></div>

              {/* Content */}
              <div className="relative z-10">
                <div className="flex items-center justify-between mb-4">
                  <div className={`p-3 rounded-xl bg-gradient-to-br ${stat.gradient} shadow-glow`}>
                    <Icon className="w-6 h-6 text-white" />
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-medium text-white/60 mb-1">
                      {stat.label}
                    </p>
                    <p className="text-3xl font-display font-bold text-white">
                      {stat.value}
                    </p>
                  </div>
                </div>

                {/* Progress bar */}
                <div className="h-1 bg-white/10 rounded-full overflow-hidden">
                  <div
                    className={`h-full bg-gradient-to-r ${stat.gradient} rounded-full transition-all duration-1000`}
                    style={{ width: `${stat.progress}%` }}
                  ></div>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      {/* Weekly Activity Chart */}
      <div className="glass rounded-2xl p-6 card-hover">
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center space-x-3">
            <div className="p-2 rounded-lg bg-gradient-to-br from-blue-500 to-cyan-500">
              <Clock className="w-5 h-5 text-white" />
            </div>
            <h3 className="text-xl font-display font-semibold text-white">
              이번 주 일별 작업 시간
            </h3>
          </div>
          <div className="text-sm text-white/60">
            {weeklyStats && formatDate(weeklyStats.startDate)} ~ {weeklyStats && formatDate(weeklyStats.endDate)}
          </div>
        </div>

        <div className="h-64">
          {getChartData().length > 0 ? (
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={getChartData()}>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                <XAxis
                  dataKey="name"
                  stroke="rgba(255,255,255,0.6)"
                  style={{ fontSize: '12px' }}
                />
                <YAxis
                  stroke="rgba(255,255,255,0.6)"
                  style={{ fontSize: '12px' }}
                  label={{ value: '시간', angle: -90, position: 'insideLeft', fill: 'rgba(255,255,255,0.6)' }}
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: 'rgba(0, 0, 0, 0.8)',
                    border: '1px solid rgba(255, 255, 255, 0.2)',
                    borderRadius: '12px',
                    color: '#fff',
                  }}
                  itemStyle={{
                    color: '#fff',
                  }}
                  labelStyle={{
                    color: '#fff',
                  }}
                  formatter={(value, name) => {
                    if (name === 'hours') return [`${value}시간`, '작업 시간'];
                    return [value, name];
                  }}
                />
                <Bar
                  dataKey="hours"
                  fill="url(#colorGradient)"
                  radius={[8, 8, 0, 0]}
                />
                <defs>
                  <linearGradient id="colorGradient" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#3b82f6" stopOpacity={1} />
                    <stop offset="100%" stopColor="#06b6d4" stopOpacity={0.8} />
                  </linearGradient>
                </defs>
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <div className="flex items-center justify-center h-full">
              <p className="text-white/60">이번 주 활동 데이터가 없습니다.</p>
            </div>
          )}
        </div>
      </div>

      {/* Recent Activity Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Logs */}
        <div className="glass rounded-2xl p-6 card-hover">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center space-x-3">
              <div className="p-2 rounded-lg bg-gradient-to-br from-purple-500 to-blue-500">
                <Code className="w-5 h-5 text-white" />
              </div>
              <h3 className="text-xl font-display font-semibold text-white">
                최근 로그
              </h3>
            </div>
            <a href="/logs" className="text-sm text-white/60 hover:text-white transition-colors">
              전체 보기 →
            </a>
          </div>

          <div className="space-y-3">
            {recentLogs.length > 0 ? (
              recentLogs.map((log) => (
                <a
                  key={log.id}
                  href={`/logs/${log.id}`}
                  className="block p-4 rounded-xl bg-white/5 hover:bg-white/10 transition-all group"
                >
                  <div className="flex items-start justify-between mb-2">
                    <h4 className="font-semibold text-white group-hover:text-blue-400 transition-colors line-clamp-1">
                      {log.title}
                    </h4>
                    <span className="text-xs text-white/50 whitespace-nowrap ml-2">
                      {formatRelativeTime(log.logDate)}
                    </span>
                  </div>
                  {log.description && (
                    <p className="text-sm text-white/60 line-clamp-2 mb-2">
                      {log.description}
                    </p>
                  )}
                  <div className="flex items-center justify-between">
                    {log.projectName && (
                      <span className="text-xs px-2 py-1 rounded-lg bg-purple-500/20 text-purple-300">
                        {log.projectName}
                      </span>
                    )}
                    {log.workMinutes > 0 && (
                      <span className="text-xs text-white/50">
                        {formatWorkTime(log.workMinutes)}
                      </span>
                    )}
                  </div>
                </a>
              ))
            ) : (
              <div className="flex items-center justify-center py-12">
                <div className="text-center">
                  <div className="inline-flex p-4 rounded-full bg-white/5 mb-4">
                    <Code className="w-8 h-8 text-white/40" />
                  </div>
                  <p className="text-white/60">
                    아직 로그가 없습니다.<br />
                    첫 번째 로그를 작성해보세요!
                  </p>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Quick Stats */}
        <div className="glass rounded-2xl p-6 card-hover">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center space-x-3">
              <div className="p-2 rounded-lg bg-gradient-to-br from-green-500 to-emerald-500">
                <Zap className="w-5 h-5 text-white" />
              </div>
              <h3 className="text-xl font-display font-semibold text-white">
                활동 요약
              </h3>
            </div>
          </div>

          <div className="space-y-4">
            {[
              {
                label: '평균 작업 시간',
                value: weeklyStats ? formatWorkTime(weeklyStats.avgWorkMinutes) : '0시간',
                color: 'from-purple-500 to-pink-500'
              },
              {
                label: '활동 일수',
                value: weeklyStats && weeklyStats.dailyCounts
                  ? `${weeklyStats.dailyCounts.filter(d => d.count > 0).length}일`
                  : '0일',
                color: 'from-blue-500 to-cyan-500'
              },
              {
                label: '총 로그',
                value: weeklyStats ? `${weeklyStats.totalLogs}개` : '0개',
                color: 'from-green-500 to-emerald-500'
              },
            ].map((item) => (
              <div
                key={item.label}
                className="flex items-center justify-between p-4 rounded-xl bg-white/5 hover:bg-white/10 transition-colors"
              >
                <div className="flex items-center space-x-3">
                  <div className={`w-2 h-2 rounded-full bg-gradient-to-r ${item.color}`}></div>
                  <span className="text-white/80">{item.label}</span>
                </div>
                <span className="font-semibold text-white">{item.value}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
