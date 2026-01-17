package practicespringboot.dhama.springboot.AppCatch;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import practicespringboot.dhama.springboot.Entity.dateTimeEntity;
import practicespringboot.dhama.springboot.repo.datetime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCatch {
    @Autowired
    private  datetime datetime;
    public    Map<String,String> APP_CATCH = new HashMap<>();
    @Scheduled(cron = "0 * * * * *")
    @PostConstruct
    public void init() { // Note: fixed typo from 'inti' to 'init'
        List<dateTimeEntity> all = datetime.findAll();
        System.out.println("Loading cache... Found items: " + all.size());
        for(dateTimeEntity e : all){
            APP_CATCH.put(e.getKey(), e.getValue());
        }
    }
}


