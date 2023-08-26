package com.okworo.cards.dao.request;

import com.okworo.cards.enums.SortOrder;
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
public class CardSearchModel {
    private Long id;
    private String name;
    private String color;
    private String status;
    private Date createdAt;
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private SortOrder sortOrder;

}
