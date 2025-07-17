package br.com.desafioalura.forumhub.web;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String email; // Alterado de username para email
    private String password;

    public JwtRequest() {
    }

    public JwtRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getEmail() { // Alterado de getUsername para getEmail
        return this.email;
    }

    public void setEmail(String email) { // Alterado de setUsername para setEmail
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
