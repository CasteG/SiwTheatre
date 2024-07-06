package it.uniroma3.siw.coccolecapelli.oauth;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum AuthenticationProvider {
	LOCAL,OAUTH
}
