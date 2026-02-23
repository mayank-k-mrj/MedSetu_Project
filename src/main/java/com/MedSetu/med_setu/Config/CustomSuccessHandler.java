package com.MedSetu.med_setu.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
    throws IOException, ServletException {
        String role = authentication.getAuthorities()
                .iterator().next().getAuthority();
        if(role.equals("USER")){
            response.sendRedirect("/userDashboard.html");
        }
        else if(role.equals("NGO")){
            response.sendRedirect("/ngoDashboard.html");
        }
    }
}
