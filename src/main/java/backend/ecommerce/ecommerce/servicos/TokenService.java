package backend.ecommerce.ecommerce.servicos;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    private final SecretKey chaveSecreta;
    private final String emissor;
    private final long minutosAcesso;
    private final long diasRefresh;

    public TokenService(@Value("${security.jwt.segredo}") String segredo,
                        @Value("${security.jwt.emissor}") String emissor,
                        @Value("${security.jwt.minutos-acesso}") long minutosAcesso,
                        @Value("${security.jwt.dias-refresh}") long diasRefresh) {

        this.chaveSecreta =  Keys.hmacShaKeyFor(segredo.getBytes());
        this.emissor = emissor;
        this.minutosAcesso = minutosAcesso;
        this.diasRefresh = diasRefresh;  
    }

    public String gerarAccessoToken(String email, String perfil) {
        var agora = Instant.now();
        return Jwts.builder()
        .setSubject(email)
        .setIssuer(emissor)
        .setIssuedAt(Date.from(agora))
        .setExpiration(Date.from(agora.plus(minutosAcesso, ChronoUnit.MINUTES)))
        .addClaims(Map.of("perfil", perfil))
        .signWith(chaveSecreta, SignatureAlgorithm.HS256)
        .compact();
    }

    public String gerarRefreshToken(String email) {
        var agora = Instant.now();
        return Jwts.builder()
        .setSubject(email)
        .setIssuer(emissor)
        .setIssuedAt(Date.from(agora))
        .setExpiration(Date.from(agora.plus(diasRefresh, ChronoUnit.DAYS)))
        .claim("tipo", "refresh")
        .signWith(chaveSecreta, SignatureAlgorithm.HS256)
        .compact();
    }

    public Jws<Claims> validarToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(chaveSecreta)
            .requireIssuer(emissor)
            .build()
            .parseClaimsJws(token);
    }
}
