FROM gradle:8.6.0 as build

WORKDIR /app

COPY . .

RUN gradle build

FROM tomcat:10

COPY --from=build /app/build/libs/*.war $CATALINA_HOME/webapps/weather.war

CMD ["catalina.sh","run"]