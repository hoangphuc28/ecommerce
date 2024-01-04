# Use an official OpenJDK runtime as a parent image
FROM --platform=linux/amd64 openjdk:17-jdk-slim
# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/ecommerce-0.0.1-SNAPSHOT.jar ecommerce-0.0.1.jar

# Expose port 8080
EXPOSE 8080

# Run the application when the container launches
CMD ["java", "-jar", "ecommerce-0.0.1.jar"]
