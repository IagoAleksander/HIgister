package com.iaz.HIgister.data.model.GoodReads;

import org.simpleframework.xml.Element;

/**
 * Created by alksander on 02/03/2018.
 */

public class Search {

//    @Element(required = false, name = "results-start")
//    private String results_start;
//
//    @Element(required = false, name = "total-results")
//    private String total_results;
//
//    @Element(required = false, name = "source")
//    private String source;

    @Element(required = false, name = "results")
    private Results results;

//    private String query;
//
//    @Element(required = false, name = "query-time-seconds")
//    private String query_time_seconds;
//
//    @Element(required = false, name = "results-end")
//    private String results_end;

//    public String getResults_start() {
//        return results_start;
//    }
//
//    public void setResults_start(String results_start) {
//        this.results_start = results_start;
//    }
//
//    public String getTotal_results() {
//        return total_results;
//    }
//
//    public void setTotal_results(String total_results) {
//        this.total_results = total_results;
//    }
//
//    public String getSource() {
//        return source;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }
//
//    public String getQuery() {
//        return query;
//    }
//
//    public void setQuery(String query) {
//        this.query = query;
//    }
//
//    public String getQuery_time_seconds() {
//        return query_time_seconds;
//    }
//
//    public void setQuery_time_seconds(String query_time_seconds) {
//        this.query_time_seconds = query_time_seconds;
//    }
//
//    public String getResults_end() {
//        return results_end;
//    }
//
//    public void setResults_end(String results_end) {
//        this.results_end = results_end;
//    }
}
