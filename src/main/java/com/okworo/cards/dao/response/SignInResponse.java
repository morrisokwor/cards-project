package com.okworo.cards.dao.response;

import com.okworo.cards.enums.Role;
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
public class SignInResponse {

    private UserResponse user;
    private String accessToken;
    private String message;
    private Boolean success;
    private Role role;
}
