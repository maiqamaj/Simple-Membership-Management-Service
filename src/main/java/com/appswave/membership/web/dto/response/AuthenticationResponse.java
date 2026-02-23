package com.appswave.membership.web.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthenticationResponse extends Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String firstName;
    private String lastName;
    private String role;

}