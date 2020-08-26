\#Example service spring boot

    {host}/api/example

### Build
    mvnw clean install
    
### Build docker image
    docker build -t diegof0/api-example . 
    
### Push Image
    docker push diegof0/api-example
    
### Run docker container
    docker run -d -p 8080:8080 example/service
    
## Or Deploy Kubernetes objects
    kubectl apply -f ./k8s/
    
### check deployment status 
    kubectl get all
    
### 
    localhost:80/swagger-ui.html
  
#### Other kubectl commands
    kubectl delete  service/api-example-service
    kubectl delete deployment.apps/api-example-deployment
    kubectl delete pod/api-example-pod
    
