FROM registry1.dso.mil/ironbank/redhat/openjdk/openjdk21:1.21

WORKDIR /app

COPY ./target/elrrdatasync-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java","-Dcom.redhat.fips=false","-jar","elrrdatasync-0.0.1-SNAPSHOT.jar"]
