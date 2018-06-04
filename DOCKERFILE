FROM maven:latest
#the build
ENV WORKINGDIR=/tmp
ADD ./src ${WORKINGDIR}/src
ADD ./pom.xml ${WORKINGDIR}/pom.xml

WORKDIR ${WORKINGDIR}
RUN mvn install compile -DskipTests
#the runtime
EXPOSE 8080

CMD ["mvn","spring-boot:run"]