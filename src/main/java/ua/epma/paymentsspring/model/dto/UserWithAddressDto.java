package ua.epma.paymentsspring.model.dto;

import lombok.Value;
import ua.epma.paymentsspring.model.entity.Address;
import ua.epma.paymentsspring.model.entity.Role;

@Value
public class UserWithAddressDto {

    Long id;
    String firstName;
    String lastName;
    String patronymic;
    String email;
    boolean blocked;
    Role role;
    Address addressId;

    public UserWithAddressDto(Long id, String firstName, String lastName, String patronymic, String email, boolean blocked, Role role, Address addressId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.blocked = blocked;
        this.role = role;
        this.addressId = addressId;
    }
}
