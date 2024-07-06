package it.uniroma3.siw.coccolecapelli.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import it.uniroma3.siw.coccolecapelli.model.User;
import it.uniroma3.siw.coccolecapelli.service.UtenteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	private UtenteService utenteService;
	
	public OAuth2LoginSuccessHandler(){
        super();
    }
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String loginName = oAuth2User.getLogin();
        String displayName = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String fullName = oAuth2User.getFullName();
        System.out.println("Login name: " + loginName );
        System.out.println("Display name: " + displayName );
        System.out.println("email: " + email );
        System.out.println("fullname: " + fullName );

        User user= utenteService.getUsername(loginName);

        if(user == null){
            utenteService.registerNewCustomerAfterOAuthLoginSuccess(loginName,fullName,AuthenticationProvider.OAUTH);
        }else{
            utenteService.updateExistingUser(user ,fullName, AuthenticationProvider.OAUTH);
        }

        response.sendRedirect("/login/oauth2/user");
        SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
