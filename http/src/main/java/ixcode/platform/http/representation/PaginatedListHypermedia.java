package ixcode.platform.http.representation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

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
        super("list");
    }

    public PaginatedListHypermedia totalNumberOfItems(Integer totalNumberOfItems) {
        this.totalNumberOfItems = totalNumberOfItems;
        return this;
    }

    public PaginatedListHypermedia withFullList(List<?> items) {
        this.items.addAll(items);
        return this;
    }


    public PaginatedListHypermedia ofType(String... types) {
        for (String type : types) {
            ofType(type);
        }
        return this;
    }
    public PaginatedListHypermedia ofType(String type) {
        super.types.add(0, type);
        return this;
    }

    public PaginatedListHypermedia withPage(List<Hyperlink> pageOfUsers) {
        this.pageSize = pageOfUsers.size();
        this.items.addAll(pageOfUsers);
        return this;
    }

    public PaginatedListHypermedia startingAt(int startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    public PaginatedListHypermedia nextPage(Hyperlink nextPage) {
        this.next = nextPage;
        return this;
    }

    public PaginatedListHypermedia previousPage(Hyperlink previousPage) {
        this.previous = previousPage;
        return this;
    }


}