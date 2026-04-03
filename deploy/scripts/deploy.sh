#!/bin/bash
# Quick deploy script — run from /opt/studymate/deploy/
set -e

echo "========================================="
echo "  StudyMate Production Deploy"
echo "========================================="

DEPLOY_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$DEPLOY_DIR"

# Check .env exists
if [ ! -f .env ]; then
    echo "ERROR: .env file not found. Copy .env.example to .env and fill in values."
    exit 1
fi

echo "[1/5] Pulling latest backend code..."
cd /opt/studymate/backend-src
git pull origin main
cp -r * /opt/studymate/
cd "$DEPLOY_DIR"

echo "[2/5] Pulling and building frontend..."
cd /opt/studymate/frontend-src
git pull origin main
VITE_API_URL="" npm ci --prefer-offline && npm run build
rm -rf "$DEPLOY_DIR/frontend/dist"
mkdir -p "$DEPLOY_DIR/frontend"
cp -r dist "$DEPLOY_DIR/frontend/"
cd "$DEPLOY_DIR"

echo "[3/5] Building Docker images..."
docker compose -f docker-compose.prod.yml build backend

echo "[4/5] Starting services..."
docker compose -f docker-compose.prod.yml up -d

echo "[5/5] Waiting for health check..."
sleep 15
for i in {1..10}; do
    if curl -sf http://localhost/actuator/health > /dev/null 2>&1; then
        echo "Health check PASSED"
        break
    fi
    echo "  Waiting... ($i/10)"
    sleep 10
done

echo ""
echo "========================================="
echo "  Deploy complete!"
echo "  Health: http://localhost/actuator/health"
echo "========================================="
docker compose -f docker-compose.prod.yml ps
