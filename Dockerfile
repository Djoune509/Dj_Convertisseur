# Stage 1: Build jar
FROM eclipse-temurin:22-jdk AS build
WORKDIR /app
COPY . .

# Fix mvnw permissions
RUN chmod +x mvnw

# Build project
RUN ./mvnw clean package -DskipTests

# Stage 2: Run jar
FROM eclipse-temurin:22-jre
WORKDIR /app
COPY --from=build /app/target/Dj_Convertisseur-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]