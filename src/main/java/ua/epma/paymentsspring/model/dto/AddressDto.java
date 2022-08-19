package ua.epma.paymentsspring.model.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AddressDto {

    @Min(1)
    @Max(200)
    private int buildingNumber;

    @NotEmpty
    @Size(min = 3, max = 20, message = "to long")
    private String street;
//TODO clean up AddressDto

    private String countryName;

    @NotEmpty
    private String cityName;


}
