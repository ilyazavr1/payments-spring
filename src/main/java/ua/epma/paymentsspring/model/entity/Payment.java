package ua.epma.paymentsspring.model.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "payments")
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "balance_destination")
    private BigDecimal balanceDestination;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "is_send")
    private boolean isSend;

    @Column(name = "creation_timestamp")
    private LocalDateTime creationTimestamp;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_destination_id", nullable = false)
    private User userDestinationId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "card_sender_id", nullable = false)
    private Card cardSenderId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "card_destination_id", nullable = false)
    private Card cardDestinationId;

}
