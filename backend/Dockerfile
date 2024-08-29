# Use a base image with Java and Maven installed
FROM maven:3-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a base image with Java installed
FROM eclipse-temurin:21-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/*.jar akimob.jar

# Set the environment variables for the database connection
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=akimob
ENV DB_USERNAME=akimob
ENV DB_PASSWORD=your_password

# Expose the port on which the application will run
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "akimob.jar"]
