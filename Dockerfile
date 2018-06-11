FROM maven:latest
#the build
ENV WORKINGDIR=/tmp
ADD ./src ${WORKINGDIR}/src
ADD ./pom.xml ${WORKINGDIR}/pom.xml

WORKDIR ${WORKINGDIR}
RUN mvn install compile -DskipTests
#the runtime
EXPOSE 8080

CMD ["java","-cp", "VDCBenchmark-1.0-SNAPSHOT.jar", "de.tub.Benchmark"]