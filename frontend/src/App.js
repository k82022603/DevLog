import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import ErrorBoundary from './components/ErrorBoundary';
import Dashboard from './pages/Dashboard';
import LogList from './pages/LogList';
import LogDetail from './pages/LogDetail';
import LogForm from './pages/LogForm';
import ProjectList from './pages/ProjectList';
import ProjectForm from './pages/ProjectForm';
import Statistics from './pages/Statistics';
import Settings from './pages/Settings';

function App() {
  return (
    <ErrorBoundary>
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/logs" element={<LogList />} />
            <Route path="/logs/new" element={<LogForm />} />
            <Route path="/logs/:id" element={<LogDetail />} />
            <Route path="/logs/:id/edit" element={<LogForm />} />
            <Route path="/projects" element={<ProjectList />} />
            <Route path="/projects/new" element={<ProjectForm />} />
            <Route path="/projects/:id/edit" element={<ProjectForm />} />
            <Route path="/statistics" element={<Statistics />} />
            <Route path="/settings" element={<Settings />} />
          </Routes>
        </Layout>
      </Router>
    </ErrorBoundary>
  );
}

export default App;
