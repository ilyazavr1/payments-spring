package ua.epma.paymentsspring.model.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "card_unblock_requests")
public class CardUnblockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card_id;

}