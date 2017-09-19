package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAUserRepository.class)
public interface UserRepository {

    CompletionStage<User> add(User user);

    CompletionStage<Stream<User>> list();
    
    CompletionStage<User> findById(Long id);

    CompletionStage<Stream<User>> findAll();
    
    CompletionStage<User> edit(User user);
    
    CompletionStage<Boolean> delete(Long id);
}
