# 1. Base Image: Use a Java 17 JRE for a smaller and more secure image
FROM eclipse-temurin:17-jre-jammy

# 2. Set metadata labels (good practice)
LABEL maintainer="LangVE"
LABEL description="Sample CI Server Spring Boot Application"

# 3. Set the working directory inside the container
WORKDIR /app

# 4. Copy the executable JAR file from the build context into the container
# The JAR file is located in the build/libs directory. We rename it to app.jar for simplicity.
COPY build/libs/sample-ci-server-*.jar app.jar

# 5. Expose the port that the application runs on
EXPOSE 8080

# 6. Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
