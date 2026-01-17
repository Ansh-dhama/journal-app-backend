package practicespringboot.dhama.springboot.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import practicespringboot.dhama.springboot.Entity.JournalEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JournayEntryRepo extends MongoRepository<JournalEntry, ObjectId> {
    List<JournalEntry> findByUser_IdOrderByIdDesc(ObjectId userId);
    Optional<JournalEntry> findByIdAndUser_Id(ObjectId id, ObjectId userId);
    long deleteByIdAndUser_Id(ObjectId id, ObjectId userId);


    List<JournalEntry> findByUser_IdAndDateAfterOrderByDateDesc(ObjectId id, LocalDateTime sevenDaysAgo);
// OK
}
