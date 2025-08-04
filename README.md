# Spring Boot Academy: Actuator and Transactions Lab Series

Welcome to the comprehensive Spring Boot Academy lab series focused on **Spring Boot Actuator** and **Spring Transactions**. This series provides progressive, hands-on learning experiences designed to take you from basic concepts to advanced implementation patterns.

## 🎯 Series Overview

This lab series is structured to provide a complete learning path for Spring Boot developers who want to master production-ready monitoring, management, and transaction capabilities. Each lab builds upon the previous one, ensuring a solid foundation before moving to more complex concepts.

## 📚 Lab Series Structure

### Spring Actuator Labs (Labs 1-3)

#### Lab 1: Basic Actuator Endpoints
- **Difficulty**: Easy
- **Focus**: Enabling and exploring basic actuator endpoints
- **Key Concepts**: `/health`, `/info`, `/actuator`, endpoint exposure
- **Outcome**: Understand fundamental actuator capabilities

#### Lab 2: Custom Health Indicators
- **Difficulty**: Intermediate
- **Focus**: Creating custom health indicators for application-specific monitoring
- **Key Concepts**: `HealthIndicator` interface, custom health checks, external service monitoring
- **Outcome**: Build application-specific health monitoring

#### Lab 3: Custom Actuator Endpoints
- **Difficulty**: Intermediate
- **Focus**: Creating custom read-only and writable endpoints
- **Key Concepts**: `@Endpoint`, `@ReadOperation`, `@WriteOperation`, endpoint security
- **Outcome**: Implement custom management and monitoring endpoints

### Spring Transactions Labs (Labs 4-5)

#### Lab 4: Basic Transactions
- **Difficulty**: Easy
- **Focus**: Introduction to Spring transaction management
- **Key Concepts**: `@Transactional`, ACID properties, transaction rollback
- **Outcome**: Implement basic transaction scenarios

#### Lab 5: Advanced Transactions
- **Difficulty**: Advanced
- **Focus**: Complex transaction patterns and propagation
- **Key Concepts**: Transaction propagation, isolation levels, nested transactions
- **Outcome**: Handle complex transaction scenarios

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Gradle 7.0 or higher
- Basic understanding of Spring Boot
- Familiarity with REST APIs and databases

### Quick Start
1. Clone or download this repository
2. Navigate to any lab directory (e.g., `lab1-basic-actuators-and-endpoints`)
3. Run the application: `./gradlew bootRun`
4. Follow the lab instructions in the `README.md` file

## 📋 Lab Features

Each lab includes:

### ✅ Complete Project Structure
- Ready-to-run Spring Boot applications
- Proper package organization
- Gradle build configuration
- Database setup and configuration

### ✅ Step-by-Step Instructions
- Clear, numbered instructions
- Code snippets with explanations
- File paths and naming conventions
- Command-line examples

### ✅ Verification Steps
- Expected output examples
- Testing procedures
- Database verification steps
- Health check validations

### ✅ Learning Components
- **Learning Objectives**: Clear goals for each lab
- **Scenario**: Real-world context for the lab
- **Reflection Questions**: Deep thinking prompts
- **Key Concepts**: Important terms and concepts covered
- **Resources**: Links to official documentation and articles

### ✅ Progressive Difficulty
- **Easy**: Basic concepts and simple implementations
- **Intermediate**: More complex scenarios and custom implementations
- **Advanced**: Sophisticated patterns and production considerations

## 🎓 Learning Path

### Beginner Path
1. Start with **Lab 1** to understand basic actuator concepts
2. Move to **Lab 4** for basic transaction management
3. Practice with the provided examples and exercises

### Intermediate Path
1. Complete **Lab 2** for custom health indicators
2. Implement **Lab 3** for custom endpoints
3. Experiment with different configurations and scenarios

### Advanced Path
1. Master **Lab 5** for advanced transaction concepts
2. Combine actuator and transaction concepts
3. Implement production-ready monitoring and transaction patterns

## 🔧 Technology Stack

- **Spring Boot 3.2.0**: Latest stable version
- **Spring Data JPA**: Database access and ORM
- **Spring Boot Actuator**: Production monitoring and management
- **H2 Database**: In-memory database for development
- **Gradle**: Build tool and dependency management
- **Java 17**: Modern Java features and syntax

## 📖 Key Learning Outcomes

After completing this series, you will be able to:

### Spring Actuator
- ✅ Enable and configure Spring Boot Actuator
- ✅ Access and interpret standard actuator endpoints
- ✅ Create custom health indicators for application monitoring
- ✅ Implement custom read-only and writable endpoints
- ✅ Configure endpoint security and exposure
- ✅ Monitor application health and performance
- ✅ Integrate with monitoring and alerting systems

### Spring Transactions
- ✅ Understand ACID properties and transaction concepts
- ✅ Implement basic transaction management with `@Transactional`
- ✅ Handle transaction rollbacks and exceptions
- ✅ Use different transaction propagation behaviors
- ✅ Configure transaction isolation levels
- ✅ Implement complex transaction scenarios
- ✅ Debug and troubleshoot transaction issues

## 🛠️ Common Use Cases

### Production Monitoring
- Application health monitoring
- Performance metrics collection
- Custom business metrics
- Runtime configuration management
- Application state inspection

### Financial Applications
- Money transfer operations
- Account balance management
- Transaction logging and auditing
- Rollback mechanisms for failed operations

### E-commerce Systems
- Inventory management
- Order processing workflows
- Payment processing
- Notification systems
- Partial failure handling

## 🔍 Troubleshooting

### Common Issues
1. **Port conflicts**: Change `server.port` in `application.properties`
2. **Database connection**: Ensure H2 console is enabled
3. **Actuator endpoints not visible**: Check `management.endpoints.web.exposure.include`
4. **Transaction rollbacks**: Verify exception types and rollback rules

### Debug Tips
- Enable SQL logging: `spring.jpa.show-sql=true`
- Use H2 console for database inspection
- Check actuator health endpoint for system status
- Monitor application logs for transaction details

## 📚 Additional Resources

### Official Documentation
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Spring Transaction Management](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction)
- [Spring Boot Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.jpa-and-spring-data)

### Community Resources
- [Baeldung Spring Boot Tutorials](https://www.baeldung.com/spring-boot)
- [Spring Boot Guides](https://spring.io/guides)
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

## 🤝 Contributing

This lab series is designed to be educational and practical. If you find issues or have suggestions for improvements:

1. Check the existing issues
2. Create detailed bug reports
3. Suggest new lab topics or improvements
4. Share your learning experiences

## 📄 License

This educational content is provided for learning purposes. Feel free to use, modify, and distribute for educational use.

## 🎯 Next Steps

After completing this series, consider exploring:

- **Distributed Transactions**: Saga patterns, event sourcing
- **Microservices Monitoring**: Distributed tracing, centralized logging
- **Performance Optimization**: Connection pooling, caching strategies
- **Security**: Authentication, authorization, secure endpoints
- **Cloud Deployment**: Containerization, cloud-native patterns

---

**Happy Learning! 🚀**

Start with Lab 1 and progress through the series to build a solid foundation in Spring Boot Actuator and Transaction Management. 