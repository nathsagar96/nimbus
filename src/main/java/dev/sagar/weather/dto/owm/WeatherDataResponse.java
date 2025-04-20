package dev.sagar.weather.dto.owm;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataResponse {
  private Coord coord;
  private List<Weather> weather;
  private String base;
  private Main main;
  private int visibility;
  private Wind wind;
  private Clouds clouds;
  private Rain rain;
  private Snow snow;
  private long dt;
  private Sys sys;
  private int timezone;
  private long id;
  private String name;
  private int cod;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Coord {
    private double lon;
    private double lat;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Main {
    private double temp;

    @JsonProperty("feels_like")
    private double feelsLike;

    @JsonProperty("temp_min")
    private double tempMin;

    @JsonProperty("temp_max")
    private double tempMax;

    private int pressure;
    private int humidity;

    @JsonProperty("sea_level")
    private Integer seaLevel;

    @JsonProperty("grnd_level")
    private Integer grndLevel;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Wind {
    private double speed;
    private int deg;
    private Double gust;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Clouds {
    private int all;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Rain {
    @JsonProperty("1h")
    private Double oneHour;

    @JsonProperty("3h")
    private Double threeHour;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Snow {
    @JsonProperty("1h")
    private Double oneHour;

    @JsonProperty("3h")
    private Double threeHour;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Sys {
    private Integer type;
    private Integer id;
    private String country;
    private long sunrise;
    private long sunset;
  }
}
