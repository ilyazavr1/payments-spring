package ua.epma.paymentsspring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.entity.City;
import ua.epma.paymentsspring.model.entity.Country;
import ua.epma.paymentsspring.model.repository.CityRepository;
import ua.epma.paymentsspring.model.repository.CountryRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CityService {

   CityRepository cityRepository;

   public List<City> getAlsCities(){
       return cityRepository.findAll();
   };

   public List<City> getCitiesByCountryName(String countryName){
       return cityRepository.getCitiesByCountryIdCountryName(countryName);
    }

}
