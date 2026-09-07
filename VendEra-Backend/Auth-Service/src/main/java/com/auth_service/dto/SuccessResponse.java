package com.auth_service.dto;



import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"message","data","status","timestamp"})
public class SuccessResponse {
	
	
	private String message;
    private Object data;
    private int status;
    private LocalDateTime timestamp;

    // Constructor 1 (No data)
    public SuccessResponse(String message, int status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Constructor 2 (With data)
    public SuccessResponse(String message, Object data, int status, LocalDateTime timestamp) {
        this.message = message;
        this.data = data;
        this.status = status;
        this.timestamp = timestamp;
    }

}



