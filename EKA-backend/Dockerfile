# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file to the container
COPY target/EKA-backend-0.0.1-SNAPSHOT.jar /app/EKA-backend-0.0.1-SNAPSHOT.jar

# Expose port 8080
EXPOSE 8095

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/EKA-backend-0.0.1-SNAPSHOT.jar"]
