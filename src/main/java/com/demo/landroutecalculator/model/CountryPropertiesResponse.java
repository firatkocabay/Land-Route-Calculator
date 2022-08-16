package com.demo.landroutecalculator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryPropertiesResponse {

    @JsonProperty("cca3")
    private String countryName;

    @JsonProperty("borders")
    private List<String> countryBorders;

    @JsonProperty("latlng")
    private List<Double> latitudeLongitude;

}
