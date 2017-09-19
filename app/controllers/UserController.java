package controllers;
 
import converters.UserToUserJson;
import json.UserJson;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import actors.UserProtocol;
import akka.actor.ActorRef;
import akka.pattern.PatternsCS;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
 
@Singleton
public class UserController extends Controller {
 
    private UserToUserJson userToUserJson;
    
    private ActorRef userActor;
    
    @Inject
    public UserController(@Named("user-actor") ActorRef userActor, UserToUserJson userToUserJson) {
        this.userActor = userActor;
        this.userToUserJson = userToUserJson;
    }
    
    public CompletionStage<Result> findById(Long id) {
        return PatternsCS.ask(userActor, new UserProtocol(UserProtocol.Action.GET_ONE,id),1000)
                .thenApply(response -> (Optional<User>) response)
                .thenApply(userOptional -> userOptional
                        .map(userToUserJson)
                        .map(userJson -> ok(Json.toJson(userJson))).orElse(notFound()));
    }
    
    public CompletionStage<Result> save() {
        UserJson userJson = Json.fromJson(request().body().asJson(),UserJson.class);
     
        return PatternsCS.ask(userActor, new UserProtocol(UserProtocol.Action.CREATE,userJson),1000)
                .thenApply(response -> (User) response)
                .thenApply(userToUserJson::apply)
                .thenApply(json -> ok(Json.toJson(json)));
     
    }
    
    public CompletionStage<Result> findAll() {
     
        return PatternsCS.ask(userActor, new UserProtocol(UserProtocol.Action.GET_ALL),1000)
                .thenApply(response -> (List<User>) response)
                .thenApply(list -> ok(Json.toJson(list.stream().map(userToUserJson).collect(Collectors.toList()))));
     
    }
    
    public CompletionStage<Result> edit(Long id) {
     
        UserJson userJson = Json.fromJson(request().body().asJson(),UserJson.class);
        
        return PatternsCS.ask(userActor, new UserProtocol(UserProtocol.Action.EDIT,id,userJson),1000)
                .thenApply(response -> (User) response)
                .thenApply(userToUserJson::apply)
                .thenApply(json -> ok(Json.toJson(json)));
        
    }
     
    public CompletionStage<Result> delete(Long id) {
     
        return PatternsCS.ask(userActor,new UserProtocol(UserProtocol.Action.DELETE,id),1000)
                .thenApply(response -> (Boolean) response)
                .thenApply(response -> {
                    if(response) {
                        return noContent();
                    }
                    return notFound();
                });
    }
    
 
}