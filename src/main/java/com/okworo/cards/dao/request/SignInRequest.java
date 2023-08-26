package com.okworo.cards.dao.request;

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
public class SignInRequest {

    private String email;
    private String password;
}
