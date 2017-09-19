
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.GET;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.route;

import java.util.concurrent.CompletionStage;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import models.JPAUserRepository;
import models.User;
import models.UserRepository;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

public class IntegrationTest extends WithApplication {

	 @Override
	 protected Application provideApplication() {
		 return new GuiceApplicationBuilder().build();
	 }
	 
	 @Test
	 public void testAdd() {
		 UserRepository repository = app.injector().instanceOf(JPAUserRepository.class);
		 User pReq = new User("john@kowalski.com", "John", "Kowalski");
	     
	     CompletionStage<User> cs = repository.add(pReq).thenApply(p -> {
	    	 	Http.RequestBuilder request = new Http.RequestBuilder()
		                .method(GET)
		                .uri("/user/"+p.id);
	    	 	
	    	 	Result result = route(app, request);
	    	 	final String body = contentAsString(result);
	   	     
	    	 	JsonNode json = Json.parse(body);
	    	 	User pRes = Json.fromJson(json, User.class);
	   	     
	    	 	System.out.println(pRes);
	    	 	assertThat(pReq.email, equalTo(pRes.email));
	    	 	assertThat(pReq.name, equalTo(pRes.name));
	    	 	assertThat(pReq.lastName, equalTo(pRes.lastName));
	    	 	
	            return p;
	        });

	     try{
	    	 cs.toCompletableFuture().get();
	     } catch(Exception e){
	    	 e.printStackTrace();
	     }
	     
	     
	 }
	
}
