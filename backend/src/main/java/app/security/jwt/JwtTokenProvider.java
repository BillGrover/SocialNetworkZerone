package app.security.jwt;

import app.security.JwtPersonDetailsService;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class JwtTokenProvider {

    private final Logger logger = LogManager.getLogger(JwtTokenProvider.class);
    private final Marker error = MarkerManager.getMarker("ERROR");

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    private final JwtPersonDetailsService userDetailsService;

    public JwtTokenProvider(JwtPersonDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String email, String firstName, String lastName) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Optional<String> email = getEmail(token);
        if (email.isPresent()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email.get());
            return new UsernamePasswordAuthenticationToken(userDetails, null);
        } else {
            return null;
        }
    }

    public Optional<String> getEmail(String token) {
        try {
            return Optional.ofNullable(Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody().getSubject());
        } catch (Exception exception) {
            logger.error(
                    error,
                    "Exception in app.security.jwt.JwtTokenProvider.getEmail with cause = {} , with message = {}",
                    exception.getCause() != null ? exception.getCause() : "NULL",
                    exception.getMessage() != null ? exception.getMessage() : "NULL"
            );
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) {
        Optional<String> username = getEmail(token);
        if (username.isEmpty()) {
            return false;
        } else {
            return /*userDetailsService.loadUserByUsername(username.get()) != null && */ !isTokenExpired(token);
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
