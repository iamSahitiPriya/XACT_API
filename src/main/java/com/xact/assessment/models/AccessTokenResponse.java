package com.xact.assessment.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AccessTokenResponse {
    String token_type;
    Integer expires_in;
    String access_token;
    String scope;
    Date createdTime;
}
