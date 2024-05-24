package sigurnostbackend.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//Ovaj filter se obično koristi za proveru JWT tokena u svakom zahtevu koji dolazi
// na server kako bi se omogućila autentifikacija korisnika na osnovu tokena
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator tokenGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

//metoda je centralna metoda ovog filtera koja se izvršava za svaki HTTP zahtev. U ovoj metodi se
// vrši provera prisustva JWT tokena u zahtevu i,
// ako je prisutan i validan, postavlja se autentifikacija za korisnika.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
            String token = getJWTFromRequest(request);
        if(StringUtils.hasText(token)) {
            try {
                if(tokenGenerator.validateToken(token)) {
                    String username = tokenGenerator.getUsernameFromJWT(token);

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
            } catch (AuthenticationCredentialsNotFoundException ex) {
                System.out.println("Invalid token: " + ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
//na ovaj nacin izvlacimo token iz header-a
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        //System.out.println("kako izgleda request: "+request.toString());
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
