package practicespringboot.dhama.springboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import practicespringboot.dhama.springboot.Enums.Sentiment;

@Data
@AllArgsConstructor
public class JournalEntryCreateDto {
    private String id;
    @NotNull
    private String title;
    private String description;
    private Sentiment sentiment;
}
