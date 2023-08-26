package com.okworo.cards.dao.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Morris.Okworo on 26/08/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequest {

    private Long id;
    @NotEmpty(message = "Name should not be null")
    private String name;
    private String color;
    private String status;
    private String description;


}
