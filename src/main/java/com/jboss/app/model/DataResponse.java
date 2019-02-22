package com.jboss.app.model;

public class DataResponse<T> {
    private final T result;
    //private final long lastUpdated;
    private final String lastUpdate;
    

    public DataResponse(T result, String lastUpdate) {
        this.result = result;
        //this.lastUpdated = lastUpdated;
        this.lastUpdate = lastUpdate;
    }

    /*public DataResponse(T result, long lastUpdated,String reportUrl ) {
        this.result = result;
        this.lastUpdated = lastUpdated;
        this.reportUrl = reportUrl;
    }*/

    public T getResult() {
        return result;
    }

    /*public long getLastUpdated() {
        return lastUpdated;
    }*/

    public String getLastUpdate() {
        return lastUpdate;
    }
   /* public String getReportUrl() {
        return reportUrl;
    }*/

}
