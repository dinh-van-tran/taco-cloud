# Kafka
# Connect to strimzi kafka outside the cluster
- Strimzi Kafka has problems with connecting outside the cluster in some environments:
  + Minikube
  + Docker Desktop
  + K3d
  + Kind
- The solution is to use a different tool [kekspose](https://github.com/scholzj/kekspose) to expose the kafka broker outside the cluster.
```bash
kekspose --namespace kafka --service kafka-cluster
 ```