package com.stss.backend.Bugtracker.jwt.resource;

import java.io.Serializable;

public class JwtTokenRequest implements Serializable {

    private static final long serialVersionUID = -5616176897013108345L;

    private String email;
    private String password;

    public JwtTokenRequest() {
        super();
    }

    public JwtTokenRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    {
//        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpbjI4bWludXRlcyIsImV4cCI6MTYwNzI3NDQ3NiwiaWF0IjoxNjA2NjY5Njc2fQ.QAN_x5SAuI72Qnwt_byBWoYTiOjVKkOOKjOXyhOe86jRiQIlvjCYKNC9cGAZ3cATU8s2wjSWPTeGJzm5AJuFYA"
//    }
}
