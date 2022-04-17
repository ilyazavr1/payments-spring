package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.epma.paymentsspring.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Role findByRoleEnum(Role.RoleEnum roleEnum);


}
