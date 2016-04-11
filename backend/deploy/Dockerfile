FROM java:8
ADD hashibackend-1.0-SNAPSHOT.tgz /opt/HashiBackend
WORKDIR /opt/HashiBackend/hashibackend-1.0-SNAPSHOT
CMD ["/opt/HashiBackend/hashibackend-1.0-SNAPSHOT/bin/hashibackend", "-Dconfig.resource=prod.conf", "-Dplay.evolutions.db.default.autoApply=true"]
EXPOSE 9000
