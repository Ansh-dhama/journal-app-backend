package practicespringboot.dhama.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practicespringboot.dhama.springboot.Entity.JournalEntry;
import practicespringboot.dhama.springboot.dto.JournalEntryCreateDto;
import practicespringboot.dhama.springboot.services.Journalentryservice; // Note: You might want to rename this class to PascalCase later (JournalEntryService)

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
@Tag(name = "Journal APIs", description = "Endpoints for managing user journal entries")
public class JournalController {

    private final Journalentryservice service;

    @Operation(summary = "Get all entries", description = "Fetch all journal entries for the authenticated user")
    @GetMapping
    public ResponseEntity<List<JournalEntryCreateDto>> getAllEntriesOfUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JournalEntry> entries = service.getAllForUser(username);

        if (entries == null || entries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<JournalEntryCreateDto> response = entries.stream()
                .map(e -> new JournalEntryCreateDto(
                        e.getId().toHexString(),
                        e.getTitle(),
                        e.getDescription(),
                        e.getSentiment()
                )).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create entry", description = "Create a new journal entry")
    @PostMapping
    public ResponseEntity<JournalEntryCreateDto> create(@RequestBody JournalEntryCreateDto body) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        JournalEntry entry = new JournalEntry();
        entry.setTitle(body.getTitle());
        entry.setDescription(body.getDescription());
        entry.setSentiment(body.getSentiment());

        // Service handles saving and linking to user
        service.saveEntryForUsername(entry, username);

        // Return 201 Created
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete entry", description = "Delete a specific journal entry by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean deleted = service.delete(username, id);

        return deleted
                ? ResponseEntity.noContent().build()   // 204 No Content (Success)
                : ResponseEntity.notFound().build();   // 404 Not Found
    }
}