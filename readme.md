
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



rabbit
https://github.com/rabbitmq/rabbitmq-tutorials/tree/master/java

keycloak-client-token-provider.serverUrl=https://10.17.1.58/auth/;data.database.address=jdbc:postgresql://10.17.1.58:5432/encourage;keycloak-client-token-provider.realm=default;keycloak-client-token-provider.master-client-secret=37b016e0-1a7f-420d-8c4a-ab5b15447307;spring.rabbitmq.host=10.17.1.58


start locally conversations:
ssh -L 6443:127.0.0.1:6443 root@vm058.dev.cz.zoomint.com
kubectl port-forward encourage-data-697f5fd5c7-9chpv 8300:8300
kubectl port-forward kubernetes-zookeeper-67cb4d8cc7-lg62w 9181:9181
kubectl port-forward kubernetes-solrcloud-0 8983:8983
kubectl port-forward encourage-zqm-connector-77f5bf6d9f-zktf9 8201:8201
kubectl port-forward kubernetes-solrcloud-zookeeper-0 9983:2181

kubectl port-forward kubernetes-rabbitmq-0 15672:15672