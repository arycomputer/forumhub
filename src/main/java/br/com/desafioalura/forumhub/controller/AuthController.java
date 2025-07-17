package br.com.desafioalura.forumhub.controller;

import br.com.desafioalura.forumhub.web.JwtRequest;
import br.com.desafioalura.forumhub.web.JwtResponse;
import br.com.desafioalura.forumhub.model.User;
import br.com.desafioalura.forumhub.repository.UserRepository;
import br.com.desafioalura.forumhub.security.JwtTokenUtil;
import br.com.desafioalura.forumhub.service.JwtUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já existe!");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        System.out.println(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole("USER"); // Define a função padrão como USER
        userRepository.save(newUser);
        return ResponseEntity.ok("Utilizador registado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        System.out.println(authenticationRequest.getEmail()+" "+ authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail()); // loadUserByUsername agora usa email

        // Obter o objeto User completo para acessar a função
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado após autenticação."));

        final String token = jwtTokenUtil.generateToken(userDetails, user.getRole()); // Passa a função para o gerador de token
        return ResponseEntity.ok(new JwtResponse(token, user.getRole())); // Retorna o token e a função
    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                        @RequestParam String password,
//                        HttpSession session,
//                        Model model) {
//        try {
//            apiService.login(email, password, session);
//            return "redirect:/";
//        } catch (Exception e) {
//            model.addAttribute("erro", "Falha ao autenticar: " + e.getMessage());
//            return "login";
//        }
//    }
//    @PostMapping("/login")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
//System.out.println(authenticationRequest.getEmail()+" "+ authenticationRequest.getPassword());
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(authenticationRequest.getEmail()); // loadUserByUsername agora usa email
//
//        // Obter o objeto User completo para acessar a função
//        User user = userRepository.findByEmail(authenticationRequest.getEmail())
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado após autenticação."));
//
//        final String token = jwtTokenUtil.generateToken(userDetails, user.getRole()); // Passa a função para o gerador de token
//System.out.println(token);
//        return ResponseEntity.ok(new JwtResponse(token, user.getRole())); // Retorna o token e a função
//    }

//    @PostMapping("/login")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(authenticationRequest.getEmail()); // loadUserByUsername agora usa email
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("UTILIZADOR DESATIVADO", e);
        } catch (BadCredentialsException e) {
            throw new Exception("CREDENCIAS INVÁLIDAS", e);
        }
    }
}
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private JwtUserDetailsService userDetailsService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody User newUser) {
//        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("Email já existe!");
//        }
//        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        newUser.setRole("USER"); // Define a função padrão como USER
//        userRepository.save(newUser);
//        return ResponseEntity.ok("Utilizador registado com sucesso!");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(authenticationRequest.getEmail()); // loadUserByUsername agora usa email
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new JwtResponse(token));
//    }
//
//    private void authenticate(String email, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//        } catch (DisabledException e) {
//            throw new Exception("UTILIZADOR DESATIVADO", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("CREDENCIAS INVÁLIDAS", e);
//        }
//    }
//}