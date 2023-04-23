FROM adoptopenjdk:11.0.11_9-jdk-hotspot as app

ARG APP_PROFILE=local
ENV APP_PROFILE=${APP_PROFILE} \
    TZ=Asia/Seoul

WORKDIR /app

# copy library
WORKDIR /app
COPY build/libs/* /app/

# entry point
ENTRYPOINT java \
  -XX:+UseContainerSupport \
  -XX:InitialRAMPercentage=75 \
  -XX:MaxRAMPercentage=75 \
  -XshowSettings:vm \
  -Dfile.encoding=UTF-8 \
  -Djava.net.preferIPv4Stack=true \
  -jar /app/api.jar \
  --spring.profiles.active=${APP_PROFILE}
