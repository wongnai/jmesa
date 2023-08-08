package com.bixuebihui.jmesa.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jmesa.limit.FilterSet;


//import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maxRows",
        "page",
        "exportType",
        "sort",
        "filter",
        "filterSet"
})
//@Generated("jsonschema2pojo")
public class Query implements Serializable {

    private final static long serialVersionUID = 8752072127684749495L;
    @JsonProperty("maxRows")
    private Long maxRows;
    @JsonProperty("page")
    private Long page;
    @JsonProperty("exportType")
    private String exportType;
    @JsonProperty("sort")
    private List<Sort> sort = new ArrayList<Sort>();
    @JsonProperty("filter")
    private List<Filter> filter = new ArrayList<Filter>();
    @JsonProperty("filterSet")
    private FilterSet filterSet;

    @JsonProperty("filterSet")
    public FilterSet getFilterSet() {
        return filterSet;
    }

    @JsonProperty("filterSet")
    public void setFilterSet(FilterSet filterSet) {
        this.filterSet = filterSet;
    }

    @JsonProperty("maxRows")
    public Long getMaxRows() {
        return maxRows;
    }

    @JsonProperty("maxRows")
    public void setMaxRows(Long maxRows) {
        this.maxRows = maxRows;
    }

    @JsonProperty("page")
    public Long getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Long page) {
        this.page = page;
    }

    @JsonProperty("exportType")
    public String getExportType() {
        return exportType;
    }

    @JsonProperty("exportType")
    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    @JsonProperty("sort")
    public List<Sort> getSort() {
        return sort;
    }

    @JsonProperty("sort")
    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }

    @JsonProperty("filter")
    public List<Filter> getFilter() {
        return filter;
    }

    @JsonProperty("filter")
    public void setFilter(List<Filter> filter) {
        this.filter = filter;
    }

}
