package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class MovieInfoServiceTest {

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();
    MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    void movieInfoFlux() {
        var movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();
        StepVerifier.create(movieInfoFlux)
                //.expectNextCount(7)
                .assertNext(movieInfo ->
                        assertEquals("Batman Begins", movieInfo.getName())

                )
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void retrieveMovieInfoById_RestClient() {
        Long movieInfoId = 1L;
        var movieInfoFlux = movieInfoService.retrieveMovieInfoById_RestClient(movieInfoId);

        StepVerifier.create(movieInfoFlux)
                //.expectNextCount(7)
                .assertNext(movieInfo ->
                        assertEquals("Batman Begins", movieInfo.getName())
                )
                .verifyComplete();
    }
}