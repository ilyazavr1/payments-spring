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
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "number")
    private String number;

    @Column(name = "money")
    private int money;

    @Column(name = "blocked")
    private boolean blocked;

    @Column(name = "under_consideration")
    private boolean underConsideration;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    /*@OneToMany(mappedBy = "cardSenderId", fetch = FetchType.EAGER)
    private Set<Payment> paymentsCardSender;

    @OneToMany(mappedBy = "cardDestinationId", fetch = FetchType.EAGER)
    private Set<Payment> paymentsCardDestination;*/


}
