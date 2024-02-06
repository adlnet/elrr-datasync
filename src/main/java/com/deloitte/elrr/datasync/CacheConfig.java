package com.deloitte.elrr.datasync;


import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class CacheConfig {

    @Value("${lrs.samlid}")
    private String samlid;
    @Value("${lrs.samlurl}")
    private String samlurl;
    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrations() throws Exception {
        RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
                .fromMetadataLocation(samlurl).registrationId(samlid).build();

        return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated()).saml2Metadata(withDefaults())
                .saml2Login(saml2 -> {
                    try {
                        saml2.relyingPartyRegistrationRepository(relyingPartyRegistrations());
                        saml2.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });

        return http.build();
    }

}

