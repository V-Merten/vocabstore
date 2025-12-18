FROM node:18-alpine AS frontend-build
WORKDIR /app/frontend

COPY frontend/package*.json frontend/package-lock.json ./
RUN npm ci

COPY frontend ./
RUN npm run build

FROM maven:3.9.9-eclipse-temurin-23 AS backend-build
WORKDIR /app

COPY backend ./backend
COPY --from=frontend-build /app/frontend ./frontend

COPY --from=frontend-build /app/frontend/dist ./backend/src/main/resources/static

WORKDIR /app/backend
RUN mvn -DskipTests package

FROM eclipse-temurin:23-jre AS runtime
WORKDIR /app

COPY --from=backend-build /app/backend/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]