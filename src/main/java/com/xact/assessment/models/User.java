package com.xact.assessment.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    private String status;
    Profile profile;

    public String getUserEmail() {
        return profile.getEmail();
    }
}
