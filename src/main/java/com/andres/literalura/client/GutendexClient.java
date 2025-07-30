package com.andres.literalura.client;

import com.andres.literalura.dto.GutendexResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class GutendexClient {

    private final RestClient restClient;

    public GutendexClient(RestClient gutendexRestClient) {
        this.restClient = gutendexRestClient;
    }

    public GutendexResponseDTO searchBooksByTitle(String title) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/books/")
                            .queryParam("search", title)
                            .build())
                    .retrieve()
                    .body(GutendexResponseDTO.class);
        } catch (RestClientResponseException ex) {
            // Errores HTTP (4xx/5xx)
            throw new RuntimeException("Error HTTP " + ex.getStatusCode() + " al consultar Gutendex: " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            // Otros errores (conexión, parseo, etc.)
            throw new RuntimeException("Error al consultar Gutendex: " + ex.getMessage(), ex);
        }
    }
}
