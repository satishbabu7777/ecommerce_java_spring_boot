FROM eclipse-temurin:17-jdk-alpine

LABEL maintainer="Satish Babu"
LABEL email="satishbabu7777@gmail.com"
LABEL project="ecommerce-app"
LABEL service="api-gateway"
LABEL version="1.0"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]