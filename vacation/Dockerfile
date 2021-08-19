FROM openjdk:8-jre-alpine
VOLUME /tmp
VOLUME /root
VOLUME /log
ARG JAR_FILE=./vacation-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} to-do-springboot.jar
EXPOSE 9000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/to-do-springboot.jar"]
