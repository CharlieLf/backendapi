package com.example.backendapi.city;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class City {

    private String name;
    private double latitude;
    private double longitude;
    private long population;
    private String admin1Code;
    private String countryCode;

}
