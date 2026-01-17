package practicespringboot.dhama.springboot.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import practicespringboot.dhama.springboot.Entity.User;

import java.util.List;

@Component
public class UserRepoImp {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> findAll() {
        Query query = new Query();

        // Using andOperator to ensure both conditions are strictly met
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("username").exists(true),
                Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                // Simplified regex for testing; ensure this matches your DB exactly
        ));

        // It's good practice to specify the collection name if it doesn't match the class name
        // List<User> users = mongoTemplate.find(query, User.class, "users");

        return mongoTemplate.find(query, User.class,"users");
    }
}