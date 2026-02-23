package com.appswave.membership.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MemberResponse extends Response implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String gender;
    private String membershipType;
    private String persona;
}
