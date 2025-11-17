package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.validation.Groups;

import java.time.LocalDate;

@Getter
@Setter
public class UserPayload {
    @Null(groups = Groups.Create.class, message = "id must be null on create")
    @NotNull(groups = Groups.Update.class, message = "id is required on update")
    @Min(groups = Groups.Update.class, value = 1, message = "id must be >= 1")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    private String lastName;

    @NotNull
    @Email
    @Size(max = 254)
    private String email;

    @Past(message = "birthDate must be in the past")
    private LocalDate birthDate;
}