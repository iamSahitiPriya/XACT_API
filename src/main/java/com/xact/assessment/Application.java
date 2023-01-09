/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Thoughtworks X-Act API",
                version = "1.1.0",
                description = "Backend API for Thoughtworks X-Act application",
                license = @License(name = "Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.", url = "https://www.thoughtworks.com"),
                contact = @Contact(url = "https://www.thoughtworks.com", name = "Thoughtworks X-Act", email = "project-xact@thoughtworks.com")
        )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
