#!/bin/bash
# Daily PostgreSQL backup script
# Add to crontab: 0 2 * * * /opt/studymate/deploy/scripts/backup-db.sh

BACKUP_DIR="/opt/studymate/backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
KEEP_DAYS=7

mkdir -p "$BACKUP_DIR"

echo "[$(date)] Starting PostgreSQL backup..."

docker exec studymate-postgres pg_dump \
    -U studymate \
    -d studymate \
    --format=custom \
    --compress=9 \
    > "$BACKUP_DIR/studymate_${TIMESTAMP}.dump"

if [ $? -eq 0 ]; then
    echo "[$(date)] Backup created: studymate_${TIMESTAMP}.dump"
    SIZE=$(du -h "$BACKUP_DIR/studymate_${TIMESTAMP}.dump" | cut -f1)
    echo "[$(date)] Backup size: $SIZE"
else
    echo "[$(date)] ERROR: Backup failed!"
    exit 1
fi

# MongoDB backup
echo "[$(date)] Starting MongoDB backup..."
docker exec studymate-mongo mongodump \
    --db studymate \
    --archive="/tmp/mongo_${TIMESTAMP}.gz" \
    --gzip 2>/dev/null

docker cp "studymate-mongo:/tmp/mongo_${TIMESTAMP}.gz" \
    "$BACKUP_DIR/mongo_${TIMESTAMP}.gz" 2>/dev/null

# Cleanup old backups
echo "[$(date)] Cleaning backups older than ${KEEP_DAYS} days..."
find "$BACKUP_DIR" -name "*.dump" -mtime +$KEEP_DAYS -delete
find "$BACKUP_DIR" -name "*.gz" -mtime +$KEEP_DAYS -delete

echo "[$(date)] Backup complete."
