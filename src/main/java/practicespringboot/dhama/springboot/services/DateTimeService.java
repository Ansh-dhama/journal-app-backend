    package practicespringboot.dhama.springboot.services;

    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;
    import practicespringboot.dhama.springboot.AppCatch.AppCatch;
    import practicespringboot.dhama.springboot.api.response.DateTimeResponse;

    import java.util.Map;
@Slf4j
    @Service
    public class DateTimeService {

        @Autowired
        private AppCatch appCatch;

        private static final String API_KEY = "b7ec87951cca4d08a8ff4ba8af40752d";

        @Autowired
        private RestTemplate restTemplate;
@Autowired
        private RedisService redisService;

        public DateTimeResponse getCurrentTime(String city) {
            String cityKey = city.trim().toLowerCase();
            String redisKey = "Time:" + cityKey;
           DateTimeResponse response =redisService.get(redisKey,DateTimeResponse.class);
           if(response!= null) {
               log.info("getCurrentTime:{city} from redis",city,response);
               return response;
           }


                String url = appCatch.APP_CATCH.get("DateTimeApi");

                if (url == null) {
                    throw new IllegalStateException("DateTimeApi URL not found in cache");
                }

                // These keys ("apiKey", "loc") must match the {key} in the URL string
                Map<String, String> params = Map.of(
                        "apiKey", API_KEY,
                        "loc", city
                );

            // RestTemplate will now replace {apiKey} and {loc} in the URL with the values from the map
            DateTimeResponse r =restTemplate.getForObject(url, DateTimeResponse.class, params);
            redisService.set(redisKey,r,500L);
            log.info("getCurrentTime:{city} from database",city,r);
            return r;


        }

    }
