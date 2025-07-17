package br.com.desafioalura.forumhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint acessível publicamente
    @GetMapping("/public")
    public String publicEndpoint() {
        return "Este é um endpoint público!" + passwordEncoder.encode("12345678");
    }

    // Endpoint acessível para utilizadores autenticados (qualquer função)
    @GetMapping("/user")
    public String userEndpoint() {
        return "Olá, utilizador autenticado!";
    }

    // Endpoint acessível apenas para utilizadores com a função ADMIN
    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Olá, administrador!";
    }
}
