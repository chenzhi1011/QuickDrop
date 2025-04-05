FROM maven:3-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

CMD ["sh", "-c", "java -jar target/*.jar"]