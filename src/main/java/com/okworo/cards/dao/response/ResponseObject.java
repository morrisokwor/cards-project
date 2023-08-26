package com.okworo.cards.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Morris.Okworo on 26/08/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject {
    private Boolean success;
    private String message;
    private Object data;
}
