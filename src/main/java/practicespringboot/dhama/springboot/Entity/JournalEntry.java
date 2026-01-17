package practicespringboot.dhama.springboot.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import practicespringboot.dhama.springboot.Enums.Sentiment;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Data
public class JournalEntry {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String title;
    private String description;
    private LocalDateTime date;

   private Sentiment sentiment;
    @DBRef(lazy = true)
    @JsonIgnoreProperties({"journalEntries", "password"})
    private User user; // set on server side only
}
