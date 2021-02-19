package com.stss.backend.Bugtracker.jwt.resource;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private final String token;

    private String name;

    private long id;

    private Cookie cookie;

    @Autowired
    private HttpServletResponse httpServletResponse;

    public JwtTokenResponse(String token) {
        this.token = token;
    }

    public JwtTokenResponse(String token, long id, Cookie cookie) {
        this.token = token;
        this.id = id;
        this.cookie = cookie;
    }

    public JwtTokenResponse(String token, String name, long id) {
        this.token = token;
        this.name = name;
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public long getId() {
        return id;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void addCookie(Cookie cookie) {
        httpServletResponse.addCookie(cookie);
    }
}
