package ua.epma.paymentsspring.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ua.epma.paymentsspring.model.entity.Address;

@Value
public class UserDtoWithAddress {

     public UserDtoWithAddress(String firstName) {
          this.firstName = firstName;
     }

     String firstName;






}
