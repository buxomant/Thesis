package com.cbp.app.client;

import com.cbp.app.model.db.GoogleSearch;
import com.cbp.app.model.response.GoogleSearch.GoogleSearchResponse;
import com.cbp.app.repository.GoogleSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class GoogleSearchClient {

    private final RestTemplate restTemplate;
    private final GoogleSearchRepository googleSearchRepository;
    private final String googleCustomSearchUrl;

    @Autowired
    public GoogleSearchClient(
        RestTemplateBuilder restTemplateBuilder,
        GoogleSearchRepository googleSearchRepository,
        @Value("${google-custom-search.url}") String googleCustomSearchUrl
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.googleSearchRepository = googleSearchRepository;
        this.googleCustomSearchUrl = googleCustomSearchUrl;
    }

    public GoogleSearchResponse fetchNextSearchResults(String nextQueryPhrase, int nextStartIndex) {
        String url = googleCustomSearchUrl + String.format("&q=%s&start=%s", nextQueryPhrase, nextStartIndex);

        try {
            HttpEntity httpEntity = new HttpEntity(new HttpHeaders());
            return exchange(url, HttpMethod.GET, httpEntity, GoogleSearchResponse.class).getBody();
        } catch (HttpStatusCodeException e) {
            // do nothing (yet)
            throw e;
        }
    }

    public<T> ResponseEntity<T> exchange(String url, HttpMethod httpMethod, HttpEntity<?> requestEntity, Class<T> responseType) {
        return restTemplate.exchange(url, httpMethod, requestEntity, responseType);
    }
}
