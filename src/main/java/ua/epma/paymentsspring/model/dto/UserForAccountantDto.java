package ua.epma.paymentsspring.model.dto;

import lombok.Value;
import ua.epma.paymentsspring.model.entity.Role;

@Value
public class UserForAccountantDto {

    Long id;
    String firstName;
    String lastName;
    String patronymic;
    String email;
    boolean blocked;
    Role role;


    public UserForAccountantDto(Long id, String firstName, String lastName, String patronymic, String email, boolean blocked, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.blocked = blocked;
        this.role = role;
    }

}
