package practicespringboot.dhama.springboot.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("AppCatch")
@Data
@NoArgsConstructor
public class dateTimeEntity {
    private String key;
    private String value;
}
