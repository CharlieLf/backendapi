package com.example.backendapi.suggestions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/suggestions", "/suggestions/"})
public class SuggestionController {

    private final SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping
    public ResponseEntity<List<Suggestion>> getSuggestions(
            @RequestParam("q") String query,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude) {
        List<Suggestion> suggestions = suggestionService.getSuggestions(query, latitude, longitude);
        return ResponseEntity.ok(suggestions);
    }

}
