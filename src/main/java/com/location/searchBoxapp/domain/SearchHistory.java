package com.location.searchBoxapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "history")
public class SearchHistory {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "word")
    private String word;

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }


}
