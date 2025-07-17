package br.com.desafioalura.forumhub;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ⚠️ Acesso negado - usuário não tem a role necessária
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        return createResponse("Acesso negado: você precisa ser ADMIN para realizar esta ação.", HttpStatus.FORBIDDEN);
    }

    // ❌ Login inválido
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return createResponse("Credenciais inválidas. Verifique seu e-mail e senha.", HttpStatus.UNAUTHORIZED);
    }

    // 🚫 Usuário não encontrado
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UsernameNotFoundException ex) {
        return createResponse("Usuário não encontrado.", HttpStatus.NOT_FOUND);
    }

    // 🔒 Token inválido ou expirado
    @ExceptionHandler(com.auth0.jwt.exceptions.TokenExpiredException.class)
    public ResponseEntity<Map<String, String>> handleTokenExpired(com.auth0.jwt.exceptions.TokenExpiredException ex) {
        return createResponse("Token expirado. Faça login novamente.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(com.auth0.jwt.exceptions.SignatureVerificationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidToken(com.auth0.jwt.exceptions.SignatureVerificationException ex) {
        return createResponse("Token inválido. Faça login novamente.", HttpStatus.UNAUTHORIZED);
    }

    // 🔁 Refresh token inválido
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return createResponse("Token inválido ou malformado.", HttpStatus.BAD_REQUEST);
    }

    // 📥 Erros de validação de input
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // 🌐 Qualquer outro erro não previsto
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return createResponse("Erro interno do servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 🔧 Método auxiliar
    private ResponseEntity<Map<String, String>> createResponse(String msg, HttpStatus status) {
        Map<String, String> body = new HashMap<>();
        body.put("erro", msg);
        return ResponseEntity.status(status).body(body);
    }
}
