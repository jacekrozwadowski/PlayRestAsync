import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static play.mvc.Http.Status.OK;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;

import org.junit.BeforeClass;
import org.junit.Test;

import actors.UserActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import controllers.UserController;
import converters.UserToUserJson;
import models.JPAUserRepository;
import models.User;
import models.UserRepository;
import play.Application;
import play.core.j.JavaContextComponents;
import play.core.j.JavaHelpers;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

public class UnitTest extends WithApplication implements JavaHelpers {
	
	@Override
	protected Application provideApplication() {
		 return new GuiceApplicationBuilder().build();
	}
	
	static ActorSystem system; 
	
	@BeforeClass
    public static void setup() {
        system = ActorSystem.create();
	}
	
	@Test
    public void checkAddUser() {
		
		HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
		UserRepository userRepository = app.injector().instanceOf(JPAUserRepository.class);
		
		final Props props = Props.create(UserActor.class, userRepository, ec);
		final ActorRef userActor = system.actorOf(props, "user-actor");
		
		User user = new User("john@kowalski.com", "John", "Kowalski");
        user.id = 1L;

        UserToUserJson userToUserJson = new UserToUserJson();
        
        // Set up the request builder to reflect input
        final Http.RequestBuilder requestBuilder = new Http.RequestBuilder().method("post").bodyJson(Json.toJson(user));
        
        // Add in an Http.Context here using invokeWithContext:
        // XXX extending JavaHelpers is a cheat to get at JavaContextComponents easily, put this into helpers
        JavaContextComponents components = createContextComponents();
        final CompletionStage<Result> stage = Helpers.invokeWithContext(requestBuilder, components, () -> {
            //HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());

            // Create controller and call method under test:
            final UserController controller = new UserController(userActor, userToUserJson);
            return controller.save();
        });
        
        // Test the completed result
        await().atMost(1000, MILLISECONDS).until(() ->
	        assertThat(stage.toCompletableFuture()).isCompletedWithValueMatching(result ->
	        	//result.status() == SEE_OTHER, "Should redirect after operation"
	        	result.status() == OK, "Status expected OK"
	        )
		);
		
	}

}
