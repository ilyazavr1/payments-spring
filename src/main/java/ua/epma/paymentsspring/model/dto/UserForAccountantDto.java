package ua.epma.paymentsspring.model.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import ua.epma.paymentsspring.model.entity.Role;

@Value
@AllArgsConstructor
public class UserForAccountantDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
    private  boolean blocked;
    private  Role role;


}
