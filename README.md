# Play RESTful with Akka Actors and Async Processing

Project shows how to user Play Framework and Akka Actors with Async (CompletableFuture) Java api

Main features
1. Play Framework as core system
2. Akka Actors used to processig request
3. You can see how to call blocking JDBC API behind an asynchronous boundary using the CompletionStage API
4. It shows also a few test examples

# How to install
It requires sbt tool
```
sbt complile
sbt test
sbt run
```

# How to use
```
#get all users
curl http://localhost:9000/person

#create first user
curl -H 'Content-Type: application/json' -X POST  http://localhost:9000/person -d '{"name":"John","lastName":"Kowalski","email":"john@kowalski.com"}'

#create second user
curl -H 'Content-Type: application/json' -X POST  http://localhost:9000/person -d '{"name":"Ann","lastName":"Kowalski","email":"ann@kowalski.com"}'

#get first user only
curl -X GET  http://localhost:9000/person/1 

#modify second user
curl -H 'Content-Type: application/json' -X PUT  http://localhost:9000/person/2 -d '{"name":"Ann","lastName":"Kowalska","email":"anna@kowalska.com"}'

#create third user
curl -H 'Content-Type: application/json' -X POST  http://localhost:9000/person -d '{"name":"Cris","lastName":"Kowalski","email":"cris@kowalski.com"}'

#delete third user
curl -X DELETE  http://localhost:9000/person/3
``` 
