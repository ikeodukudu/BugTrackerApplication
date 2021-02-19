package com.stss.backend.Bugtracker.services;

import com.stss.backend.Bugtracker.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface BugUserService extends UserDetailsService {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);

    //User save(UserResgistrationDto registration);
}
