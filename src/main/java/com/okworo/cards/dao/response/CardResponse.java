package com.okworo.cards.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Morris.Okworo on 26/08/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponse {

    private Long id;
    private String name;
    private String color;
    private String status;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Long createdBy;
    private Long updatedBy;

}
