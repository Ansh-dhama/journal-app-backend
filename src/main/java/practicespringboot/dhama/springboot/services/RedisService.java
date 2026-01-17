package practicespringboot.dhama.springboot.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T get(String key, Class<T> clazz) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) return null; // not found

            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Redis get failed for key={}", key, e);
            return null;
        }
    }

    public void set(String key, Object value, Long timeSeconds) {
        try {
            String json = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, timeSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis set failed for key={}", key, e);
        }
    }
}
