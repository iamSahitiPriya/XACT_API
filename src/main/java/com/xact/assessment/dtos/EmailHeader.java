package com.xact.assessment.dtos;


import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Introspected
@Getter
@Setter
@AllArgsConstructor
public class EmailHeader {

    String email;
    String name;

    public EmailHeader() {
        this.email = "project-xact@thoughtworks.net";
        this.name = "X-ACT Support";
    }

}
