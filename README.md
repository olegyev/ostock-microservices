# ostock-microservices
Training project for microservices with Spring Cloud

<link>https://github.com/ihuaylupo/manning-smia/tree/master</link>

## Build and run:
<ol>
  <li><b>Run config server first.</b> </br>
      From <a href="https://github.com/olegyev/ostock-microservices/tree/master/configserver">config repo</a>, build a Docker image: </br>
      <code>./mvnw clean package dockerfile:build</code></br>
      Then, start config server in a Docker container: </br>
      <code>docker run -itd -p 8071:8071 ostock/configserver:0.0.1-SNAPSHOT</code>
  </li>
  <li>
    <b>Build all other services (they depend on config server).</b> </br>
    From each remaining service's root folder, run: </br>
    <code>./mvnw clean package dockerfile:build</code>
  </li>
  <li>
    <b>Stop and prune config server container to free up a port.</b> </br>
    <code>docker ps</code> - to see config server container name </br>
    <code>docker stop [CONTAINER_NAME]</code> </br>
    <code>docker container prune</code> - to prune dangling containers </br>
  </li>
  <li>
    <b>Run an entire system with docker compose.</b> </br>
    From <a href="https://github.com/olegyev/ostock-microservices/tree/master/docker">Docker repo's</a> root folder, run: </br>
    <code>docker compose up</code>
  </li>
  <li>
    <b>Stop containers and free up resources.</b> </br>
    From <a href="https://github.com/olegyev/ostock-microservices/tree/master/docker">Docker repo's</a> root folder, run: </br>
    <code>docker compose down</code> </br>
    To free up resources, run: </br>
    <code>docker image prune</code> - to prune dangling images </br>
    <code>docker volume prune</code> - to prune dangling volumes </br>
    <code>docker system df</code> - to ensure that resources are freed up </br>
  </li>
</ol>
