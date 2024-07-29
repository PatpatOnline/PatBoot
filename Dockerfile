# PatBoot Dockerfile

FROM openjdk:17-jdk-slim

ARG VERSION
ARG PROFILE

COPY assets/PatBoot-${VERSION}.jar /application.jar
ENV PROFILE=${PROFILE}

EXPOSE 8080

CMD ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "/application.jar", "--spring.profiles.active=${PROFILE}"]
