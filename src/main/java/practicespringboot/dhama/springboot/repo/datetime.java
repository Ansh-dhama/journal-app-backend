package practicespringboot.dhama.springboot.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import practicespringboot.dhama.springboot.Entity.dateTimeEntity;

public interface datetime extends MongoRepository<dateTimeEntity, ObjectId> {

}
