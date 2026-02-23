package com.appswave.membership.web.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Response {


    private long responseCode = 200L;
    private boolean success = true;
    private Date currentDate = new Date();
    private String message;

    public Response() {}

    public Response(String message) {
        this.message = message;
    }
}
