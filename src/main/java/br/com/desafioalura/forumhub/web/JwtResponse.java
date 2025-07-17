package br.com.desafioalura.forumhub.web;

// src/main/java/com/example/demo/web/JwtResponse.java

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final String role; // Novo campo para a função do utilizador

    public JwtResponse(String jwttoken, String role ) {
        this.jwttoken = jwttoken;
        this.role = role;
    }

    public String getToken() {
        return this.jwttoken;
    }
    public String getRole() { // Getter para a função
        return this.role;
    }
}
