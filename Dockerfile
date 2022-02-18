FROM openjdk:11-jre-slim
ENV spring_profiles_active=docker
ARG APP_FILE
COPY ${APP_FILE} /app.jar
CMD ["java","-jar","/app.jar"]