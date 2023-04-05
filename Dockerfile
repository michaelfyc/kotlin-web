FROM gradle:7-jdk11 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUNM gradle buildFatJar --no-daemon


FROM openjdk:11
EXPOSE 9999: 9999
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/kotlin-web.jar
ENTRYPOINT ["java", "-jar", "/app/kotlin-web.jar"]