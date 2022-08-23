package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.epma.paymentsspring.model.dto.UserWithAddressDto;
import ua.epma.paymentsspring.model.entity.UserAddress;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @Query(value = "SELECT new ua.epma.paymentsspring.model.dto.UserWithAddressDto(" +
            "uadr.userId.id, uadr.userId.firstName, uadr.userId.lastName,uadr.userId.patronymic," +
            "uadr.userId.email,uadr.userId.blocked,uadr.userId.role, uadr.addressId)" +
            " FROM UserAddress uadr WHERE uadr.userId.role = (SELECT r FROM Role r WHERE r.roleEnum = 'CLIENT') ")
    List<UserWithAddressDto> getUsersNotAdminWithAddress();



}
