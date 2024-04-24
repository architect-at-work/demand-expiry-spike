# Run in docker

```shell
./gradlew build
docker build -t demand-expiry .
docker run -p 8080:8080 demand-expiry -e --REDIS_HOST=host.docker.internal
```

# Get K8S Dashboard UI

1. K8s Proxy
    ```shell
    kubectl proxy --port=9000
    ```
2. ```shell
    kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
    cd tools/k8s
    kubectl apply -f dashboard-adminuser.yml
    kubectl apply -f dashboard-clusterrolebinding.yaml
    kubectl apply -f secret.yml
    kubectl create token dashboard-user --namespace kubernetes-dashboard --duration 300m
    ```
3. Access UI
   ```shell
    http://localhost:9000/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy
   ```