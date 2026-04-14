# Use official OpenJDK image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the app
RUN ./gradlew build -x test

# Expose port (Render uses dynamic PORT, but this is fine)
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "build/libs/task-api-0.0.1-SNAPSHOT.jar"]