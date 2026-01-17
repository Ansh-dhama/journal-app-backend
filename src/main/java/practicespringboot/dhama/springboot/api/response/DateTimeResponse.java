package practicespringboot.dhama.springboot.api.response;

import lombok.Data;

@Data
public class DateTimeResponse {

    private String datetime;
    private String timezone_name;
    private String timezone_location;
    private String requested_location;
    private double latitude;
    private double longitude;
}
