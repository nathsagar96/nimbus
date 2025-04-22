package dev.sagar.nimbus.dto.owm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocodingResponse {
  private String name;

  @JsonProperty("local_names")
  private LocalNames localNames;

  private double lat;
  private double lon;
  private String country;
  private String state;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LocalNames {
    private String en;
  }
}
