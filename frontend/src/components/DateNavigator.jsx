import React, { useState, useRef } from 'react';
import { createPortal } from 'react-dom';
import { ChevronLeft, ChevronRight, Calendar } from 'lucide-react';

const DateNavigator = ({ onDateSelect, selectedDate }) => {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [isOpen, setIsOpen] = useState(false);
  const [position, setPosition] = useState({ top: 0, left: 0 });
  const buttonRef = useRef(null);

  // Get days in month
  const getDaysInMonth = (date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();

    const days = [];

    // Add empty cells for days before the first day of month
    for (let i = 0; i < startingDayOfWeek; i++) {
      days.push(null);
    }

    // Add actual days
    for (let i = 1; i <= daysInMonth; i++) {
      days.push(new Date(year, month, i));
    }

    return days;
  };

  const days = getDaysInMonth(currentMonth);
  const monthYear = currentMonth.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
  });

  const handlePrevMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1, 1)
    );
  };

  const handleNextMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 1)
    );
  };

  const handleDateClick = (date) => {
    if (date) {
      onDateSelect(date);
      setIsOpen(false);
    }
  };

  const isToday = (date) => {
    if (!date) return false;
    const today = new Date();
    return (
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear()
    );
  };

  const isSelected = (date) => {
    if (!date || !selectedDate) return false;
    return (
      date.getDate() === selectedDate.getDate() &&
      date.getMonth() === selectedDate.getMonth() &&
      date.getFullYear() === selectedDate.getFullYear()
    );
  };

  // Calculate position when opening
  const handleToggle = () => {
    if (!isOpen && buttonRef.current) {
      const rect = buttonRef.current.getBoundingClientRect();
      setPosition({
        top: rect.bottom + 8,
        left: rect.left,
      });
    }
    setIsOpen(!isOpen);
  };

  return (
    <>
      {/* Toggle Button */}
      <button
        ref={buttonRef}
        onClick={handleToggle}
        className="flex items-center space-x-2 px-4 py-2 rounded-xl bg-white/5 hover:bg-white/10 border border-white/10 text-white transition-all"
      >
        <Calendar className="w-4 h-4" />
        <span className="text-sm">
          {selectedDate
            ? selectedDate.toLocaleDateString('ko-KR')
            : '날짜 선택'}
        </span>
      </button>

      {/* Calendar Dropdown - Rendered via Portal */}
      {isOpen && createPortal(
        <>
          {/* Backdrop */}
          <div
            className="fixed inset-0 z-[100] bg-black/20"
            onClick={() => setIsOpen(false)}
          ></div>

          {/* Calendar */}
          <div
            className="fixed z-[101] rounded-2xl p-4 shadow-2xl border border-white/20 min-w-[320px] bg-[rgba(20,20,40,0.98)] backdrop-blur-xl"
            style={{
              top: `${position.top}px`,
              left: `${position.left}px`,
            }}
          >
            {/* Header */}
            <div className="flex items-center justify-between mb-4">
              <button
                onClick={handlePrevMonth}
                className="p-2 rounded-lg hover:bg-white/10 transition-colors"
              >
                <ChevronLeft className="w-5 h-5 text-white" />
              </button>
              <h3 className="text-lg font-semibold text-white">{monthYear}</h3>
              <button
                onClick={handleNextMonth}
                className="p-2 rounded-lg hover:bg-white/10 transition-colors"
              >
                <ChevronRight className="w-5 h-5 text-white" />
              </button>
            </div>

            {/* Weekday Labels */}
            <div className="grid grid-cols-7 gap-1 mb-2">
              {['일', '월', '화', '수', '목', '금', '토'].map((day) => (
                <div
                  key={day}
                  className="text-center text-xs font-bold text-white py-2"
                  style={{ textShadow: '0 2px 4px rgba(0,0,0,0.5)' }}
                >
                  {day}
                </div>
              ))}
            </div>

            {/* Calendar Grid */}
            <div className="grid grid-cols-7 gap-1">
              {days.map((date, index) => (
                <button
                  key={index}
                  onClick={() => handleDateClick(date)}
                  disabled={!date}
                  className={`
                    aspect-square flex items-center justify-center rounded-lg text-sm font-semibold transition-all
                    ${
                      !date
                        ? 'cursor-default'
                        : isSelected(date)
                        ? 'bg-gradient-to-r from-purple-500 to-blue-500 text-white shadow-glow'
                        : isToday(date)
                        ? 'bg-white/20 text-white'
                        : 'text-white hover:bg-white/10'
                    }
                  `}
                  style={date ? { textShadow: '0 1px 2px rgba(0,0,0,0.3)' } : {}}
                >
                  {date ? date.getDate() : ''}
                </button>
              ))}
            </div>

            {/* Quick Actions */}
            <div className="mt-4 pt-4 border-t border-white/10 flex items-center justify-between">
              <button
                onClick={() => {
                  onDateSelect(new Date());
                  setIsOpen(false);
                }}
                className="text-sm text-purple-400 hover:text-purple-300 transition-colors"
              >
                오늘
              </button>
              <button
                onClick={() => {
                  onDateSelect(null);
                  setIsOpen(false);
                }}
                className="text-sm text-white/60 hover:text-white transition-colors"
              >
                초기화
              </button>
            </div>
          </div>
        </>,
        document.body
      )}
    </>
  );
};

export default DateNavigator;
