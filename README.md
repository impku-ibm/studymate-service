# StudyMate Authentication Service

A secure Spring Boot authentication service with JWT tokens, Redis session management, and MongoDB Atlas integration.

## 🚀 Features

- **JWT Authentication** - Secure token-based authentication
- **Refresh Tokens** - Long-lived tokens for seamless user experience
- **Redis Session Management** - Fast token storage and blacklisting
- **MongoDB Atlas Integration** - Cloud-based user data storage
- **Global Exception Handling** - Comprehensive error management
- **Input Validation** - Request validation with custom error messages
- **Docker Support** - Containerized deployment
- **API Documentation** - Swagger/OpenAPI integration

## 🛠️ Tech Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.5.8** - Framework
- **Spring Security** - Authentication & authorization
- **JWT (JJWT)** - Token generation and validation
- **MongoDB Atlas** - Cloud database
- **Redis** - Session storage and caching
- **Docker** - Containerization
- **Gradle** - Build tool

## 📋 Prerequisites

- Java 21+
- Docker & Docker Compose
- MongoDB Atlas account
- Redis (via Docker)

## 🚀 Quick Start

### 1. Clone the repository
```bash
git clone https://github.com/impku-ibm/studymate-auth-service.git
cd studymate-auth-service
```

### 2. Environment Setup
```bash
cp .env.example .env
# Edit .env with your configuration
```

### 3. Run with Docker
```bash
docker-compose up --build
```

### 4. Access the application
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB Atlas connection string | - |
| `REDIS_HOST` | Redis host | `redis` |
| `REDIS_PORT` | Redis port | `6379` |
| `REDIS_DATABASE` | Redis database number | `1` |
| `JWT_SECRET` | JWT signing secret | - |
| `JWT_EXPIRATION` | Access token expiration (ms) | `3600000` |

### Sample .env file
```env
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/studymate
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_DATABASE=1
JWT_SECRET=your-super-secret-jwt-key-here-minimum-32-characters
JWT_EXPIRATION=3600000
```

## 📚 API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/signup` | Register new user |
| `POST` | `/auth/login` | User login |
| `POST` | `/auth/logout` | User logout |
| `POST` | `/auth/refresh-token` | Refresh access token |

### Protected Routes

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/user/me` | Get current user info |

## 🔐 Authentication Flow

1. **Signup**: Create user account
2. **Login**: Receive access & refresh tokens
3. **Access Protected Routes**: Use Bearer token
4. **Token Refresh**: Use refresh token when access token expires
5. **Logout**: Blacklist tokens

## 📝 API Usage Examples

### Signup
```bash
curl -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"schoolmodule@example.com","password":"password123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"schoolmodule@example.com","password":"password123"}'
```

### Access Protected Route
```bash
curl -X GET http://localhost:8080/user/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Logout
```bash
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Refresh Token
```bash
curl -X POST http://localhost:8080/auth/refresh-token \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"YOUR_REFRESH_TOKEN"}'
```

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client App    │───▶│  Spring Boot    │───▶│  MongoDB Atlas  │
│                 │    │   Auth Service  │    │   (User Data)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │     Redis       │
                       │ (Token Storage) │
                       └─────────────────┘
```

## 🔒 Security Features

- **Password Encryption** - BCrypt hashing
- **JWT Tokens** - Secure token generation
- **Token Blacklisting** - Logout invalidation
- **Input Validation** - Request sanitization
- **CORS Configuration** - Cross-origin security
- **Rate Limiting Ready** - Extensible for rate limiting

## 🐳 Docker Deployment

### Development
```bash
docker-compose up --build
```

### Production
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🧪 Testing

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Redis Connection
```bash
docker exec studymate-redis-1 redis-cli -n 1 KEYS "*"
```

## 📊 Monitoring

- **Application Logs**: `docker-compose logs app`
- **Redis Monitoring**: `docker exec studymate-redis-1 redis-cli monitor`
- **Health Endpoints**: `/actuator/health`

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the private License property of Pankaj - see the [LICENSE](LICENSE) file for details.

## 🆘 Troubleshooting

### Common Issues

**App not starting:**
- Check environment variables in `.env`
- Verify MongoDB Atlas connection string
- Ensure Redis is running

**Authentication failing:**
- Verify JWT secret is set
- Check token expiration settings
- Validate Redis connection

**Database connection issues:**
- Confirm MongoDB Atlas whitelist settings
- Check network connectivity
- Verify credentials

### Debug Commands

```bash
# Check running containers
docker-compose ps

# View application logs
docker-compose logs app --tail=50

# Check Redis keys
docker exec studymate-redis-1 redis-cli -n 1 KEYS "*"

# Test MongoDB connection
docker exec studymate-app-1 ping mongo
```

## 📞 Support

For support and questions:
- Create an issue in this repository
- Check existing documentation
- Review troubleshooting section

---

**Built with ❤️ using Spring Boot and modern Java technologies By Upadhyay brothers.**