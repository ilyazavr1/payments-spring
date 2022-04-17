package ua.epma.paymentsspring.model.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CardDto {

    @NotEmpty
    @Size(min = 1, max = 30)
    private String name;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{16}$")
    private String number;


    private int money;


    private boolean blocked;


    private boolean underConsideration;



}
