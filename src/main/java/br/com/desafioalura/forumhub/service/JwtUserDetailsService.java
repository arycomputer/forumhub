package br.com.desafioalura.forumhub.service;

import br.com.desafioalura.forumhub.model.User;
import br.com.desafioalura.forumhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // O parâmetro agora é o email
        User user = userRepository.findByEmail(email) // Busca por email
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado com o email: " + email));

        // Retorna um objeto UserDetails do Spring Security. O email será usado como "username" para o Spring Security.
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole())));
    }
}
