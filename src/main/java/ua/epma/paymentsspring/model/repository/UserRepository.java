package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.epma.paymentsspring.model.dto.UserTest;
import ua.epma.paymentsspring.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);


    User findByEmail(String email);

    //@Query("SELECT u FROM User u where u.role = (SELECT r FROM Role r WHERE r.roleEnum = 'CLIENT')")
    @Query("SELECT u FROM User u where u.role.roleEnum = 'CLIENT'")
    List<User> getUsersWithRoleClient();



    @Query(value = "SELECT u.firstName as firstName, ua.addressId as addressId FROM User u INNER join UserAddress ua on ua.userId = u")
    List<UserTest> findUsersTest();




}
