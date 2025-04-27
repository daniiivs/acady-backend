FROM openjdk:21
COPY ./out/production/acady-backend/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","AcadyBackendApplication"]