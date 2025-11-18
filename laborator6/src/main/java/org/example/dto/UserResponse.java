package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
}
