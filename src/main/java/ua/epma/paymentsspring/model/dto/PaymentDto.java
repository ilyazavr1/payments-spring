package ua.epma.paymentsspring.model.dto;

import lombok.*;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentDto {




    private int balance;

    @NotNull
    @Max(10000)
    @Min(1)
    private Integer money;


    private boolean isSend;


    private LocalDateTime creationTimestamp;

    @NotEmpty
    @Size(min = 16, max = 16)
    @Pattern(regexp = "^[0-9]{16}$")
    private String cardSenderNumber;

    private Long cardSenderId;
    private Card cardSender;

    @NotEmpty
    @Size(min = 16, max = 16)
    @Pattern(regexp = "^[0-9]{16}$")
    private String cardDestinationNumber;

    private Long cardDestinationId;
    private Card cardDestination;
    private User userId;

    public void setCardDestinationNumber(String cardDestinationNumber) {
        this.cardDestinationNumber = cardDestinationNumber.replaceAll("[^0-9]+", "").trim();
    }
}
