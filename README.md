#IpContextService

## API INFO
    {host}/swagger-ui.html
    localhost/swagger-ui.html
  
## Build
    mvn clean install
    
## Run with maven
    mvn spring-boot:run    
  
## Docker  
    
### Build docker image
    docker build -t {repository}/ipcontext-service .
    docker build -t diegof0/ipcontext-service . 
    
### Push Image
    docker push {repository}/ipcontext-service
    docker push diegof0/ipcontext-service
    
### Run docker container
    docker run -d -p 8080:8080 diegof0/ipcontext-service
   
   
    
## Kubernetes
    kubectl apply -f ./k8s/
    
### check deployment status 
    kubectl get all

### delete commands
    kubectl delete service/ipcontext-service
    kubectl delete deployment.apps/ipcontext-service-deployment
    kubectl delete pod/ipcontext-service-pod
    
    

  

    
    