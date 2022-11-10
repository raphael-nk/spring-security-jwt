package nk.dev.springsecurityjwt;

import nk.dev.springsecurityjwt.models.AuthenticationRequest;
import nk.dev.springsecurityjwt.models.AuthenticationResponse;
import nk.dev.springsecurityjwt.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {
    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/hello")
    public String hello(){ return "Hello World";}

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception{
       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
           );
       } catch (BadCredentialsException e){
           throw new Exception("Incorrect");
       }

        final UserDetails userDetails = userDetailsService.
                loadUserByUsername(authenticationRequest.getUsername());

       final String jwt = jwtUtil.generateToken(userDetails);

       return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
