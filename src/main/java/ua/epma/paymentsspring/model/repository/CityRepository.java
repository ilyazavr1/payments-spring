package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epma.paymentsspring.model.entity.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    City getCityByCityName(String name);

    List<City> getCitiesByCountryIdCountryName(String countryName);


}
