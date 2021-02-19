package com.stss.backend.Bugtracker.controllers;

import com.stss.backend.Bugtracker.dtos.HomeDto;
import com.stss.backend.Bugtracker.dtos.UpdatePwdDto;
import com.stss.backend.Bugtracker.dtos.UserLoginDto;
import com.stss.backend.Bugtracker.dtos.UserResgistrationDto;
import com.stss.backend.Bugtracker.jwt.JwtTokenUtil;
import com.stss.backend.Bugtracker.jwt.resource.AuthenticationException;
import com.stss.backend.Bugtracker.models.User;
import com.stss.backend.Bugtracker.repositories.TicketRepository;
import com.stss.backend.Bugtracker.repositories.UserRepository;
import com.stss.backend.Bugtracker.services.BugUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import java.net.URI;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private BugUserDetailsService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("USERS")
    public UserResgistrationDto userResgistrationDto() {
        return new UserResgistrationDto();
    }

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BugUserDetailsService bugUserDetailsService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @PostMapping("/register")
    public ResponseEntity<Void> registerAccount(@RequestBody UserResgistrationDto userDto, BindingResult result) {
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        User createdUser = userService.save(userDto);
        logger.info("User added successfully: " + createdUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/login").build().toUri();//buildAndExpand(createdUser.getUserId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePwdDto updatePwdDto) {
        authenticate(updatePwdDto.getEmail(), updatePwdDto.getNewPassword());
        userService.updatePassword(updatePwdDto);
        User existing = userService.findByEmail(updatePwdDto.getEmail());
        logger.info("Password updated successfully: " + existing);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/login").build().toUri();//buildAndExpand(createdUser.getUserId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    private void authenticate(String email, String password) throws AuthenticationException {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        User user = bugUserDetailsService.findByEmail(email);
        if (user != null) {
            try {
                if (!user.isAccountNonLocked() && user.getLockTime() != null) {
                    if (!bugUserDetailsService.unlockWhenTimeExpired(user)) {
                        throw new AuthenticationException("Your account has been locked. Please try after 24 hours",
                                new BadCredentialsException("Your account has been locked. Please try after 24 hours"));
                    } else {
                        bugUserDetailsService.resetFailedAttempts(user.getEmail());
                    }
                }
            } catch (BadCredentialsException e) {
                throw new AuthenticationException("Something went wrong", e);
            }
        } else {
            throw new AuthenticationException("Account not found -- Please Register",
                    new BadCredentialsException("Account not found -- Please Register"));
        }

    }

//    @GetMapping("/login/{email}/{password}")
//    public ResponseEntity<Void> loginAccount(@PathVariable String email, @PathVariable String password){
//        User findUser = userService.findByEmail(email);
//        if(findUser != null){
//                URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/home").buildAndExpand(findUser.getName()).toUri();
//                String name = findUser.getName();
//                logger.info("User logged in successfully");
//                return ResponseEntity.created(uri).build();
//        }
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/home").buildAndExpand(findUser.getUserId()).toUri();
//        return ResponseEntity.created(uri).build();
//    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) {

        User findUser = userService.findByEmail(userLoginDto.getEmail());
        UserDetails userDetails = bugUserDetailsService.loadUserByUsername(userLoginDto.getEmail());
        long id = findUser.getUserId();
        return ResponseEntity.ok(id);
    }

    @GetMapping(path = "/home/{id}")
    public HomeDto getName(@PathVariable long id) {
        User findUser = userService.findByUserId(id);
        String fullname = findUser.getName();
        return new HomeDto(String.format("Welcome %s", fullname));
    }
}
