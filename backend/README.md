# FinSight AI - Java Backend

A comprehensive Spring Boot backend for the FinSight AI financial education platform, featuring adaptive quiz systems, personalized learning paths, and bank integration capabilities.

## Features

### Core Functionality
- **User Management**: Registration, authentication, profile management
- **Adaptive Quiz System**: Dynamic question generation based on knowledge level and goals
- **Goal-Based Learning**: Personalized educational content based on user objectives
- **Progress Tracking**: Comprehensive analytics and learning progress monitoring
- **Bank Integration Framework**: Ready for Canadian bank API integration

### Educational Features
- **Knowledge Level Assessment**: 4-tier system (Beginner, Intermediate, Advanced, Expert)
- **Dynamic Question Bank**: Questions tailored to user's current level and goals
- **Adaptive Learning**: Questions adjust based on quiz performance
- **Learning Recommendations**: Personalized resources based on quiz results
- **Progress Analytics**: Detailed performance tracking and improvement suggestions

### Technical Architecture
- **Spring Boot 3.2.0**: Modern Java framework with latest features
- **JPA/Hibernate**: Robust ORM with H2 database for development
- **JWT Authentication**: Secure token-based authentication
- **RESTful APIs**: Clean, documented API endpoints
- **OpenAPI Documentation**: Interactive API documentation with Swagger

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
```bash
cd backend
```

2. **Build the application**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access Points

- **API Base URL**: `http://localhost:8080/api`
- **Swagger Documentation**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:finsightdb`
  - Username: `sa`
  - Password: `password`

## API Endpoints

### User Management
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User authentication
- `GET /api/users/{userId}` - Get user profile
- `PUT /api/users/{userId}` - Update user profile
- `POST /api/users/{userId}/goals` - Add user goal
- `GET /api/users/{userId}/goals` - Get user goals
- `PUT /api/users/{userId}/knowledge-level` - Update knowledge level

### Quiz System
- `POST /api/quiz/generate/{userId}` - Generate personalized quiz
- `POST /api/quiz/submit/{userId}` - Submit quiz answers
- `GET /api/quiz/history/{userId}` - Get quiz history
- `GET /api/quiz/recommendations/{userId}` - Get learning recommendations
- `GET /api/quiz/stats/{userId}` - Get quiz statistics

## Database Schema

### Core Entities
- **User**: User profiles and authentication
- **FinancialGoal**: Available financial goals and categories
- **UserGoal**: User-specific goal associations
- **Question**: Quiz questions with difficulty levels
- **AnswerOption**: Multiple choice answers
- **QuizAttempt**: User quiz attempt records
- **QuizAnswer**: Individual answers within attempts
- **LearningProgress**: Learning module progress tracking

### Knowledge Levels
- **BEGINNER**: Just starting financial journey
- **INTERMEDIATE**: Basic understanding of financial concepts
- **ADVANCED**: Good financial literacy
- **EXPERT**: Deep financial knowledge and experience

### Goal Categories
- **SAVING**: Building emergency funds and savings
- **INVESTING**: Growing wealth through investments
- **DEBT_MANAGEMENT**: Managing and reducing debt
- **RETIREMENT**: Planning for retirement
- **HOME_OWNERSHIP**: Buying and managing property
- **EDUCATION**: Educational expenses and planning
- **BUDGETING**: Managing personal finances
- **CUSTOM**: User-defined financial goals

## Adaptive Quiz System

### Question Selection Algorithm
1. **Current Level Questions (60%)**: Questions matching user's current knowledge level
2. **Next Level Questions (30%)**: Slightly harder questions for learning
3. **Previous Level Questions (10%)**: Easier questions for reinforcement

### Knowledge Level Progression
- **90%+ Score**: Advance to next level
- **60-89% Score**: Maintain current level
- **<60% Score**: Move to previous level

### Personalization Features
- **Goal-Based Questions**: Questions relevant to user's selected financial goals
- **Difficulty Adaptation**: Questions adjust based on performance
- **Learning Recommendations**: Personalized resources based on incorrect answers

## Configuration

### Application Properties
Key configuration options in `application.yml`:

```yaml
finsight:
  quiz:
    default-question-count: 10
    max-question-count: 20
  learning:
    modules-per-level: 10
    completion-threshold: 80
  bank:
    integration:
      enabled: true
      api-timeout: 5000
```

### Database Configuration
- **Development**: H2 in-memory database
- **Production**: PostgreSQL (configurable)
- **Connection Pooling**: HikariCP
- **Migration**: Flyway (for production)

## Security

### JWT Authentication
- **Token Expiration**: 24 hours
- **Secret Key**: Configurable in application.yml
- **Password Hashing**: BCrypt encryption

### API Security
- **CORS Configuration**: Configurable for frontend integration
- **Rate Limiting**: Planned for production
- **Input Validation**: Comprehensive validation on all endpoints

## Development

### Code Structure
```
src/main/java/com/finsight/
  config/          # Configuration classes
  controller/      # REST API controllers
  model/          # JPA entities
  repository/     # Data access layer
  service/        # Business logic
  dto/           # Data transfer objects
  security/      # Security configuration
```

### Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn test -P integration

# Generate test coverage report
mvn jacoco:report
```

### Data Initialization
The application automatically initializes with:
- **9 Financial Goals**: Covering major financial categories
- **Sample Questions**: 20+ questions across different knowledge levels
- **Answer Options**: Complete multiple choice questions with explanations

## Bank Integration Framework

### Canadian Bank Support
The system is designed to integrate with major Canadian banks:
- **RBC**, **TD**, **Scotiabank**, **BMO**, **CIBC**
- **Tangerine**, **Simplii**, **EQ Bank**
- **Credit Unions** and **Digital Banks**

### Integration Features
- **Account Information**: Balance, transaction history
- **Product Recommendations**: Personalized based on user goals
- **Interest Rate Data**: Real-time rate information
- **API Framework**: Ready for bank API integration

## Production Deployment

### Environment Variables
```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/finsight
DATABASE_USERNAME=finsight
DATABASE_PASSWORD=secure_password
JWT_SECRET=your-secret-key
BANK_API_KEY=your-bank-api-key
```

### Docker Support
```bash
# Build Docker image
docker build -t finsight-backend .

# Run container
docker run -p 8080:8080 finsight-backend
```

### Monitoring
- **Health Checks**: `/api/health`
- **Metrics**: Spring Boot Actuator
- **Logging**: Structured logging with levels
- **Performance**: Request timing and database query monitoring

## Contributing

### Code Standards
- **Java 17**: Use modern Java features
- **Spring Boot**: Follow Spring conventions
- **Testing**: Minimum 80% test coverage
- **Documentation**: Javadoc for public APIs

### Git Workflow
1. Create feature branch from `develop`
2. Implement changes with tests
3. Submit pull request with description
4. Code review and merge

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For questions and support:
- **Documentation**: Check API documentation at `/swagger-ui.html`
- **Issues**: Create GitHub issue for bugs
- **Contact**: Development team via project channels

## Roadmap

### Phase 1: Core Backend (Current)
- [x] User management and authentication
- [x] Adaptive quiz system
- [x] Goal-based learning
- [x] Progress tracking

### Phase 2: Enhanced Features
- [ ] Bank API integration
- [ ] Advanced analytics
- [ ] Real-time notifications
- [ ] Mobile API optimization

### Phase 3: AI Integration
- [ ] Machine learning recommendations
- [ ] Predictive analytics
- [ ] Natural language processing
- [ ] Advanced personalization
