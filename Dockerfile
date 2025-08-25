# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy Maven build files
COPY pom.xml .
COPY src ./src

# Build the project
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

# Copy the built jar to the container
COPY target/studentapp-0.0.1-SNAPSHOT.jar app.jar

# Expose port (matches Spring Boot default)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
