# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy source code and build the application
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

# Copy only the built JAR file from the build stage
COPY --from=build /app/target/video-summerizer-0.0.1-SNAPSHOT.jar video-summerizer.jar

# Expose application port
EXPOSE 8081

# Set environment variables dynamically
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
ENV SPRING_AI_MISTRAL_API_KEY=${MISTRAL_AI_API_KEY}
ENV SPRING_AI_STABILITYAI_API_KEY=${STABILITY_AI_API_KEY}
ENV HUGGING_FACE_API_TOKEN=${HUGGING_FACE_API_TOKEN}
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV MAIL_USERNAME=${MAIL_USERNAME}
ENV MAIL_PASSWORD=${MAIL_PASSWORD}

# JVM Optimization for Production
ENTRYPOINT ["java", "-jar", "video-summerizer.jar"]
