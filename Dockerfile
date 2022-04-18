FROM openjdk:8-jdk

COPY /target/cronjob-mail-1.0.jar /data/CronJobMailAlert/cronjob-mail-1.0.jar
COPY src/main/kotlin/. /data/CronJobMailAlert/
COPY src/main/resources/. /data/CronJobMailAlert/resources
COPY target/. /data/CronJobMailAlert/target

WORKDIR /data/CronJobMailAlert

RUN java -version

CMD ["java","-jar","cronjob-mail-1.0.jar"]

EXPOSE 8080-8081
