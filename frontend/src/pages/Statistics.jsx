import React, { useState, useEffect } from 'react';
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import {
  BarChart3,
  Calendar,
  Clock,
  Code,
  TrendingUp,
  Zap,
  Target,
} from 'lucide-react';
import { statisticsApi } from '../services/api';

const Statistics = () => {
  const [activeTab, setActiveTab] = useState('weekly'); // weekly, monthly
  const [loading, setLoading] = useState(true);
  const [weeklyStats, setWeeklyStats] = useState(null);
  const [monthlyStats, setMonthlyStats] = useState(null);
  const [techStackStats, setTechStackStats] = useState(null);

  const tabs = [
    { id: 'weekly', label: '주간', icon: Calendar },
    { id: 'monthly', label: '월간', icon: BarChart3 },
  ];

  useEffect(() => {
    fetchStatistics();
  }, []);

  const fetchStatistics = async () => {
    try {
      setLoading(true);

      const [weeklyRes, monthlyRes, techStackRes] = await Promise.all([
        statisticsApi.getCurrentWeek(),
        statisticsApi.getCurrentMonth(),
        statisticsApi.getTechStack(),
      ]);

      setWeeklyStats(weeklyRes.data);
      setMonthlyStats(monthlyRes.data);
      setTechStackStats(techStackRes.data);
    } catch (err) {
      console.error('Error fetching statistics:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatWorkTime = (minutes) => {
    if (!minutes) return '0시간';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours === 0) return `${mins}분`;
    if (mins === 0) return `${hours}시간`;
    return `${hours}시간 ${mins}분`;
  };

  // Chart colors
  const COLORS = [
    '#8B5CF6', // purple
    '#3B82F6', // blue
    '#10B981', // green
    '#F59E0B', // orange
    '#EF4444', // red
    '#EC4899', // pink
    '#06B6D4', // cyan
    '#8B5CF6', // indigo
  ];

  // Prepare weekly chart data
  const getWeeklyChartData = () => {
    if (!weeklyStats || !weeklyStats.dailyCounts) return [];

    const daysOfWeek = ['월', '화', '수', '목', '금', '토', '일'];

    return weeklyStats.dailyCounts.map((day, index) => ({
      name: daysOfWeek[index] || day.date.split('-')[2] + '일',
      hours: day.workMinutes ? (day.workMinutes / 60).toFixed(1) : 0,
      logs: day.count || 0,
    }));
  };

  // Prepare project distribution data
  const getProjectDistribution = () => {
    if (!weeklyStats || !weeklyStats.projectCounts) return [];

    return weeklyStats.projectCounts.map((project) => ({
      name: project.projectName,
      value: project.workMinutes || 0,
      logs: project.count || 0,
    }));
  };

  // Prepare tech stack data
  const getTechStackData = () => {
    if (!techStackStats || !techStackStats.popularTags) return [];

    return techStackStats.popularTags.slice(0, 10).map((tag) => ({
      name: tag.tagName,
      count: tag.usageCount || 0,
      percentage: tag.percentage || 0,
    }));
  };

  // Prepare monthly chart data
  const getMonthlyChartData = () => {
    if (!monthlyStats || !monthlyStats.weeklyCounts) return [];

    return monthlyStats.weeklyCounts.map((week) => ({
      name: `${week.weekNumber}주차`,
      hours: week.workMinutes ? (week.workMinutes / 60).toFixed(1) : 0,
      logs: week.count || 0,
    }));
  };

  // Get insights
  const getInsights = () => {
    const stats = activeTab === 'weekly' ? weeklyStats : monthlyStats;
    if (!stats) return {};

    // Most productive day
    const mostProductiveDay = stats.dailyCounts
      ? [...stats.dailyCounts].sort((a, b) => b.workMinutes - a.workMinutes)[0]
      : null;

    const daysOfWeek = ['월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일'];
    const mostProductiveDayName = mostProductiveDay
      ? daysOfWeek[stats.dailyCounts.indexOf(mostProductiveDay)]
      : '-';

    // Most used tech
    const mostUsedTech =
      techStackStats && techStackStats.popularTags && techStackStats.popularTags.length > 0
        ? techStackStats.popularTags[0].tagName
        : '-';

    return {
      mostProductiveDay: mostProductiveDayName,
      avgWorkTime: formatWorkTime(stats.avgWorkMinutes),
      mostUsedTech,
      totalLogs: stats.totalLogs || 0,
      totalWorkTime: formatWorkTime(stats.totalWorkMinutes),
      activeProjects: stats.activeProjects || 0,
    };
  };

  const insights = getInsights();

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <div className="inline-flex p-4 rounded-full bg-white/5 mb-4 animate-pulse">
            <BarChart3 className="w-8 h-8 text-white/40" />
          </div>
          <p className="text-white/60">통계를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6 animate-slide-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-4xl font-display font-bold text-white mb-2">통계</h2>
          <p className="text-white/60 text-lg">개발 활동을 분석하고 인사이트를 얻으세요</p>
        </div>
      </div>

      {/* Tabs */}
      <div className="flex items-center space-x-2 glass rounded-xl p-2">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          return (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`flex-1 flex items-center justify-center space-x-2 px-4 py-3 rounded-lg transition-all ${
                activeTab === tab.id
                  ? 'bg-gradient-to-r from-purple-500 to-blue-500 text-white shadow-glow'
                  : 'text-white/60 hover:text-white hover:bg-white/5'
              }`}
            >
              <Icon className="w-5 h-5" />
              <span className="font-medium">{tab.label}</span>
            </button>
          );
        })}
      </div>

      {/* Insights Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="glass rounded-2xl p-6">
          <div className="flex items-center space-x-3 mb-4">
            <div className="p-3 rounded-xl bg-gradient-to-br from-purple-500 to-pink-500 shadow-glow">
              <Target className="w-6 h-6 text-white" />
            </div>
            <div>
              <p className="text-sm text-white/60">가장 생산적인 요일</p>
              <p className="text-2xl font-bold text-white">{insights.mostProductiveDay}</p>
            </div>
          </div>
        </div>

        <div className="glass rounded-2xl p-6">
          <div className="flex items-center space-x-3 mb-4">
            <div className="p-3 rounded-xl bg-gradient-to-br from-blue-500 to-cyan-500 shadow-glow">
              <Clock className="w-6 h-6 text-white" />
            </div>
            <div>
              <p className="text-sm text-white/60">평균 작업 시간</p>
              <p className="text-2xl font-bold text-white">{insights.avgWorkTime}</p>
            </div>
          </div>
        </div>

        <div className="glass rounded-2xl p-6">
          <div className="flex items-center space-x-3 mb-4">
            <div className="p-3 rounded-xl bg-gradient-to-br from-green-500 to-emerald-500 shadow-glow">
              <Code className="w-6 h-6 text-white" />
            </div>
            <div>
              <p className="text-sm text-white/60">가장 많이 사용한 기술</p>
              <p className="text-2xl font-bold text-white">{insights.mostUsedTech}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Weekly Statistics */}
      {activeTab === 'weekly' && (
        <>
          {/* Daily Work Time Chart */}
          <div className="glass rounded-2xl p-6">
            <div className="flex items-center space-x-2 mb-6">
              <BarChart3 className="w-5 h-5 text-blue-400" />
              <h3 className="text-xl font-semibold text-white">일별 작업 시간</h3>
            </div>
            <div className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={getWeeklyChartData()}>
                  <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                  <XAxis
                    dataKey="name"
                    stroke="rgba(255,255,255,0.6)"
                    style={{ fontSize: '12px' }}
                  />
                  <YAxis
                    stroke="rgba(255,255,255,0.6)"
                    style={{ fontSize: '12px' }}
                    label={{
                      value: '시간',
                      angle: -90,
                      position: 'insideLeft',
                      fill: 'rgba(255,255,255,0.6)',
                    }}
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
                      if (name === 'logs') return [`${value}개`, '로그 수'];
                      return [value, name];
                    }}
                  />
                  <Bar dataKey="hours" fill="url(#colorGradient)" radius={[8, 8, 0, 0]} />
                  <defs>
                    <linearGradient id="colorGradient" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="0%" stopColor="#3b82f6" stopOpacity={1} />
                      <stop offset="100%" stopColor="#06b6d4" stopOpacity={0.8} />
                    </linearGradient>
                  </defs>
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Project Distribution */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="glass rounded-2xl p-6">
              <div className="flex items-center space-x-2 mb-6">
                <TrendingUp className="w-5 h-5 text-purple-400" />
                <h3 className="text-xl font-semibold text-white">프로젝트별 시간 분포</h3>
              </div>
              <div className="h-80">
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={getProjectDistribution()}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      label={({ cx, cy, midAngle, innerRadius, outerRadius, percent, name }) => {
                        const RADIAN = Math.PI / 180;
                        const radius = outerRadius + 25;
                        const x = cx + radius * Math.cos(-midAngle * RADIAN);
                        const y = cy + radius * Math.sin(-midAngle * RADIAN);
                        return (
                          <text
                            x={x}
                            y={y}
                            fill="white"
                            textAnchor={x > cx ? 'start' : 'end'}
                            dominantBaseline="central"
                            style={{ fontSize: '14px', fontWeight: '600', textShadow: '0 2px 4px rgba(0,0,0,0.8)' }}
                          >
                            {`${name} (${(percent * 100).toFixed(0)}%)`}
                          </text>
                        );
                      }}
                      outerRadius={100}
                      fill="#8884d8"
                      dataKey="value"
                    >
                      {getProjectDistribution().map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
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
                        if (name === 'value') return [formatWorkTime(value), '작업 시간'];
                        return [value, name];
                      }}
                    />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            </div>

            {/* Tech Stack Usage */}
            <div className="glass rounded-2xl p-6">
              <div className="flex items-center space-x-2 mb-6">
                <Code className="w-5 h-5 text-green-400" />
                <h3 className="text-xl font-semibold text-white">기술 스택 사용 빈도</h3>
              </div>
              <div className="h-80">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={getTechStackData()} layout="vertical">
                    <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                    <XAxis type="number" stroke="rgba(255,255,255,0.6)" />
                    <YAxis
                      dataKey="name"
                      type="category"
                      stroke="rgba(255,255,255,0.6)"
                      width={100}
                      style={{ fontSize: '12px' }}
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
                        if (name === 'count') return [`${value}회`, '사용 횟수'];
                        return [value, name];
                      }}
                    />
                    <Bar dataKey="count" fill="url(#techGradient)" radius={[0, 8, 8, 0]} />
                    <defs>
                      <linearGradient id="techGradient" x1="0" y1="0" x2="1" y2="0">
                        <stop offset="0%" stopColor="#10b981" stopOpacity={1} />
                        <stop offset="100%" stopColor="#059669" stopOpacity={0.8} />
                      </linearGradient>
                    </defs>
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
          </div>
        </>
      )}

      {/* Monthly Statistics */}
      {activeTab === 'monthly' && (
        <>
          {/* Weekly Trend Chart */}
          <div className="glass rounded-2xl p-6">
            <div className="flex items-center space-x-2 mb-6">
              <TrendingUp className="w-5 h-5 text-purple-400" />
              <h3 className="text-xl font-semibold text-white">주별 트렌드</h3>
            </div>
            <div className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={getMonthlyChartData()}>
                  <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                  <XAxis
                    dataKey="name"
                    stroke="rgba(255,255,255,0.6)"
                    style={{ fontSize: '12px' }}
                  />
                  <YAxis
                    stroke="rgba(255,255,255,0.6)"
                    style={{ fontSize: '12px' }}
                    label={{
                      value: '시간',
                      angle: -90,
                      position: 'insideLeft',
                      fill: 'rgba(255,255,255,0.6)',
                    }}
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
                      if (name === 'logs') return [`${value}개`, '로그 수'];
                      return [value, name];
                    }}
                  />
                  <Line
                    type="monotone"
                    dataKey="hours"
                    stroke="#8B5CF6"
                    strokeWidth={3}
                    dot={{ fill: '#8B5CF6', r: 6 }}
                    activeDot={{ r: 8 }}
                  />
                  <Line
                    type="monotone"
                    dataKey="logs"
                    stroke="#3B82F6"
                    strokeWidth={3}
                    dot={{ fill: '#3B82F6', r: 6 }}
                    activeDot={{ r: 8 }}
                  />
                  <Legend />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Summary Stats */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="glass rounded-2xl p-6">
              <div className="flex items-center space-x-3">
                <div className="p-3 rounded-xl bg-gradient-to-br from-purple-500 to-pink-500">
                  <Zap className="w-6 h-6 text-white" />
                </div>
                <div>
                  <p className="text-sm text-white/60">총 로그</p>
                  <p className="text-3xl font-bold text-white">{insights.totalLogs}개</p>
                </div>
              </div>
            </div>

            <div className="glass rounded-2xl p-6">
              <div className="flex items-center space-x-3">
                <div className="p-3 rounded-xl bg-gradient-to-br from-blue-500 to-cyan-500">
                  <Clock className="w-6 h-6 text-white" />
                </div>
                <div>
                  <p className="text-sm text-white/60">총 작업 시간</p>
                  <p className="text-3xl font-bold text-white">{insights.totalWorkTime}</p>
                </div>
              </div>
            </div>

            <div className="glass rounded-2xl p-6">
              <div className="flex items-center space-x-3">
                <div className="p-3 rounded-xl bg-gradient-to-br from-green-500 to-emerald-500">
                  <TrendingUp className="w-6 h-6 text-white" />
                </div>
                <div>
                  <p className="text-sm text-white/60">활성 프로젝트</p>
                  <p className="text-3xl font-bold text-white">{insights.activeProjects}개</p>
                </div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default Statistics;
