ssh -L 6443:127.0.0.1:6443 root@vm058.dev.cz.zoomint.com
kubectl get pods | grep encourage-data | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8300:8300
kubectl get pods | grep encourage-zqm-connector | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8201:8201
kubectl get pods | grep kubernetes-zookeeper | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 9181:9181
kubectl get pods | grep kubernetes-solrcloud-zookeeper-0 | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 9983:2181
kubectl get pods | grep kubernetes-solrcloud-0 | sed 's/\|/ /'|awk '{print $1}' | xargs -I {} kubectl port-forward {} 8983:8983