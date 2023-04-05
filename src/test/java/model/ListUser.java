package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@lombok.Data
public class ListUser {
    private Integer page;
    @JsonProperty("per_page")
    private Integer perPage;
    private Integer total;
    @JsonProperty("total_pages")
    private Integer totalPages;
    private List<Data> data;
    private Support support;

}