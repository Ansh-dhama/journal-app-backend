package practicespringboot.dhama.springboot.Scheduler;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import practicespringboot.dhama.springboot.Entity.JournalEntry;
import practicespringboot.dhama.springboot.Entity.User;

import practicespringboot.dhama.springboot.Enums.Sentiment;
import practicespringboot.dhama.springboot.repo.JournayEntryRepo;
import practicespringboot.dhama.springboot.repo.UserRepoImp;
import practicespringboot.dhama.springboot.services.EmailService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepoImp userRepoImp;

    private final JournayEntryRepo journayEntryRepo;

    @Scheduled(cron = "0 * * * * *")
    public void fetchUsers() {
        System.out.println("⏰ Scheduler Started...");

        List<User> users = userRepoImp.findAll();
        System.out.println("👥 Found " + users.size() + " users in database.");

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);

        for (User user : users) {
            if (user == null || user.getId() == null) continue;

            System.out.println("➡️ Checking user: " + user.getUsername());

            // ✅ IMPORTANT: Fetch entries from journal_entries collection (not from user DBRef list)
            List<JournalEntry> recentEntries =
                    journayEntryRepo.findByUser_IdAndDateAfterOrderByDateDesc(user.getId(), sevenDaysAgo);

            if (recentEntries == null || recentEntries.isEmpty()) {
                System.out.println("   ❌ No recent journal entries found for this user.");
                continue;
            }

            System.out.println("   ✅ Found " + recentEntries.size() + " recent entries.");

            long happyCount = recentEntries.stream()
                    .filter(e -> e.getSentiment() == Sentiment.HAPPY)
                    .count();

            String entryDescriptions = recentEntries.stream()
                    .map(JournalEntry::getDescription)
                    .filter(d -> d != null && !d.isBlank())
                    .collect(Collectors.joining(", "));

            // basic email checks
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                System.out.println("   ⚠️ User email missing, skipping email send.");
                continue;
            }

            // optional: if all descriptions empty, still send summary
            if (entryDescriptions.isBlank()) {
                entryDescriptions = "(No descriptions)";
            }

            System.out.println("   📧 Attempting to send email to: " + user.getEmail());

            try {
                emailService.sendEmail(
                        user.getEmail(),
                        "Weekly Recap: " + happyCount + " happy days",
                        "Entries: " + entryDescriptions
                );
                System.out.println("   🚀 SUCCESS: Email sent!");
            } catch (Exception ex) {
                System.out.println("   🔥 ERROR sending email: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        System.out.println("🛑 Scheduler Finished.");
    }
}