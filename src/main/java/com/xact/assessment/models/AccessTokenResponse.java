package com.xact.assessment.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AccessTokenResponse {
    String token_type;
    Integer expires_token;
    String access_token;
    String scope;
}
