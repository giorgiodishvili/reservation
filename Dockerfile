#
# Build stage
#
FROM maven:3.6.3-jdk-11 as builder
WORKDIR /app

COPY src src
COPY pom.xml .


RUN mvn -f /app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:11.0.10-jre-slim

ENV TZ="Asia/Tbilisi"
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
HEALTHCHECK --interval=5s --timeout=2s --retries=7 CMD wget -q http://127.0.0.1:8080/ping -O /dev/null || exit 1

WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT [\
        "java",\
        "-jar",\
        "/app/app.jar"\
]