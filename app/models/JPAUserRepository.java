package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAUserRepository implements UserRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAUserRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<User> add(User user) {
        return supplyAsync(() -> wrap(em -> insert(em, user)), executionContext);
    }

    @Override
    public CompletionStage<Stream<User>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }
    
    @Override
    public CompletionStage<Stream<User>> findAll() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }
    
    @Override
    public CompletionStage<User> findById(Long id) {
        return supplyAsync(() -> wrap(em -> findById(em, id)), executionContext);
    }
    
    @Override
    public CompletionStage<User> edit(User user) {
        return supplyAsync(() -> wrap(em -> update(em, user)), executionContext);
    }
    
    @Override
    public CompletionStage<Boolean> delete(Long id) {
        return supplyAsync(() -> wrap(em -> delete(em, id)), executionContext);
    }
    

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private User insert(EntityManager em, User user) {
        em.persist(user);
        return user;
    }
    
    private User update(EntityManager em, User user) {
    	User p = em.find(User.class, user.id);
    	p.email = user.email;
    	p.name = user.name;
    	p.lastName = user.lastName;
    	
        em.merge(p);
        return p;
    }
    
    private User findById(EntityManager em, Long id) {
    	return em.find(User.class, id);
    }
    
    private Boolean delete(EntityManager em, Long id) {
    	User p = em.find(User.class, id);
    	em.remove(p);
    	return true;
    }

    private Stream<User> list(EntityManager em) {
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();
        return users.stream();
    }
}
