package actors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;

import akka.actor.AbstractActor;
import json.UserJson;
import models.User;
import models.UserRepository;
import play.libs.concurrent.HttpExecutionContext;
 
public class UserActor extends AbstractActor {
 
	
	private final UserRepository userRepository;
	private final HttpExecutionContext ec;
	
	@Inject
    public UserActor(UserRepository userRepository, HttpExecutionContext ec) {
        this.userRepository = userRepository;
        this.ec = ec;
    }
	
    @Override
    public Receive createReceive() {
     
         return receiveBuilder()
                .match(UserProtocol.class, userProtocol -> {
     
                    switch (userProtocol.getAction()) {
                        case CREATE:
                            sender().tell(create(userProtocol.getUserJson()),self());
                            break;
                        case GET_ONE:
                            sender().tell(getOne(userProtocol.getId()),self());
                            break;
                        case GET_ALL:
                            sender().tell(getAll(),self());
                            break;
                        case EDIT:
                            sender().tell(edit(userProtocol.getId(), userProtocol.getUserJson()),self());
                            break;
                        case DELETE:
                            sender().tell(delete(userProtocol.getId()),self());
                            break;
                    }
     
                })
                 .matchAny(any -> unhandled("this message is unhandled" + any.getClass()))
                 .build();
    }
    
    private User create(UserJson userJson) throws Exception {
        User user = new User();
        user.email = userJson.email;
        user.lastName = userJson.lastName;
        user.name = userJson.name;

        CompletionStage<User> cs = userRepository.add(user).thenApplyAsync(p -> {
            return p;
        }, ec.current());
    	
    	return cs.toCompletableFuture().get();
    }
     
    private Optional<User> getOne(Long id) throws Exception {
    	CompletionStage<Optional<User>> cs = userRepository.findById(id).thenApplyAsync(p -> {
            return Optional.ofNullable(p);
        }, ec.current());
    	
    	return cs.toCompletableFuture().get();
    }
     
    private List<User> getAll() throws Exception {
    	CompletionStage<List<User>> cs = userRepository.findAll().thenApplyAsync(userStream -> {
    	         return userStream.collect(Collectors.toList());
    	        }, ec.current());  
    	
    	return cs.toCompletableFuture().get();
    }
    
    private User edit(Long id, UserJson userJson) throws Exception {
    	 User user = new User();
         user.email = userJson.email;
         user.lastName = userJson.lastName;
         user.name = userJson.name;
         user.id = id;
         
         CompletionStage<User> cs = userRepository.edit(user).thenApplyAsync(p -> {
             return p;
         }, ec.current());
     	
     	return cs.toCompletableFuture().get();
    }
     
    private Boolean delete(Long id) throws Exception {
    	CompletionStage<Boolean> cs = userRepository.delete(id).thenApplyAsync(r -> {
	         return r;
	        }, ec.current());  
	
    	return cs.toCompletableFuture().get();
    }
     
}