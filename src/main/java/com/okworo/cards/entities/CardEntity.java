package com.okworo.cards.entities;

import com.okworo.cards.enums.CardStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * @author Morris.Okworo on 26/08/2023
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "card")
public class CardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_gen")
    @SequenceGenerator(name = "card_gen", sequenceName = "card_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    private String name;
    private String color;
    @Enumerated(EnumType.STRING)
    private CardStatus status;
    private String description;

}
