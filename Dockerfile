FROM openjdk:11-jdk-oracle

ARG target/clientes.jar

COPY target/clientes.jar clientes.jar

EXPOSE 9001

ENTRYPOINT ["java","-jar", "clientes.jar"]
