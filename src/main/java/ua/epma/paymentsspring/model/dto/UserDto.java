package ua.epma.paymentsspring.model.dto;


import lombok.*;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {


    @NotEmpty
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^(\\p{L}){1}( |[\\p{L}]){2,20}$")
    private String firstName;

    @NotEmpty
    @Size(min = 3, max = 20, message = "to long")
    @Pattern(regexp = "^(\\p{L}){1}( |[\\p{L}]){2,20}$")
    private String lastName;

    @NotEmpty
    @Size(min = 3, max = 20, message = "to long")
    @Pattern(regexp = "^(\\p{L}){1}( |[\\p{L}]){2,20}$")
    private String patronymic;

    @NotEmpty
    @Size(max = 30)
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
    private String password;


    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic.trim();
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }
}
