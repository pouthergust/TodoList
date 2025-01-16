package br.com.gabrielghvn.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gabrielghvn.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
        var servletPath = request.getServletPath();

        if (!servletPath.startsWith("/tasks/")) {
          filterChain.doFilter(request, response);
          return;
        }

        String auth = request.getHeader("Authorization");
        String authEncoded = auth.substring("Basic".length()).trim();

        byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

        String authString = new String(authDecoded);

        String[] credentials = authString.split(":");

        var user = this.userRepository.findByUsername(credentials[0]);

        if (user == null) {
          response.sendError(401 , "Usuário sem autorização");
        } else {
          var passowordVerify = BCrypt.verifyer().verify(credentials[1].toCharArray(), user.getPassword());
          if (passowordVerify.verified) {
            request.setAttribute("idUser", user.getId());
            filterChain.doFilter(request, response);
          } else {
            response.sendError(401, "Senha inválida");
          }
        }
    
  }
}
