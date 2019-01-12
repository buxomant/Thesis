package com.cbp.app.model.response;

public class WebsiteStatisticsResponse {
    private Integer numberOfDomesticWebsites;
    private Integer numberOfForeignWebsites;
    private Integer numberOfRedirectToForeignWebsites;
    private Integer numberOfIndexingServiceWebsites;
    private Integer numberOfNewsWebsites;
    private Integer numberOfSocialMediaWebsites;
    private Integer numberOfUncategorizedWebsites;
    private Integer numberOfTopDomains;
    private Integer numberOfSubDomains;

    public WebsiteStatisticsResponse() { }

    public WebsiteStatisticsResponse(
        Integer numberOfDomesticWebsites,
        Integer numberOfForeignWebsites,
        Integer numberOfRedirectToForeignWebsites,
        Integer numberOfIndexingServiceWebsites,
        Integer numberOfNewsWebsites,
        Integer numberOfSocialMediaWebsites,
        Integer numberOfUncategorizedWebsites,
        Integer numberOfTopDomains,
        Integer numberOfSubDomains
    ) {
        this.numberOfDomesticWebsites = numberOfDomesticWebsites;
        this.numberOfForeignWebsites = numberOfForeignWebsites;
        this.numberOfRedirectToForeignWebsites = numberOfRedirectToForeignWebsites;
        this.numberOfIndexingServiceWebsites = numberOfIndexingServiceWebsites;
        this.numberOfNewsWebsites = numberOfNewsWebsites;
        this.numberOfSocialMediaWebsites = numberOfSocialMediaWebsites;
        this.numberOfUncategorizedWebsites = numberOfUncategorizedWebsites;
        this.numberOfTopDomains = numberOfTopDomains;
        this.numberOfSubDomains = numberOfSubDomains;
    }

    public Integer getNumberOfDomesticWebsites() {
        return numberOfDomesticWebsites;
    }

    public void setNumberOfDomesticWebsites(Integer numberOfDomesticWebsites) {
        this.numberOfDomesticWebsites = numberOfDomesticWebsites;
    }

    public Integer getNumberOfForeignWebsites() {
        return numberOfForeignWebsites;
    }

    public void setNumberOfForeignWebsites(Integer numberOfForeignWebsites) {
        this.numberOfForeignWebsites = numberOfForeignWebsites;
    }

    public Integer getNumberOfRedirectToForeignWebsites() {
        return numberOfRedirectToForeignWebsites;
    }

    public void setNumberOfRedirectToForeignWebsites(Integer numberOfRedirectToForeignWebsites) {
        this.numberOfRedirectToForeignWebsites = numberOfRedirectToForeignWebsites;
    }

    public Integer getNumberOfIndexingServiceWebsites() {
        return numberOfIndexingServiceWebsites;
    }

    public void setNumberOfIndexingServiceWebsites(Integer numberOfIndexingServiceWebsites) {
        this.numberOfIndexingServiceWebsites = numberOfIndexingServiceWebsites;
    }

    public Integer getNumberOfNewsWebsites() {
        return numberOfNewsWebsites;
    }

    public void setNumberOfNewsWebsites(Integer numberOfNewsWebsites) {
        this.numberOfNewsWebsites = numberOfNewsWebsites;
    }

    public Integer getNumberOfSocialMediaWebsites() {
        return numberOfSocialMediaWebsites;
    }

    public void setNumberOfSocialMediaWebsites(Integer numberOfSocialMediaWebsites) {
        this.numberOfSocialMediaWebsites = numberOfSocialMediaWebsites;
    }

    public Integer getNumberOfUncategorizedWebsites() {
        return numberOfUncategorizedWebsites;
    }

    public void setNumberOfUncategorizedWebsites(Integer numberOfUncategorizedWebsites) {
        this.numberOfUncategorizedWebsites = numberOfUncategorizedWebsites;
    }

    public Integer getNumberOfTopDomains() {
        return numberOfTopDomains;
    }

    public void setNumberOfTopDomains(Integer numberOfTopDomains) {
        this.numberOfTopDomains = numberOfTopDomains;
    }

    public Integer getNumberOfSubDomains() {
        return numberOfSubDomains;
    }

    public void setNumberOfSubDomains(Integer numberOfSubDomains) {
        this.numberOfSubDomains = numberOfSubDomains;
    }
}
