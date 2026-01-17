package practicespringboot.dhama.springboot.Scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import practicespringboot.dhama.springboot.Entity.JournalEntry;
import practicespringboot.dhama.springboot.Entity.User;
import practicespringboot.dhama.springboot.services.EmailService;
import practicespringboot.dhama.springboot.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
@Component
public class EveryMonth {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 * * * * *")
    public void inactiveusers() {
        List<User> users = userService.getAll();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        for (User user : users) {

            if (user.getLastReminderSent() != null && user.getLastReminderSent().isAfter(sevenDaysAgo)) {
                continue;
            }

            List<JournalEntry> journalEntries = user.getJournalEntries();

            boolean hasRecentEntry = journalEntries.stream()
                    .anyMatch(x -> x.getDate() != null && x.getDate().isAfter(oneMonthAgo));

            if (!hasRecentEntry) {
                try {
                    emailService.sendEmail(
                            user.getEmail(),
                            "We miss you!",
                            "Hey " + user.getUsername() + ", come back!"
                    );
                    user.setLastReminderSent(LocalDateTime.now());
                    userService.save(user);

                    System.out.println("✅ Reminder sent to: " + user.getUsername());
                } catch (Exception e) {
                    System.out.println("Error sending email");
                }
            }
        }
    }
}