package com.example.backendapi.city;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CityLoader {
    private final List<City> cities = new ArrayList<>();
    private final ResourceLoader resourceLoader;
    private long maxPopulation = 1;

    public CityLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void loadCity() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:data.geo/cities_canada-usa.tsv");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                City newCity = new City(
                        fields[1], // CityName
                        Double.parseDouble(fields[4]), // Lat
                        Double.parseDouble(fields[5]), // Long
                        Long.parseLong(fields[14]), // Population
                        fields[10], // Admin1Code
                        fields[8] // CountryCode

                );
                cities.add(newCity);
                if(newCity.getPopulation() > maxPopulation) maxPopulation = newCity.getPopulation();
            }
        }
    }

    public List<City> getCities(){
        return cities;
    }

    public long getMaxPopulation(){
        return maxPopulation;
    }

}
