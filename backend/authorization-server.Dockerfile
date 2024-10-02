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
COPY authorization-server/src ./authorization-server/src

# Build the application
RUN mvn package -pl authorization-server -am -DskipTests

# Use a base image with Java installed
FROM eclipse-temurin:21-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/authorization-server/target/*.jar authorization-server.jar

# Expose the port on which the application will run
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "authorization-server.jar"]
