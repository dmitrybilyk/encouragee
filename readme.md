
Solr:
https://solr.apache.org/guide/solr/latest/query-guide/common-query-parameters.html#cache-local-parameter - current progress with Ruslan
1. /home/dmytro/dev/tools/solr-9.2.1/bin/solr start -p 8983
2. stop solr:
   /home/dmytro/dev/tools/solr-9.2.1/bin/solr stop -all
3. restart solr:
   /home/dmytro/dev/tools/solr-9.2.1/bin/solr restart
4. create card collection:
   /home/dmytro/dev/tools/solr-9.2.1/bin/solr create -c cars
5. Add doc to index:
   curl http://localhost:8983/solr/cars/update -H ‘Content-type:text/xml’ –data-binary @cars.xml

start in SolrCloud:
/home/dmytro/dev/tools/solr-9.2.1/bin/solr -e cloud

curl --request POST \
--url http://localhost:8983/api/collections \
--header 'Content-Type: application/json' \
--data '{
"create": {
"name": "techproducts",
"numShards": 1,
"replicationFactor": 1
}
}'

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
kubectl get pods | grep encourage-data | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8300:8300
kubectl get pods | grep encourage-zqm-connector | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8201:8201
kubectl get pods | grep kubernetes-zookeeper | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 9181:9181
kubectl get pods | grep kubernetes-solrcloud-zookeeper-0 | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 9983:2181
kubectl get pods | grep kubernetes-solrcloud-0 | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8983:8983

kubectl get pods | grep encourage-framework | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8102:8102
kubectl get pods | grep encourage-conversations | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8107:8107

kubectl port-forward kubernetes-rabbitmq-0 15672:15672

### Run with rabbit profile
~/dev/projects/others/encouragee/service$ 
mvn spring-boot:run -Dspring-boot.run.profiles=rabbit -Prabbit

### Run with solr profile
mvn spring-boot:run -Dspring-boot.run.profiles=solr -Dkeycloak-client-token-provider.serverUrl=https://10.17.1.58/auth/ -D keycloak-client-token-provider.realm=default -Dkeycloak-client-token-provider.master-client-secret=edc05414-47c2-48d4-a3ba-d1c7505ca309
