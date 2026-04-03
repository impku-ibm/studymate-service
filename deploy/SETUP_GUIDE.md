# StudyMate — Oracle Cloud Free Tier Deployment Guide

## Architecture
```
Internet → Nginx (80/443) → Spring Boot (8080)
                           → React SPA (static)
                           → PostgreSQL (5432, internal)
                           → Redis (6379, internal)
                           → MongoDB (27017, internal)
```

## Folder Structure on Server
```
/opt/studymate/
├── deploy/
│   ├── docker-compose.prod.yml
│   ├── Dockerfile.prod
│   ├── .env                    ← your secrets (not in git)
│   ├── nginx/
│   │   ├── nginx.conf
│   │   └── conf.d/default.conf
│   ├── frontend/
│   │   └── dist/               ← React build output
│   ├── scripts/
│   │   └── backup-db.sh
│   └── Jenkinsfile
├── backups/                    ← daily DB dumps
└── jenkins/                    ← Jenkins home
```

---

## Step 1: Create Oracle Cloud VM

1. Sign up at https://cloud.oracle.com (free tier)
2. Create a Compute Instance:
   - Shape: **VM.Standard.A1.Flex** (ARM) — 4 OCPUs, 24GB RAM (Always Free)
   - OS: **Ubuntu 22.04** (Canonical)
   - Boot volume: 100GB (free)
3. Download the SSH key during creation
4. Note the **Public IP** assigned

### Configure Networking
In Oracle Cloud Console → Networking → Virtual Cloud Networks → Security Lists:

Add Ingress Rules:
| Port | Protocol | Source    | Description |
|------|----------|----------|-------------|
| 22   | TCP      | 0.0.0.0/0 | SSH        |
| 80   | TCP      | 0.0.0.0/0 | HTTP       |
| 443  | TCP      | 0.0.0.0/0 | HTTPS      |

---

## Step 2: Initial Server Setup

SSH into your VM:
```bash
ssh -i ~/your-key.pem ubuntu@YOUR_PUBLIC_IP
```

Run these commands:
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker $USER

# Install Docker Compose
sudo apt install -y docker-compose-plugin

# Open firewall ports (Ubuntu iptables)
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 80 -j ACCEPT
sudo iptables -I INPUT 6 -m state --state NEW -p tcp --dport 443 -j ACCEPT
sudo netfilter-persistent save

# Create project directory
sudo mkdir -p /opt/studymate/{deploy,backups}
sudo chown -R $USER:$USER /opt/studymate

# Logout and login again for docker group
exit
```

---

## Step 3: Deploy Application

SSH back in, then:
```bash
cd /opt/studymate

# Clone backend repo
git clone https://github.com/impku-ibm/studymate-service.git backend-src

# Copy deploy files
cp -r backend-src/deploy/* deploy/
cp -r backend-src/deploy/.env.example deploy/.env

# Clone and build frontend
git clone https://github.com/impku-ibm/studymate-ui.git frontend-src
cd frontend-src
```

Install Node.js (ARM):
```bash
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs
```

Build frontend:
```bash
cd /opt/studymate/frontend-src
VITE_API_URL="" npm ci && npm run build
mkdir -p /opt/studymate/deploy/frontend
cp -r dist /opt/studymate/deploy/frontend/
```

---

## Step 4: Configure Environment

```bash
cd /opt/studymate/deploy
nano .env
```

Fill in:
```env
POSTGRES_USER=studymate
POSTGRES_PASSWORD=YourStrongPassword123!
JWT_SECRET=your-random-32-char-string-here-change-this
DOMAIN=yourdomain.com
EMAIL=admin@yourdomain.com
```

---

## Step 5: Copy Backend Source for Docker Build

The Dockerfile.prod builds from source inside Docker:
```bash
cd /opt/studymate/deploy
# Symlink or copy backend source so Docker can build it
cp -r /opt/studymate/backend-src/* ../
# The Dockerfile.prod context is ".." (parent of deploy/)
```

---

## Step 6: Start Everything

```bash
cd /opt/studymate/deploy
docker compose -f docker-compose.prod.yml up -d --build
```

Watch logs:
```bash
docker compose -f docker-compose.prod.yml logs -f backend
```

Check health:
```bash
curl http://localhost/actuator/health
```

---

## Step 7: SSL with Let's Encrypt

Point your domain's DNS A record to your Oracle VM's public IP, then:

```bash
# Get initial certificate
docker compose -f docker-compose.prod.yml run --rm certbot \
    certonly --webroot -w /var/www/certbot \
    -d yourdomain.com \
    --email admin@yourdomain.com \
    --agree-tos --no-eff-email

# Now enable HTTPS in nginx config
nano nginx/conf.d/default.conf
# 1. Uncomment the "return 301" line in the HTTP server block
# 2. Uncomment the entire HTTPS server block
# 3. Replace "yourdomain.com" with your actual domain

# Restart nginx
docker compose -f docker-compose.prod.yml restart nginx
```

SSL auto-renews via the certbot container.

---

## Step 8: Setup Backups

```bash
chmod +x /opt/studymate/deploy/scripts/backup-db.sh

# Test backup
/opt/studymate/deploy/scripts/backup-db.sh

# Add to crontab (daily at 2 AM)
crontab -e
# Add this line:
0 2 * * * /opt/studymate/deploy/scripts/backup-db.sh >> /opt/studymate/backups/backup.log 2>&1
```

---

## Step 9: Setup Jenkins (CI/CD)

```bash
# Run Jenkins in Docker
docker run -d \
    --name jenkins \
    --restart always \
    -p 8081:8080 \
    -v /opt/studymate/jenkins:/var/jenkins_home \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v /opt/studymate:/opt/studymate \
    jenkins/jenkins:lts

# Get initial admin password
docker logs jenkins 2>&1 | grep -A2 "initial"

# Access Jenkins at http://YOUR_IP:8081
# Install suggested plugins + Docker Pipeline plugin
```

### Create Pipeline Job
1. New Item → Pipeline
2. Pipeline → Definition: "Pipeline script from SCM"
3. SCM: Git → URL: `https://github.com/impku-ibm/studymate-service.git`
4. Script Path: `deploy/Jenkinsfile`
5. Build Triggers: GitHub hook trigger for GITScm polling

### GitHub Webhook
In your GitHub repo → Settings → Webhooks → Add:
- URL: `http://YOUR_IP:8081/github-webhook/`
- Content type: `application/json`
- Events: Just the push event

---

## Step 10: Auto-Recovery on Reboot

```bash
# Docker containers already have restart: always
# Enable Docker to start on boot
sudo systemctl enable docker

# Verify
sudo reboot
# After reboot, check:
docker ps
```

---

## Step 11: Monitoring (Free)

### UptimeRobot (free, 5-min checks)
1. Sign up at https://uptimerobot.com
2. Add monitor: HTTP(s) → `https://yourdomain.com/actuator/health`
3. Alert via email/Telegram on downtime

---

## Useful Commands

```bash
# View all containers
docker compose -f docker-compose.prod.yml ps

# View backend logs
docker compose -f docker-compose.prod.yml logs -f backend

# Restart backend only
docker compose -f docker-compose.prod.yml restart backend

# Full rebuild and deploy
docker compose -f docker-compose.prod.yml up -d --build

# Enter PostgreSQL
docker exec -it studymate-postgres psql -U studymate

# Enter MongoDB
docker exec -it studymate-mongo mongosh studymate

# Manual backup
/opt/studymate/deploy/scripts/backup-db.sh

# Restore PostgreSQL backup
docker exec -i studymate-postgres pg_restore -U studymate -d studymate < backups/studymate_YYYYMMDD.dump
```

---

## Cost Summary

| Resource          | Cost     |
|-------------------|----------|
| Oracle VM (ARM)   | $0/month |
| Oracle Boot Disk  | $0/month |
| Oracle Public IP  | $0/month |
| Cloudflare DNS    | $0/month |
| UptimeRobot       | $0/month |
| Let's Encrypt SSL | $0/month |
| **Total**         | **$0/month** |
