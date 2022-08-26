package ua.epma.paymentsspring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.entity.Country;
import ua.epma.paymentsspring.model.repository.CountryRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CountryService {

    private CountryRepository countryRepository;

    public List<Country> getCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryByName(String name) {
        return countryRepository.getCountryByCountryName(name);
    }


}
