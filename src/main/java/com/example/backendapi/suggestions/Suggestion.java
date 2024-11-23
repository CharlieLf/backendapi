package com.example.backendapi.suggestions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Suggestion {

    private String name;
    private double latitude;
    private double longitude;
    private double score;

}
