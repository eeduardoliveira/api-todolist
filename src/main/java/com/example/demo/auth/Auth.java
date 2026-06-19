package com.example.demo.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.demo.domain.model.User;
import com.example.demo.repository.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class Auth extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(Auth.class);

    @Autowired
    private IUserRepository repository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/atividade")) {
            logger.debug("Authenticating request to: {}", servletPath);
            
            String auth = request.getHeader("Authorization");
            auth = auth.substring("Basic".length()).trim();
            logger.debug("Authorization header received (Base64 encoded)");

            byte[] authDecode = Base64.getDecoder().decode(auth);
            String authString = new String(authDecode);
            logger.debug("Authorization credentials decoded");

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            logger.info("Authentication attempt for username: {}", username);

            User user = this.repository.findByUsername(username);

            if (user == null) {
                logger.warn("Authentication failed: User '{}' does not exist", username);
                throw new RuntimeException("Usuario nao existe");
            }

            BCrypt.Result passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

            if (!passwordVerify.verified) {
                logger.warn("Authentication failed: Invalid password for user '{}'", username);
                throw new RuntimeException("Senha incorreta");
            }
            
            logger.info("Authentication successful for user '{}' (ID: {})", username, user.getId());
            request.setAttribute("idUser", user.getId());
            filterChain.doFilter(request, response);
        } else {
            logger.debug("Bypassing authentication for path: {}", servletPath);
            filterChain.doFilter(request, response);
        }
    }
}
