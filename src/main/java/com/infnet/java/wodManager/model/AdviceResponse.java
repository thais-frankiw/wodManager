package com.infnet.java.wodManager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class AdviceResponse {

    @JsonProperty("slip")
    private AdviceSlip slip;

    // Getters e setters
    public AdviceSlip getSlip() {
        return slip;
    }

    public void setSlip(AdviceSlip slip) {
        this.slip = slip;
    }


    public static class AdviceSlip {
        private int id;
        private String advice;

        // Getters e setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }
    }
}