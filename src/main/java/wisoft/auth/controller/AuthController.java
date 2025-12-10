package wisoft.auth.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wisoft.auth.dto.LoginRequest;
import wisoft.auth.dto.SignUpRequest;
import wisoft.auth.dto.TokenResponse;
import wisoft.auth.security.JwtTokenProvider;
import wisoft.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN";

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    private ResponseCookie createTokenCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(60 * 60)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (!authService.authenticate(request.getUsername(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtTokenProvider.createToken(request.getUsername());
        ResponseCookie cookie = createTokenCookie(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(TokenResponse.builder().message("로그인 성공").build());
    }

    @PostMapping("/extend")
    public ResponseEntity<TokenResponse> extend(
            @CookieValue(value = ACCESS_TOKEN_COOKIE, required = false) String token) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TokenResponse.builder().message("토큰이 없습니다.").build());
        }

        try {
            jwtTokenProvider.validateToken(token);

            String username = jwtTokenProvider.getUsername(token);
            String newToken = jwtTokenProvider.createToken(username);

            ResponseCookie cookie = createTokenCookie(newToken);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(TokenResponse.builder().message("토큰 연장 성공").build());

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TokenResponse.builder().message("토큰이 만료되었습니다").build());

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TokenResponse.builder().message("유효하지 않은 토큰입니다").build());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenResponse> validate(
            @CookieValue(value = ACCESS_TOKEN_COOKIE, required = false) String token) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TokenResponse.builder().message("토큰이 없습니다.").build());
        }

        try {
            jwtTokenProvider.validateToken(token);
            return ResponseEntity.ok(
                    TokenResponse.builder().message("유효한 토큰입니다").build()
            );

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TokenResponse.builder().message("토큰이 만료되었습니다").build());

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(TokenResponse.builder().message("유효하지 않은 토큰입니다").build());
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        try {
            authService.signUp(request.getUsername(), request.getPassword());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}