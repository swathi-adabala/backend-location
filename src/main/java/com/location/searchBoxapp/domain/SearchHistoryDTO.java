package com.location.searchBoxapp.domain;

public class SearchHistoryDTO {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    private String id;
    private String word;

}
