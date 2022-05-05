package com.xact.assessment.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    Profile profile;
    private String status;

    public String getUserEmail() {
        return profile.getEmail();
    }
}
