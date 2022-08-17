package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epma.paymentsspring.model.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {




}
