
Solr:
1. /rez/dev/solr-8.11.2/bin/solr start -p 8983
2. stop solr:
   /rez/dev/solr-8.11.2/bin/solr stop -all
3. restart solr:
   /rez/dev/solr-8.11.2/bin/solr restart
4. create card collection:
   /rez/dev/solr-8.11.2/bin/solr create -c cars
5. Add doc to index:
   curl http://localhost:8983/solr/cars/update -H ‘Content-type:text/xml’ –data-binary @cars.xml
Rabbit:
5. docker-compose up

Keycloak:
1. cd /rez/dev/keycloak-18.0.2/bin/
2. ./kc.sh start-dev


