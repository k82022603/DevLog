import React from 'react';

export const CardSkeleton = () => (
  <div className="glass rounded-2xl p-6 animate-pulse">
    <div className="flex items-start justify-between mb-4">
      <div className="flex items-start space-x-4 flex-1">
        <div className="w-12 h-12 rounded-xl bg-white/10"></div>
        <div className="flex-1 space-y-3">
          <div className="h-4 bg-white/10 rounded w-1/3"></div>
          <div className="h-6 bg-white/10 rounded w-3/4"></div>
          <div className="h-3 bg-white/10 rounded w-full"></div>
          <div className="h-3 bg-white/10 rounded w-2/3"></div>
        </div>
      </div>
    </div>
    <div className="flex items-center space-x-2">
      <div className="h-6 bg-white/10 rounded-full w-16"></div>
      <div className="h-6 bg-white/10 rounded-full w-16"></div>
      <div className="h-6 bg-white/10 rounded-full w-16"></div>
    </div>
  </div>
);

export const ProjectCardSkeleton = () => (
  <div className="glass rounded-2xl p-6 animate-pulse">
    <div className="flex items-start justify-between mb-4">
      <div className="w-14 h-14 rounded-xl bg-white/10"></div>
      <div className="h-6 bg-white/10 rounded-lg w-16"></div>
    </div>
    <div className="h-6 bg-white/10 rounded w-1/2 mb-2"></div>
    <div className="h-4 bg-white/10 rounded w-full mb-4"></div>
    <div className="space-y-2 mb-4">
      <div className="flex justify-between">
        <div className="h-3 bg-white/10 rounded w-12"></div>
        <div className="h-3 bg-white/10 rounded w-12"></div>
      </div>
      <div className="h-2 bg-white/10 rounded-full w-full"></div>
    </div>
    <div className="grid grid-cols-2 gap-3">
      <div className="h-16 bg-white/5 rounded-lg"></div>
      <div className="h-16 bg-white/5 rounded-lg"></div>
    </div>
  </div>
);

export const StatCardSkeleton = () => (
  <div className="glass rounded-2xl p-6 animate-pulse">
    <div className="flex items-center justify-between mb-4">
      <div className="w-12 h-12 rounded-xl bg-white/10"></div>
    </div>
    <div className="h-3 bg-white/10 rounded w-1/2 mb-2"></div>
    <div className="h-8 bg-white/10 rounded w-2/3"></div>
  </div>
);

export const ListSkeleton = ({ count = 3, type = 'card' }) => {
  const SkeletonComponent = type === 'project' ? ProjectCardSkeleton : CardSkeleton;

  return (
    <div className="space-y-4">
      {Array.from({ length: count }).map((_, index) => (
        <SkeletonComponent key={index} />
      ))}
    </div>
  );
};

export default {
  Card: CardSkeleton,
  ProjectCard: ProjectCardSkeleton,
  StatCard: StatCardSkeleton,
  List: ListSkeleton,
};
