package modules;
 
import actors.UserActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;
 
public class AkkaModule extends AbstractModule implements AkkaGuiceSupport{
    @Override
    protected void configure() {
 
        bindActor(UserActor.class,"user-actor");
 
    }
}