package com.xact.assessment.controllers;

import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import unit.com.xact.assessment.models.User;

@Controller("/v1/users")
public class UserController {

    private UserAuthService userAuthService;
    private static ModelMapper modelMapper = new ModelMapper();


    public UserController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    static {
        PropertyMap<User, UserDto> userMap = new PropertyMap<>() {
            protected void configure() {
                map().setFirstName(source.getProfile().getFirstName());
                map().setLastName(source.getProfile().getLastName());
            }
        };
        modelMapper.addMappings(userMap);
    }

    @Get(value = "{emailId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserDto> getUserByEmail(@PathVariable String emailId) {
        UserDto userDto = modelMapper.map(userAuthService.getActiveUser(emailId), UserDto.class);
        return HttpResponse.ok(userDto);
    }
}
