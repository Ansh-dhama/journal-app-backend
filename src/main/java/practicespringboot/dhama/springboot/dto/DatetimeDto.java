package practicespringboot.dhama.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class DatetimeDto {
    private String userName;
    private String datetime;
    private String timezone_name;
    private String timezone_location;
    private String requested_location;
    private double latitude;
    private double longitude;
    private double temp;
    private double windSpeed;
}
