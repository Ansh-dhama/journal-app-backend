package practicespringboot.dhama.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.util.TreeSet;

@SpringBootApplication
@EnableTransactionManagement
//@EnableScheduling
public class    SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
    @Bean
    public PlatformTransactionManager add(MongoDatabaseFactory db){
        return new MongoTransactionManager(db);
    }
    @Bean public RestTemplate restTemplate(){
        return new RestTemplate();
}

}
