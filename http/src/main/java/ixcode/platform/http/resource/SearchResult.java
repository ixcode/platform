package ixcode.platform.http.resource;

import java.util.*;

public class SearchResult<T>  {
    private List<T> searchResults;

    public SearchResult(List<T> searchResults) {
        this.searchResults = searchResults;
    }

    public int totalNumberOfResults() {
        return searchResults.size();
    }

    public T firstResult() {
        return searchResults.get(0);
    }
}