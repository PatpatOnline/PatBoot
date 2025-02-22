# PatBoot Dockerfile

FROM openjdk:17-jdk-slim

ARG VERSION

COPY target/PatBoot-${VERSION}.jar /application.jar

EXPOSE 8080

CMD ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "/application.jar", "--spring.profiles.active=${PROFILE}"]
