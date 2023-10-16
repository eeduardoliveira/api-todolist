package com.example.demo.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.demo.domain.model.User;
import com.example.demo.repository.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class Auth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository repository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if (servletPath.startsWith("/atividade")) {
            String auth = request.getHeader("Authorization");
            auth = auth.substring("Basic".length()).trim();
            System.out.println(auth);

            byte[] authDecode = Base64.getDecoder().decode(auth);


            System.out.println("Apos Decriptografar");
            System.out.println(authDecode);

            String authString = new String(authDecode);

            System.out.println("Apos Decriptografar");
            System.out.println(authString);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            System.out.println(username);
            System.out.println(password);

            User user = this.repository.findByUsername(username);

            if (user == null) {
                throw new RuntimeException("Usuario nao existe");
            }

            BCrypt.Result passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

            if (!passwordVerify.verified) {
                throw new RuntimeException("Senha incorreta");
            }
            request.setAttribute("idUser", user.getId());
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
