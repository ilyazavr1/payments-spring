package ua.epma.paymentsspring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.entity.Address;
import ua.epma.paymentsspring.model.repository.AddressRepository;
import ua.epma.paymentsspring.model.repository.CityRepository;
import ua.epma.paymentsspring.model.repository.CountryRepository;

@AllArgsConstructor
@Service
public class AddressService {

    private AddressRepository addressRepository;
    private CountryRepository countryRepository;
    private CityRepository cityRepository;


    public Address createAddress(String countryName, String cityName, String streetName, int buildingNumber) {
        return addressRepository.save(Address.builder().
                countryId(countryRepository.getCountryByCountryName(countryName)).
                cityId(cityRepository.getCityByCityName(cityName)).
                street(streetName).
                building(buildingNumber).
                build());
    }

}
