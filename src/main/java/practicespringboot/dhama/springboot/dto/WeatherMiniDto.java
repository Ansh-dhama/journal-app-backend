package practicespringboot.dhama.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherMiniDto {
    private String username;
    private String location;
    private double tempC;
    private double windKph;
}
