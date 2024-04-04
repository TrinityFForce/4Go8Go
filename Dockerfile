FROM openjdk:17-jdk
LABEL maintainer="TrinityForce"
ARG JAR_FILE=build/libs/sagopalgo-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} sagopalgo.jar
ENTRYPOINT ["java", "-jar", "sagopalgo.jar"]