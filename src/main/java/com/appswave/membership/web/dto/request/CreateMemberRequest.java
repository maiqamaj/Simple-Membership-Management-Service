package com.appswave.membership.web.dto.request;

import com.appswave.membership.enums.Gender;
import com.appswave.membership.enums.MembershipType;
import com.appswave.membership.enums.Persona;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateMemberRequest {
    @NotBlank(message = "{validation.firstName.required}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.required}")
    private String lastName;

    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "{validation.mobile.invalid}")
    private String mobileNumber;

    @NotNull(message = "{validation.gender.required}")
    private Gender gender;

    @NotNull(message = "{validation.membershipType.required}")
    private MembershipType membershipType;

    @NotNull(message = "{validation.persona.required}")
    private Persona persona;
}
