import axios from '../api/axios';

// Dev Logs API
export const devLogApi = {
  // Get all logs
  getAll: (params) => axios.get('/logs', { params }),
  
  // Get log by ID
  getById: (id) => axios.get(`/logs/${id}`),
  
  // Create new log
  create: (data) => axios.post('/logs', data),
  
  // Update log
  update: (id, data) => axios.put(`/logs/${id}`, data),
  
  // Delete log
  delete: (id) => axios.delete(`/logs/${id}`),
  
  // Get logs by project
  getByProject: (projectId) => axios.get(`/logs?projectId=${projectId}`),
  
  // Search logs
  search: (keyword) => axios.get(`/logs/search?q=${keyword}`),
};

// Projects API
export const projectApi = {
  // Get all projects
  getAll: () => axios.get('/projects'),
  
  // Get project by ID
  getById: (id) => axios.get(`/projects/${id}`),
  
  // Create new project
  create: (data) => axios.post('/projects', data),
  
  // Update project
  update: (id, data) => axios.put(`/projects/${id}`, data),
  
  // Delete project
  delete: (id) => axios.delete(`/projects/${id}`),
  
  // Get project statistics
  getStats: (id) => axios.get(`/projects/${id}/stats`),
};

// Tech Tags API
export const techTagApi = {
  // Get all tech tags
  getAll: () => axios.get('/tech-tags'),
  
  // Get popular tags
  getPopular: (limit = 10) => axios.get(`/tech-tags/popular?limit=${limit}`),
  
  // Get tags by category
  getByCategory: (category) => axios.get(`/tech-tags?category=${category}`),
};

// Statistics API
export const statisticsApi = {
  // Get dashboard statistics
  getDashboard: () => axios.get('/statistics/dashboard'),

  // Get weekly statistics
  getWeekly: (startDate) => {
    const params = startDate ? `?startDate=${startDate}` : '';
    return axios.get(`/statistics/weekly${params}`);
  },

  // Get current week statistics
  getCurrentWeek: () => axios.get('/statistics/weekly/current'),

  // Get last week statistics
  getLastWeek: () => axios.get('/statistics/weekly/last'),

  // Get monthly statistics
  getMonthly: (year, month) => {
    const params = new URLSearchParams();
    if (year) params.append('year', year);
    if (month) params.append('month', month);
    const query = params.toString() ? `?${params.toString()}` : '';
    return axios.get(`/statistics/monthly${query}`);
  },

  // Get current month statistics
  getCurrentMonth: () => axios.get('/statistics/monthly/current'),

  // Get last month statistics
  getLastMonth: () => axios.get('/statistics/monthly/last'),

  // Get project statistics
  getProject: (projectId) => axios.get(`/statistics/project/${projectId}`),

  // Get tech stack statistics
  getTechStack: () => axios.get('/statistics/tech-stack'),
};

export default {
  devLog: devLogApi,
  project: projectApi,
  techTag: techTagApi,
  statistics: statisticsApi,
};
