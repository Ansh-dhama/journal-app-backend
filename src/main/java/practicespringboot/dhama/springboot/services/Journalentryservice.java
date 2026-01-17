package practicespringboot.dhama.springboot.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicespringboot.dhama.springboot.Entity.JournalEntry;
import practicespringboot.dhama.springboot.Entity.User;
import practicespringboot.dhama.springboot.repo.JournayEntryRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Journalentryservice {

    private final JournayEntryRepo journayEntryRepo;
    private final UserService userService;

    private  static final Logger logger = LoggerFactory.getLogger(Journalentryservice.class);

    public List<JournalEntry> getAllForUser(String username) {
        User user = userService.findByUsername(username);
        if (user == null) return List.of();
        return journayEntryRepo.findByUser_IdOrderByIdDesc(user.getId());
    }

    @Transactional
    public void saveEntryForUsername(JournalEntry entry, String username) {
try{
        User user = userService.findByUsername(username);
        entry.setId(null);
        entry.setUser(user);

        JournalEntry saved = journayEntryRepo.save(entry);

        user.getJournalEntries().add(saved);
        userService.save(user);   // <-- correct method

    }catch(Exception e){
    System.out.println("ansh");
    }
    }



    public Optional<JournalEntry> getById(ObjectId id) {
        return journayEntryRepo.findById(id);
    }

    @Transactional
    public boolean delete(String username, String id) {
        final ObjectId oid;
        try { oid = new ObjectId(id); }
        catch (IllegalArgumentException e) { return false; }

        User user = userService.findByUsername(username);
        if (user == null) return false;

        long n = journayEntryRepo.deleteByIdAndUser_Id(oid, user.getId());
        if (n == 0) return false;

        user.getJournalEntries().removeIf(e -> e == null || oid.equals(e.getId()));
        userService.save(user);
        return true;
    }

    @Transactional
    public Optional<JournalEntry> patch(ObjectId id, JournalEntry patch) {
        return journayEntryRepo.findById(id).map(ex -> {
            if (patch.getTitle() != null) ex.setTitle(patch.getTitle());
            if (patch.getDescription() != null) ex.setDescription(patch.getDescription());
            return journayEntryRepo.save(ex);
        });
    }
}
