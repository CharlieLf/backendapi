package com.example.backendapi.suggestions;

import com.example.backendapi.city.City;
import com.example.backendapi.city.CityLoader;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {

    private final CityLoader cityLoader;

    public SuggestionService(CityLoader cityLoader){
        this.cityLoader = cityLoader;
    }

    public List<Suggestion> getSuggestions(String query, Double latitude, Double longitude) {
        return cityLoader.getCities().stream()
                .filter(city -> city.getName().toLowerCase().contains(query.toLowerCase()))
                .map(city -> new Suggestion(
                        city.getName() + ", " + city.getAdmin1Code() + ", " + city.getCountryCode(),
                        city.getLatitude(),
                        city.getLongitude(),
                        calculateScore(city, latitude, longitude)
                ))
                .sorted(Comparator.comparingDouble(Suggestion::getScore).reversed())
                .collect(Collectors.toList());
    }

    private double calculateScore(City city, Double latitude, Double longitude) {
        double distanceScore = 0.0;

        if (latitude != null && longitude != null) {
            double distance = haversine(city.getLatitude(), city.getLongitude(), latitude, longitude);
            if(distance == 0)distanceScore = 1;
            else if (distance < 50) {
                distanceScore = 0.8 + 0.2 * (1 - (distance / 50.0));
            }
            else if (distance < 100) {
                distanceScore = 0.6 + 0.2 * (1 - ((distance - 50) / 50.0));
            }
            else{
                distanceScore = 0.6 * Math.exp(-0.01 * (distance - 100));
            }


            return Math.round((0.4 + (0.6 *  distanceScore)) * 10.0) / 10.0;
        }
        else {
            double populationScore = (double)city.getPopulation() / cityLoader.getMaxPopulation();
            return Math.round((0.6 + (0.4 * populationScore)) * 10.0) / 10.0;
        }
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double rad = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) *
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return rad * c;
    }

}
