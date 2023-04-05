/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.security.token.TokenAuthenticationFetcher;
import io.micronaut.security.token.reader.TokenResolver;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Replaces(value = TokenAuthenticationFetcher.class)
public class MockTokenAuthenticationFetcher implements AuthenticationFetcher {

    @Inject
    TokenResolver resolver;

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        String email = "dummy@test.com";
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", email);
        Authentication authentication = Authentication.build(email, authMap);

        Flowable<Authentication> auth = Flowable.fromOptional(resolver.resolveToken(request)).flatMap(token -> {
            return Flowable.just(authentication);
        });

        return auth;
    }
}
