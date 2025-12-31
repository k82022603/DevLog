import React from 'react';

const Logo = ({ size = 'md', className = '' }) => {
    const sizes = {
        sm: 'w-8 h-8',
        md: 'w-10 h-10',
        lg: 'w-12 h-12',
        xl: 'w-16 h-16',
    };

    return (
        <div className={`${sizes[size]} ${className} relative group`}>
            <svg
                viewBox="0 0 100 100"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
                className="w-full h-full transition-transform duration-300 group-hover:scale-110"
            >
                <defs>
                    <linearGradient id="logoGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                        <stop offset="0%" stopColor="#a855f7" />
                        <stop offset="100%" stopColor="#3b82f6" />
                    </linearGradient>
                </defs>

                {/* Code bracket left */}
                <path
                    d="M 35 20 L 25 20 Q 15 20 15 30 L 15 45 Q 15 50 10 50 Q 15 50 15 55 L 15 70 Q 15 80 25 80 L 35 80"
                    stroke="url(#logoGradient)"
                    strokeWidth="4"
                    strokeLinecap="round"
                    fill="none"
                    className="animate-pulse-slow"
                />

                {/* Code bracket right */}
                <path
                    d="M 65 20 L 75 20 Q 85 20 85 30 L 85 45 Q 85 50 90 50 Q 85 50 85 55 L 85 70 Q 85 80 75 80 L 65 80"
                    stroke="url(#logoGradient)"
                    strokeWidth="4"
                    strokeLinecap="round"
                    fill="none"
                    className="animate-pulse-slow"
                />

                {/* Pen nib in center */}
                <path
                    d="M 50 30 L 45 50 L 50 70 L 55 50 Z"
                    fill="url(#logoGradient)"
                    className="transition-all duration-300 group-hover:scale-110"
                />

                {/* Pen tip */}
                <circle
                    cx="50"
                    cy="72"
                    r="3"
                    fill="#3b82f6"
                    className="animate-bounce-slow"
                />
            </svg>
        </div>
    );
};

export default Logo;
