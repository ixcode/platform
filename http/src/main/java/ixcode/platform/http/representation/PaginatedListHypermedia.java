package ixcode.platform.http.representation;

import java.util.ArrayList;
import java.util.List;

public class PaginatedListHypermedia extends HypermediaResourceBuilder<PaginatedListHypermedia> {
    private Integer totalNumberOfItems;
    private Integer startIndex;
    private Integer pageSize;


    private Hyperlink previous;
    private Hyperlink next;

    private List<Object> items = new ArrayList<Object>();

    public static PaginatedListHypermedia paginatedListHypermedia() {
        return new PaginatedListHypermedia();
    }

    public PaginatedListHypermedia() {
        super("paginated-list");
    }

    public PaginatedListHypermedia totalNumberOfItems(Integer totalNumberOfItems) {
        this.totalNumberOfItems = totalNumberOfItems;
        return this;
    }

    public PaginatedListHypermedia withFullList(List<?> items) {
        totalNumberOfItems = items.size();
        this.items.addAll(items);
        return this;
    }
}