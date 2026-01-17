package practicespringboot.dhama.springboot.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import practicespringboot.dhama.springboot.Entity.User;
public interface UserRepo extends MongoRepository<User, ObjectId> {
    void deleteByUsername(String name);
    User findByUsername(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
}
