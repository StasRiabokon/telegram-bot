#Builder
FROM maven:3.6.1-jdk-12 as builder

RUN mkdir -p /opt/memebot/src
ARG PROJECT_HOME

COPY src $PROJECT_HOME/src
COPY pom.xml $PROJECT_HOME

WORKDIR $PROJECT_HOME

RUN mvn -DskipTests=true clean install

#Main image
FROM openjdk:13-jdk-alpine

#Do NOT forget to chown $HOME/data/app-data folder at host machine
RUN apk add --no-cache bash \
    && mkdir -p /opt/memebot/logs \
    && addgroup memebot \
    && adduser -D -G memebot memebot \
    && chown -R memebot:memebot /opt/memebot/logs/

USER memebot

ARG PROJECT_HOME
WORKDIR $PROJECT_HOME

COPY --from=builder $PROJECT_HOME/target/memebot.jar .

CMD ["java", "-jar", "memebot.jar"]