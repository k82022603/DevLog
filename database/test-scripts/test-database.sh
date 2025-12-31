#!/bin/bash

# ============================================================
# DevLog Database Test Script
# ============================================================
# This script tests database connectivity and schema
# Usage: ./test-database.sh
# ============================================================

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Database connection parameters
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="devlog"
DB_USER="devlog"
CONTAINER_NAME="devlog-postgres"

# Test counters
TESTS_PASSED=0
TESTS_FAILED=0

# Function to print test result
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ PASS${NC}: $2"
        ((TESTS_PASSED++))
    else
        echo -e "${RED}✗ FAIL${NC}: $2"
        ((TESTS_FAILED++))
    fi
}

# Function to execute SQL
exec_sql() {
    docker exec $CONTAINER_NAME psql -U $DB_USER -d $DB_NAME -t -c "$1" 2>/dev/null
}

# ============================================================
# Start Testing
# ============================================================

echo "======================================"
echo "  DevLog Database Test Suite"
echo "======================================"
echo ""

# ============================================================
# Connection Tests
# ============================================================

echo -e "${YELLOW}[1] Database Connection Tests${NC}"
echo ""

# Test 1: Docker container is running
if docker ps | grep -q $CONTAINER_NAME; then
    print_result 0 "PostgreSQL container is running"
else
    print_result 1 "PostgreSQL container is NOT running"
fi

# Test 2: PostgreSQL is accepting connections
if docker exec $CONTAINER_NAME pg_isready -U $DB_USER > /dev/null 2>&1; then
    print_result 0 "PostgreSQL is accepting connections"
else
    print_result 1 "PostgreSQL is NOT accepting connections"
fi

# Test 3: Can connect to database
if docker exec $CONTAINER_NAME psql -U $DB_USER -d $DB_NAME -c "SELECT 1;" > /dev/null 2>&1; then
    print_result 0 "Can connect to database '$DB_NAME'"
else
    print_result 1 "Cannot connect to database '$DB_NAME'"
fi

# ============================================================
# Schema Tests
# ============================================================

echo ""
echo -e "${YELLOW}[2] Schema Tests${NC}"
echo ""

# Test 4: Check if projects table exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'projects';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "Table 'projects' exists"
else
    print_result 1 "Table 'projects' does NOT exist"
fi

# Test 5: Check if dev_logs table exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'dev_logs';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "Table 'dev_logs' exists"
else
    print_result 1 "Table 'dev_logs' does NOT exist"
fi

# Test 6: Check if tech_tags table exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'tech_tags';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "Table 'tech_tags' exists"
else
    print_result 1 "Table 'tech_tags' does NOT exist"
fi

# Test 7: Check if log_tech_tags table exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'log_tech_tags';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "Table 'log_tech_tags' exists"
else
    print_result 1 "Table 'log_tech_tags' does NOT exist"
fi

# Test 8: Check if project_stats table exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'project_stats';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "Table 'project_stats' exists"
else
    print_result 1 "Table 'project_stats' does NOT exist"
fi

# ============================================================
# Index Tests
# ============================================================

echo ""
echo -e "${YELLOW}[3] Index Tests${NC}"
echo ""

# Test 9: Check if indexes exist
result=$(exec_sql "SELECT COUNT(*) FROM pg_indexes WHERE tablename = 'dev_logs';")
if [ $(echo $result | tr -d ' ') -gt 0 ]; then
    print_result 0 "Indexes exist on 'dev_logs' table"
    echo "  Found $(echo $result | tr -d ' ') indexes"
else
    print_result 1 "No indexes found on 'dev_logs' table"
fi

# ============================================================
# View Tests
# ============================================================

echo ""
echo -e "${YELLOW}[4] View Tests${NC}"
echo ""

# Test 10: Check if v_project_summary view exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.views WHERE table_name = 'v_project_summary';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "View 'v_project_summary' exists"
else
    print_result 1 "View 'v_project_summary' does NOT exist"
fi

# Test 11: Check if v_log_details view exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.views WHERE table_name = 'v_log_details';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "View 'v_log_details' exists"
else
    print_result 1 "View 'v_log_details' does NOT exist"
fi

# Test 12: Check if v_tag_statistics view exists
result=$(exec_sql "SELECT COUNT(*) FROM information_schema.views WHERE table_name = 'v_tag_statistics';")
if [ $(echo $result | tr -d ' ') -eq 1 ]; then
    print_result 0 "View 'v_tag_statistics' exists"
else
    print_result 1 "View 'v_tag_statistics' does NOT exist"
fi

# ============================================================
# Trigger Tests
# ============================================================

echo ""
echo -e "${YELLOW}[5] Trigger Tests${NC}"
echo ""

# Test 13: Check if triggers exist
result=$(exec_sql "SELECT COUNT(*) FROM pg_trigger WHERE tgname LIKE 'update_%_updated_at';")
if [ $(echo $result | tr -d ' ') -gt 0 ]; then
    print_result 0 "updated_at triggers exist"
    echo "  Found $(echo $result | tr -d ' ') triggers"
else
    print_result 1 "No updated_at triggers found"
fi

# ============================================================
# Data Tests
# ============================================================

echo ""
echo -e "${YELLOW}[6] Data Tests${NC}"
echo ""

# Test 14: Count projects
result=$(exec_sql "SELECT COUNT(*) FROM projects;")
projects_count=$(echo $result | tr -d ' ')
echo -e "  Projects count: ${YELLOW}$projects_count${NC}"
print_result 0 "Projects table is accessible"

# Test 15: Count dev_logs
result=$(exec_sql "SELECT COUNT(*) FROM dev_logs;")
logs_count=$(echo $result | tr -d ' ')
echo -e "  Dev logs count: ${YELLOW}$logs_count${NC}"
print_result 0 "Dev logs table is accessible"

# Test 16: Count tech_tags
result=$(exec_sql "SELECT COUNT(*) FROM tech_tags;")
tags_count=$(echo $result | tr -d ' ')
echo -e "  Tech tags count: ${YELLOW}$tags_count${NC}"
print_result 0 "Tech tags table is accessible"

# ============================================================
# Performance Tests
# ============================================================

echo ""
echo -e "${YELLOW}[7] Performance Tests${NC}"
echo ""

# Test 17: Database size
db_size=$(exec_sql "SELECT pg_size_pretty(pg_database_size('$DB_NAME'));")
echo -e "  Database size: ${YELLOW}$db_size${NC}"
print_result 0 "Database size retrieved successfully"

# Test 18: Largest tables
echo "  Largest tables:"
docker exec $CONTAINER_NAME psql -U $DB_USER -d $DB_NAME -c "
SELECT
    schemaname || '.' || tablename as table,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC
LIMIT 3;
"

# Test 19: Active connections
active_conn=$(exec_sql "SELECT count(*) FROM pg_stat_activity WHERE datname = '$DB_NAME';")
echo -e "  Active connections: ${YELLOW}$active_conn${NC}"
print_result 0 "Active connections retrieved successfully"

# ============================================================
# Foreign Key Tests
# ============================================================

echo ""
echo -e "${YELLOW}[8] Foreign Key Tests${NC}"
echo ""

# Test 20: Check foreign key constraints
fk_count=$(exec_sql "SELECT COUNT(*) FROM information_schema.table_constraints WHERE constraint_type = 'FOREIGN KEY';")
echo -e "  Foreign key constraints: ${YELLOW}$fk_count${NC}"
if [ $(echo $fk_count | tr -d ' ') -gt 0 ]; then
    print_result 0 "Foreign key constraints exist"
else
    print_result 1 "No foreign key constraints found"
fi

# ============================================================
# Test Summary
# ============================================================

echo ""
echo "======================================"
echo "  Test Summary"
echo "======================================"
echo -e "${GREEN}Passed:${NC} $TESTS_PASSED"
echo -e "${RED}Failed:${NC} $TESTS_FAILED"
echo "======================================"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}All database tests passed!${NC}"
    exit 0
else
    echo -e "${RED}Some database tests failed!${NC}"
    exit 1
fi
