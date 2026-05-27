FROM eclipse-temurin:21-jdk-alpine

LABEL maintainer="Satish Babu"
LABEL email="satishbabu7777@gmail.com"
LABEL project="ecommerce-app"
LABEL service="service-registry"
LABEL version="1.0"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","app.jar"]