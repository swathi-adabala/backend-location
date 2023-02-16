package com.location.searchBoxapp.Controller;

import com.location.searchBoxapp.Repository.ElasticSearchQuery;
import com.location.searchBoxapp.domain.SearchHistory;
import com.location.searchBoxapp.domain.SearchHistoryDTO;
import com.location.searchBoxapp.util.LocationSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/location")
public class LocationSearchController {

    @Autowired
    ElasticSearchQuery elasticSearchQuery;

    @GetMapping("/searchResults")
    @CrossOrigin
    public ResponseEntity<Object> getSearchResults(@RequestParam String search) throws IOException {
        try {
            Set<String> res;
            res = LocationSearchUtil.getLocationSuggestions(search);
            List<SearchHistory> historyList = elasticSearchQuery.searchAllDocuments(search);
            for (SearchHistory history : historyList) {
                res.add(history.getWord());
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/searchResults")
    @CrossOrigin
    public ResponseEntity<Object> saveSearch(@RequestBody SearchHistoryDTO searchHistoryDTO) throws IOException {
        try {
            String response = elasticSearchQuery.createOrUpdateDocument(searchHistoryDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
