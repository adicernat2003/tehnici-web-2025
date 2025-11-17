package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
}
