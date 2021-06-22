package app.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        Cookie[] cookies = servletRequest.getCookies();

        if (cookies != null) {
            String token = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                }
            }

            if (token != null && jwtTokenProvider.validateToken(token)) {
                UsernamePasswordAuthenticationToken usrPassAuthToken = jwtTokenProvider.getAuthentication(token);
                if (usrPassAuthToken != null) {
                    usrPassAuthToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(servletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usrPassAuthToken);
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
