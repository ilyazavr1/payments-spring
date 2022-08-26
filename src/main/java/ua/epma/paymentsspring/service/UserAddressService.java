package ua.epma.paymentsspring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.dto.AddressDto;
import ua.epma.paymentsspring.model.entity.Address;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.entity.UserAddress;
import ua.epma.paymentsspring.model.repository.UserAddressRepository;

@AllArgsConstructor
@Service
public class UserAddressService {

    private UserAddressRepository userAddressRepository;
    private AddressService addressService;

    public void addAddressToUser(AddressDto addressDto, User user) {
        Address address = addressService.createAddress(
                addressDto.getCountryName(),
                addressDto.getCityName(),
                addressDto.getStreet(),
                addressDto.getBuildingNumber()
        );
        userAddressRepository.save(UserAddress.builder().addressId(address).userId(user).build());
    }


}
