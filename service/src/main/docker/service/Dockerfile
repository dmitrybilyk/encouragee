FROM openjdk:11.0-jdk-slim as build
WORKDIR /workspace/app
ADD maven/@project.build.finalName@.jar application.jar
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /workspace/app/application.jar)

FROM eleveo/base-images/centos8java11:2.2.12
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
USER root
RUN groupadd -r encourage && useradd -r -g encourage encourage
USER encourage
ENTRYPOINT ["java","-cp","app:app/lib/*","com.encouragee.conversation.EncourageeApplication","--logging.config=classpath:logback-docker.xml"]
