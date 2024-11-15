# Recipients Project v.1.0

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jcondotta_bank-account-management&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jcondotta_bank-account-management)  
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jcondotta_bank-account-management&metric=coverage)](https://sonarcloud.io/summary/new_code?id=jcondotta_bank-account-management)

This project is part of a microservice architecture responsible for managing bank accounts. It provides RESTful APIs for creating and managing bank account data and integrates seamlessly with other services in the ecosystem. The service prioritizes performance, scalability, and interoperability using AWS native services and modern development practices.

## Tech Stack

### Languages & Frameworks:

- **GraalVM 21.x (Java 21-based):** Modern and efficient JDK for building robust applications, with native image capabilities for optimized startup times and resource usage.
- **Micronaut 4.5.0+:** Framework used to build the microservice with lightweight, fast startup times and cloud-native capabilities.

### Infrastructure:

- **Amazon DynamoDB:** NoSQL database used to store bank account information.
- **Amazon SNS:** Simple Notification Service for publishing notifications to subscribed consumers when bank accounts are created.
- **AWS Lambda:** Serverless compute platform for running the microservice.
- **AWS API Gateway:** Exposes and manages the API endpoints for secure and scalable access.
- **Terraform:** Infrastructure as Code (IaC) tool used for managing AWS resources like DynamoDB, Lambda, and API Gateway.
- **LocalStack:** A fully functional local AWS cloud stack used for local testing of AWS services like DynamoDB and Lambda.

### Authentication:

- No authentication is implemented in this version, keeping the API accessible for internal use.

### CI/CD & Containerization:

- **GitHub Actions:** Automated pipeline for building, testing, and deploying the microservice.
- **Docker:** Used to containerize the application for local development and deployment.

### Testing:

- **JUnit 5:** Framework for unit and integration testing.
- **Mockito:** Framework for mocking dependencies in tests.
- **AssertJ:** Library for fluent assertion statements.
- **TestContainers:** Library for spinning up containers for integration testing with services like DynamoDB, SNS, and LocalStack.

### Documentation:

- **Swagger API:** API documentation and testing interface to explore the RESTful endpoints.

## Features

- **Bank Account Management:** APIs to create and manage bank accounts with robust data validation.
- **Native Performance:** Compiled using GraalVM Native Image for lightning-fast startup and optimized resource usage.
- **Notification System:** Publishes events to an SNS topic whenever a new bank account is created, enabling integration with downstream systems.
- **Infrastructure as Code:** AWS infrastructure is managed and deployed using Terraform.
- **Local Testing:** Comprehensive local testing using JUnit 5, Mockito, AssertJ, LocalStack, and TestContainers.
- **CI/CD Pipeline:** Continuous integration and deployment through GitHub Actions for seamless delivery.

## Project Architecture