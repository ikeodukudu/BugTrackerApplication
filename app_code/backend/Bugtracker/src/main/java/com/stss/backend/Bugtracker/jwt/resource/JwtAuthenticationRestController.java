package com.stss.backend.Bugtracker.jwt.resource;

import com.stss.backend.Bugtracker.controllers.CustomLoginFailureHandler;
import com.stss.backend.Bugtracker.controllers.UserController;
import com.stss.backend.Bugtracker.jwt.JwtTokenUtil;
import com.stss.backend.Bugtracker.models.BugUserDetails;
import com.stss.backend.Bugtracker.models.User;
import com.stss.backend.Bugtracker.services.BugUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class JwtAuthenticationRestController {

    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BugUserDetailsService bugUserDetailsService;

    @Autowired
    private CustomLoginFailureHandler customLoginFailureHandler;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
            throws AuthenticationException {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final UserDetails userDetails = bugUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        User findUser = bugUserDetailsService.findByEmail(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final Cookie cookie = new Cookie("sessionID", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(1 * 1* 60 * 60); //expires in an hour
        //response.addCookie(cookie);
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse(token, findUser.getUserId(), cookie);
        logger.info("Cookie: " + cookie.getValue());
        //logger.info("Hashed cookie " + val);
        return ResponseEntity.ok(jwtTokenResponse);
    }

    @RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        BugUserDetails user = (BugUserDetails) bugUserDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String email, String password) throws AuthenticationException {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        User user = bugUserDetailsService.findByEmail(email);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            try {
                if (user.getFailedAttempt() > 0 && user.isAccountNonLocked()) {
                    bugUserDetailsService.resetFailedAttempts(user.getEmail());
                } else if (!user.isAccountNonLocked() && user.getLockTime() != null) {
                    if (!bugUserDetailsService.unlockWhenTimeExpired(user)) {
                        throw new AuthenticationException("Your account has been locked. Please try after 24 hours", new BadCredentialsException(""));
                    } else {
                        bugUserDetailsService.resetFailedAttempts(user.getEmail());
                    }
                }
            } catch (BadCredentialsException e) {
                if (!user.isAccountNonLocked() && user.getLockTime() != null) {
                    if (!bugUserDetailsService.unlockWhenTimeExpired(user)) {
                        throw new AuthenticationException("Your account has been locked. Please try again after 24 hours", e);
                    }
                }
            }

        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            if (user != null) {
                if (user.isAccountNonLocked()) {
                    //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                    if (user.getFailedAttempt() < BugUserDetailsService.MAX_FAILED_ATTEMPTS - 1) {
                        bugUserDetailsService.increaseFailedAttempts(user);
                    } else {
                        bugUserDetailsService.lock(user);
                        logger.info("Your account has been locked");
//                        throw new LockedException("Your account has been locked due to 3 failed attempts" +
//                                " It will be unlocked after 24 hours");
                        throw new AuthenticationException("Your account has been locked due to 3 failed attempts" +
                                " It will be unlocked after 24 hours", e);
                    }
                } else if (!user.isAccountNonLocked()) {
                    if (bugUserDetailsService.unlockWhenTimeExpired(user)) {
                        throw new LockedException("Your account has been unlocked. Please try again");
                    } else {
                        throw new AuthenticationException("Your account has been locked. Please try again after 24 hours", e);
                    }
                }
            }
            throw new AuthenticationException("INVALID CREDENTIALS - Try again", e);
        }
    }
}
