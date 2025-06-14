# Use the Eclipse temurin alpine official image
FROM eclipse-temurin:21-jdk-alpine

# Create and change to the app directory
WORKDIR /app

# Copy local code to the container image
COPY . .

# Dá permissão de execução ao script mvnw
RUN chmod +x mvnw

# Build the app
RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

# Run the quarkus app
CMD ["java", "-jar", "target/quarkus-app/quarkus-run.jar"]
