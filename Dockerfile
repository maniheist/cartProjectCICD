FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests


#Second stage
FROM openjdk:17-jdk-slim
WORKDIR /app
LABEL authors="manisankar.r"
COPY --from=build /app/target/cartProject-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8067
CMD ["java", "-jar", "app.jar"]
