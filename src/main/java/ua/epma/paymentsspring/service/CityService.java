package ua.epma.paymentsspring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.entity.City;
import ua.epma.paymentsspring.model.repository.CityRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CityService {

    private CityRepository cityRepository;


    public List<City> getCitiesByCountryName(String countryName) {
        return cityRepository.getCitiesByCountryIdCountryName(countryName);
    }

}
