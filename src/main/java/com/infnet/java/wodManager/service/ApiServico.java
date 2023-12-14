package com.infnet.java.wodManager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.java.wodManager.model.AdviceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiServico {
    private static final Logger logger = LoggerFactory.getLogger(ApiServico.class);
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public ApiServico(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public String getAdvice() {
        String url = "https://api.adviceslip.com/advice";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        logger.info("Status code da API de frases: " + responseEntity.getStatusCode());

        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            logger.error("Falha no retorno da frase");
            return "Frase não encontrada";
        }
        try {
            AdviceResponse adviceResponse = objectMapper.readValue(responseEntity.getBody(), AdviceResponse.class);
            return adviceResponse.getSlip().getAdvice();
        } catch (Exception e) {
            logger.error("Não foi possível mapear a resposta: ", e);
            return "Frase não encontrada";
        }
    }
}


