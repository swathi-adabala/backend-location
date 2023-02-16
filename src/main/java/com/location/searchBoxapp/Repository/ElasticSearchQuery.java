package com.location.searchBoxapp.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.location.searchBoxapp.domain.SearchHistory;
import com.location.searchBoxapp.domain.SearchHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ElasticSearchQuery {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "history";


    public String createOrUpdateDocument(SearchHistoryDTO searchHistoryDTO) throws IOException {
        searchHistoryDTO.setId(searchHistoryDTO.getWord().toLowerCase());
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(searchHistoryDTO.getId())
                .document(searchHistoryDTO)
        );
        if(response.result().name().equals("Created")){
            return new StringBuilder("Document has been successfully created.").toString();
        }else if(response.result().name().equals("Updated")){
            return new StringBuilder("Document has been successfully updated.").toString();
        }
        return new StringBuilder("Error while performing the operation.").toString();
    }

    public List<SearchHistory> searchAllDocuments(String searchWord) throws IOException {

        MatchQuery matchQuery = new MatchQuery.Builder()
                .field("word")
                .query(searchWord)
                .build();
        Query q = new Query.Builder()
                .match(matchQuery)
                .build();
        SearchRequest searchRequest =  new SearchRequest.Builder()
                .index(indexName)
                .query(q)
                .size(15)
                .build();

        List<SearchHistory> searchHistories = new ArrayList<>();
        try {
            SearchResponse searchResponse = elasticsearchClient.search(searchRequest, SearchHistory.class);
            List<Hit> hits = searchResponse.hits().hits();
            for (Hit object : hits) {
                searchHistories.add((SearchHistory) object.source());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return searchHistories;
    }
}
