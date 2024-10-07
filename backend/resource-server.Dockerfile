# Use a base image with Java and Maven installed
FROM maven:3-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the pom files and download the dependencies
COPY pom.xml .
COPY commons/pom.xml ./commons/
COPY authorization-server/pom.xml ./authorization-server/
COPY resource-server/pom.xml ./resource-server/
RUN mvn dependency:go-offline -B

# Copy the source code
COPY commons/src ./commons/src
COPY resource-server/src ./resource-server/src

# Build the application
RUN mvn package -pl resource-server -am -DskipTests

# Use a base image with Java installed
FROM eclipse-temurin:21-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/resource-server/target/*.jar resource-server.jar

# CORS
ENV CORS_ALLOWED_ORIGINS=http://localhost:4200
# Database connection
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=akimob
ENV DB_USERNAME=akimob
ENV DB_PASSWORD=your_password
# OAuth
ENV ISSUER_URI=http://authorization-server:8081
ENV AUTHORIZATION_URL=http://127.0.0.1:8081
ENV CLIENT_SECRET=akimob
ENV CLIENT_URL=http://localhost:8080
# Super admin user
ENV SUPER_ADMIN_USERNAME=superadmin
ENV SUPER_ADMIN_PASSWORD=superadmin
ENV SUPER_ADMIN_EMAIL=superadmin@email.com
# Logging
ENV LOG_MAX_HISTORY=30
ENV LOG_TOTAL_SIZE_CAP=10GB

# Expose the port on which the application will run
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "resource-server.jar"]
