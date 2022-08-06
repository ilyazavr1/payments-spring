package ua.epma.paymentsspring.model.dto;

import lombok.*;

import javax.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentDto {


    @NotNull
    @Max(10000)
    @Min(1)
    private Integer money;

    @NotEmpty
    @Size(min = 16, max = 16)
    @Pattern(regexp = "^[0-9]{16}$")
    private String cardSenderNumber;

    @NotEmpty
    @Size(min = 16, max = 16)
    @Pattern(regexp = "^[0-9]{16}$")
    private String cardDestinationNumber;

    public void setCardDestinationNumber(String cardDestinationNumber) {
        this.cardDestinationNumber = cardDestinationNumber.replaceAll("[^0-9]+", "").trim();
    }
}
