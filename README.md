# IpContextService

## API INFO
  
  **ip info**
        
    http://{host}:{port}/api/ip/{ip}
    curl -X GET "http://localhost:8080/api/ip/1.1.1.1" -H "accept: */*"
    
  **Ip Metrics**
  
    http://{host}:{port}/api/ip/{ip}
    curl -X GET "http://localhost:8080/api/ip/metrics" -H "accept: */*"
    
  **Api docs**
  
    {host}:{port}/swagger-ui.html
    http://localhost:8080/swagger-ui.html
    
## Build
    mvn clean install
    
### Coverage Report
    {projectDir}/target/site/jacoco/index.html

## Run with maven

### Profiles

  * **SPRING_PROFILES_ACTIVE=default**  
  
     Runs with h2 in mem database
    
        mvn spring-boot:run
     
     h2 console: http://localhost:8080/h2-console/ 
     
     url: jdbc:h2:mem:testdb
     
     user: sa
     
     pass:    
     

  * **SPRING_PROFILES_ACTIVE=dev**  
    Runs using postgres database. you need to set DB_HOST, DB_PORT, DB_USER, DB_PASSWORD as environment variables.
    
        #Linux
        export DB_HOST=localhost
        export DB_PORT=5432
        export DB_USER=postgres
        export DB_PASSWORD=root
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
        
        #windows
        SET DB_HOST=localhost
        SET DB_PORT=5432
        SET DB_USER=postgres
        SET DB_PASSWORD=root
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
   
  
## Docker  
    
### Build project
    mvn clean install
    
### Build docker image
    docker build -t {repository}/ipcontext-service .
    docker build -t diegof0/ipcontext-service . 
    
### Run docker container

   * Run with default profile: h2 database
   
         docker run -d -p 8080:8080 diegof0/ipcontext-service
      
   * Run with dev profile: postgres database
    you need to provide database host , port, user and password as environment variables to the container
        
         docker run -e SPRING_PROFILES_ACTIVE=dev -e DB_HOST=192.168.50.184 -e DB_PORT=5432 -e DB_USER=postgres -e DB_PASSWORD=root -p 8080:8080 diegof0/ipcontext-service
    
   * Use **DB_HOST=host.docker.internal** for database server in the host machine
         
         docker run -e SPRING_PROFILES_ACTIVE=dev -e DB_HOST=host.docker.internal -e DB_PORT=5432 -e DB_USER=postgres -e DB_PASSWORD=root -p 8080:8080 diegof0/ipcontext-service
   
### Push Image
       docker push {repository}/ipcontext-service
       docker push diegof0/ipcontext-service
    
## Kubernetes
    kubectl apply -f ./k8s/
    
### check deployment status 
    kubectl get all

### delete commands
    kubectl delete service/ipcontext-service
    kubectl delete deployment.apps/ipcontext-service-deployment
    kubectl delete pod/ipcontext-service-pod
    
    

  

    
    