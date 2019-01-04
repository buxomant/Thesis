package com.cbp.app.model.response;

public class CrawlerStatisticsResponse {
    private Integer numberOfWebsites;
    private Integer numberOfDuplicateWebsites;
    private Integer numberOfCheckedWebsites;
    private Integer numberOfProcessedWebsites;
    private Integer numberOfWebsitesWithErrors;
    private Integer numberOfPages;
    private Integer numberOfLinks;

    public CrawlerStatisticsResponse() { }

    public CrawlerStatisticsResponse(
        Integer numberOfWebsites,
        Integer numberOfCheckedWebsites,
        Integer numberOfProcessedWebsites,
        Integer numberOfWebsitesWithErrors,
        Integer numberOfDuplicateWebsites,
        Integer numberOfPages,
        Integer numberOfLinks
    ) {
        this.numberOfWebsites = numberOfWebsites;
        this.numberOfCheckedWebsites = numberOfCheckedWebsites;
        this.numberOfProcessedWebsites = numberOfProcessedWebsites;
        this.numberOfWebsitesWithErrors = numberOfWebsitesWithErrors;
        this.numberOfDuplicateWebsites = numberOfDuplicateWebsites;
        this.numberOfPages = numberOfPages;
        this.numberOfLinks = numberOfLinks;
    }

    public Integer getNumberOfWebsites() {
        return numberOfWebsites;
    }

    public void setNumberOfWebsites(Integer numberOfWebsites) {
        this.numberOfWebsites = numberOfWebsites;
    }

    public Integer getNumberOfCheckedWebsites() {
        return numberOfCheckedWebsites;
    }

    public void setNumberOfCheckedWebsites(Integer numberOfCheckedWebsites) {
        this.numberOfCheckedWebsites = numberOfCheckedWebsites;
    }

    public Integer getNumberOfProcessedWebsites() {
        return numberOfProcessedWebsites;
    }

    public void setNumberOfProcessedWebsites(Integer numberOfProcessedWebsites) {
        this.numberOfProcessedWebsites = numberOfProcessedWebsites;
    }

    public Integer getNumberOfWebsitesWithErrors() {
        return numberOfWebsitesWithErrors;
    }

    public void setNumberOfWebsitesWithErrors(Integer numberOfWebsitesWithErrors) {
        this.numberOfWebsitesWithErrors = numberOfWebsitesWithErrors;
    }

    public Integer getNumberOfDuplicateWebsites() {
        return numberOfDuplicateWebsites;
    }

    public void setNumberOfDuplicateWebsites(Integer numberOfDuplicateWebsites) {
        this.numberOfDuplicateWebsites = numberOfDuplicateWebsites;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Integer getNumberOfLinks() {
        return numberOfLinks;
    }

    public void setNumberOfLinks(Integer numberOfLinks) {
        this.numberOfLinks = numberOfLinks;
    }
}
