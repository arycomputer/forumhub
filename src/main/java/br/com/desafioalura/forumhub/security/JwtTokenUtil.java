package br.com.desafioalura.forumhub.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;
    //private final String secret = "minha-chave-secreta-supersegura-de-256-bits";
    private final long expiration = 1000 * 60 * 60 * 24; // 24 horas

    //private final Algorithm algorithm = Algorithm.HMAC256(secret);

    private Algorithm algorithm;

    // Inicializa o algoritmo de assinatura com a chave secreta
    private Algorithm getAlgorithm() {
        if (this.algorithm == null) {
            this.algorithm = Algorithm.HMAC512(secret);
        }
        return this.algorithm;
    }

    // ✅ Gera token com role como claim
    public String generateToken(UserDetails userDetails, String role) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign( getAlgorithm() );
    }

    // ✅ Extrai username (email) do token
    public String getUsernameFromToken(String token) {
        return decodeToken(token).getSubject();
    }
    public String getEmailFromToken(String token) {
        return decodeToken(token).getSubject();
    }
    // ✅ Extrai role do token
    public String getRoleFromToken(String token) {
        return decodeToken(token).getClaim("role").asString();
    }

    // ✅ Verifica se o token está expirado
    public boolean isTokenExpired(String token) {
        Date expirationDate = decodeToken(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    // ✅ Valida o token com base no UserDetails
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ✅ Decodifica e verifica a assinatura do token
    private DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }


}
