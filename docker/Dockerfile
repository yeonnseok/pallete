FROM adoptopenjdk:11.0.11_9-jdk-hotspot as app

ARG MODULE
ARG JAR
ARG APP_PROFILE=local

RUN echo "--- Arguments ---"
RUN echo "MODULE      : ${MODULE}"
RUN echo "JAR         : ${JAR}"
RUN echo "APP_PROFILE : ${APP_PROFILE}"

ENV APP_PROFILE=${APP_PROFILE} \
    TZ=Asia/Seoul \
    JAR=${JAR}

WORKDIR /app

# copy library
WORKDIR /app
COPY ${MODULE}/build/libs/* /app/

# entry point
ENTRYPOINT java \
  -XX:+UseContainerSupport \
  -XX:InitialRAMPercentage=75 \
  -XX:MaxRAMPercentage=75 \
  -XshowSettings:vm \
  -Dfile.encoding=UTF-8 \
  -Djava.net.preferIPv4Stack=true \
  -jar /app/${JAR} \
  --spring.profiles.active=${APP_PROFILE}
