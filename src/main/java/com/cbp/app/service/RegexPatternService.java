package com.cbp.app.service;

import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class RegexPatternService {
    private final Pattern urlMissingWwwPattern;
    private final Pattern globalLinkPattern;
    private final Pattern localLinkPattern;
    private final Pattern localPageLinkPattern;
    private final Pattern queryStringPattern;
    private final Pattern anchorStringPattern;
    private final Pattern alphanumericContentStartPattern;
    private final Pattern subPagePattern;
    private final Pattern nonWebResourcePattern;
    private final Pattern nonWebProtocolPattern;
    private final Pattern domesticWebsitePattern;
    private final Pattern domesticNewsWebsitePattern;
    private final Pattern socialMediaWebsitePattern;
    private final Pattern indexingServicePattern;

    public RegexPatternService() {
        this.urlMissingWwwPattern = Pattern.compile("^\\w+\\.\\w+");
        this.globalLinkPattern = Pattern.compile("^\\/{2}.+");
        this.localLinkPattern = Pattern.compile("^[^\\.]*\\/.+");
        this.localPageLinkPattern = Pattern.compile("[^\\.]*\\.(?:" +
            "htm" +
            "|html" +
            "|xhtml" +
            "|phtml" +
            "|php" +
            "|asp" +
            "|aspx" +
            "|jsp" +
            ")$");
        this.queryStringPattern = Pattern.compile("(.*)\\?.*");
        this.anchorStringPattern = Pattern.compile("(.*)#.*");
        this.alphanumericContentStartPattern = Pattern.compile("^([\\W_]*)|([\\W_]*$)");
        this.subPagePattern = Pattern.compile("(.*?\\.\\w+)/.*");
        this.nonWebResourcePattern = Pattern.compile(".*\\.(?:" +
            "bmp" +
            "|jpg" +
            "|jpeg" +
            "|png" +
            "|gif" +
            "|svg" +
            "|pdf" +
            "|doc" +
            "|docx" +
            "|xls" +
            "|xlsx" +
            "|ppt" +
            "|pptx" +
            "|ashx" +
            "|xml" +
            "|m3u" +
            "|pls" +
            "|zip" +
            "|rar" +
            "|rtf" +
            "|ods" +
            "|avi" +
            "|mp3" +
            "|rss" +
            "|atom" +
            "|txt" +
            "|tif" +
            "|exe" +
            ")$");
        this.nonWebProtocolPattern = Pattern.compile("^(" +
            "mailto" +
            "|mail" +
            "|skype" +
            "|whatsapp" +
            "|javascript" +
            "|file" +
            "|data" +
            "|mms" +
            "|ts3server" +
            "|steam" +
            "|dchub" +
            "|tel" +
            "|fax" +
            "|webcal" +
            "|callto" +
            "|viber" +
            "|samp" +
            "|ymsgr" +
            "|irc" +
            "|nav" +
            "):.*");
        this.domesticWebsitePattern = Pattern.compile(".+\\.ro.*");
        this.domesticNewsWebsitePattern = Pattern.compile(
            "(?:.*\\.)?adevarul\\.ro.*" +
            "|(?:.*\\.)?stirileprotv\\.ro.*" +
            "|(?:.*\\.)?libertatea\\.ro.*" +
            "|(?:.*\\.)?digi24\\.ro.*" +
            "|(?:.*\\.)?a1\\.ro.*" +
            "|(?:.*\\.)?antena3\\.ro.*" +
            "|(?:.*\\.)?cancan\\.ro.*" +
            "|(?:.*\\.)?realitatea\\.net.*" +
            "|(?:.*\\.)?romaniatv\\.net.*" +
            "|(?:.*\\.)?unica\\.net.*" +
            "|(?:.*\\.)?evz\\.ro.*" +
            "|(?:.*\\.)?gsp\\.ro.*" +
            "|(?:.*\\.)?click\\.ro.*" +
            "|(?:.*\\.)?csid\\.ro.*" +
            "|(?:.*\\.)?sfatulmedicului\\.ro.*" +
            "|(?:.*\\.)?digisport\\.ro.*" +
            "|(?:.*\\.)?ziare\\.com.*" +
            "|(?:.*\\.)?sport\\.ro.*" +
            "|(?:.*\\.)?stiripesurse\\.ro.*" +
            "|(?:.*\\.)?hotnews\\.ro.*" +
            "|(?:.*\\.)?mediafax\\.ro.*" +
            "|(?:.*\\.)?spynews\\.ro.*" +
            "|(?:.*\\.)?avocatnet\\.ro.*" +
            "|(?:.*\\.)?teotrandafir\\.com.*" +
            "|(?:.*\\.)?wowbiz\\.ro.*" +
            "|(?:.*\\.)?protv\\.ro.*" +
            "|(?:.*\\.)?zf\\.ro.*" +
            "|(?:.*\\.)?prosport\\.ro.*" +
            "|(?:.*\\.)?unica\\.ro.*" +
            "|(?:.*\\.)?kudika\\.ro.*" +
            "|(?:.*\\.)?ziaruldeiasi\\.ro.*" +
            "|(?:.*\\.)?gandul\\.ro.*" +
            "|(?:.*\\.)?gandul\\.info.*"
        );
        this.socialMediaWebsitePattern = Pattern.compile(
            "(?:.*\\.)?facebook\\.com.*" +
            "|(?:.*\\.)?fb\\.com.*" +
            "|(?:.*\\.)?twitter\\.com.*" +
            "|(?:.*\\.)?instagram\\.com.*" +
            "|(?:.*\\.)?last\\.fm.*" +
            "|(?:.*\\.)?pinterest\\.com.*" +
            "|(?:.*\\.)?linkedin\\.com.*" +
            "|(?:.*\\.)?youtube\\.com.*"
        );
        this.indexingServicePattern = Pattern.compile(
            ".*google\\.com.*" +
            "^google\\..*" +
            "|.*alexa\\.com.*" +
            "|.*apple\\.com.*" +
            "|.*blogger\\.com.*" +
            "|.*trustpilot\\.com.*" +
            "|.*wordpress\\.com.*" +
            "|.*outlook\\.com.*" +
            "|.*blogspot\\.com.*" +
            "|.*blogspot\\.ro.*" +
            "|.*pe-harta\\.ro.*" +
            "|.*nym\\.ro.*" +
            "|.*archive\\.org.*" +
            "|.*creativecommons\\.org.*" +
            "|.*webstatsdomain\\.org.*" +
            "|.*webstatsdomain\\.com.*" +
            "|.*gov\\.uk.*"
        );
    }

    public Pattern getUrlMissingWwwPattern() {
        return urlMissingWwwPattern;
    }

    public Pattern getGlobalLinkPattern() {
        return globalLinkPattern;
    }

    public Pattern getLocalLinkPattern() {
        return localLinkPattern;
    }

    public Pattern getLocalPageLinkPattern() {
        return localPageLinkPattern;
    }

    public Pattern getQueryStringPattern() {
        return queryStringPattern;
    }

    public Pattern getAnchorStringPattern() {
        return anchorStringPattern;
    }

    public Pattern getAlphanumericContentPattern() {
        return alphanumericContentStartPattern;
    }

    public Pattern getSubPagePattern() {
        return subPagePattern;
    }

    public Pattern getNonWebResourcePattern() {
        return nonWebResourcePattern;
    }

    public Pattern getNonWebProtocolPattern() {
        return nonWebProtocolPattern;
    }

    public Pattern getDomesticWebsitePattern() {
        return domesticWebsitePattern;
    }

    public Pattern getDomesticNewsWebsitePattern() {
        return domesticNewsWebsitePattern;
    }

    public Pattern getSocialMediaWebsitePattern() {
        return socialMediaWebsitePattern;
    }

    public Pattern getIndexingServicePattern() {
        return indexingServicePattern;
    }
}
