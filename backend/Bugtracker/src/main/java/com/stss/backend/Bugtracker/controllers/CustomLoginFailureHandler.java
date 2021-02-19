package com.stss.backend.Bugtracker.controllers;

import com.stss.backend.Bugtracker.models.User;
import com.stss.backend.Bugtracker.services.BugUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private BugUserDetailsService bugUserDetailsService;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        User user = bugUserDetailsService.findByEmail(email);

        if(user != null){
            if(user.isAccountNonLocked()){
                if(user.getFailedAttempt() < BugUserDetailsService.MAX_FAILED_ATTEMPTS -1){
                    bugUserDetailsService.increaseFailedAttempts(user);
                }else{
                    bugUserDetailsService.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts" +
                            " It will be unlocked after 24 hours");
                }
            }else if(!user.isAccountNonLocked()){
                if(bugUserDetailsService.unlockWhenTimeExpired(user)){
                    exception = new LockedException("Your account has been unlocked. Please try again");
                }
            }
        }
        super.setDefaultFailureUrl("/authenticate?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
