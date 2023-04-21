FROM 17-alpine3.16
VOLUME /midasweb
EXPOSE 8080
ENTRYPOINT ["java","-jar","/build/libs/midasweb-0.0.1.jar"]