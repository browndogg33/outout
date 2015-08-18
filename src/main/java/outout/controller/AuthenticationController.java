package outout.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import outout.service.AccountService;
import outout.view.AccountCredentials;
import outout.view.AuthenticationToken;

@Controller
@RequestMapping("/authenticate")
public class AuthenticationController {

    @Value("${token.secret}")
    private String tokenSecret;

    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AuthenticationToken> authenticate(@RequestBody AccountCredentials ac) {
        if(accountService.isValidUser(ac)) {
            return new ResponseEntity<>(buildAuthenticationToken(ac), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private AuthenticationToken buildAuthenticationToken(final @RequestBody AccountCredentials ac) {
        AuthenticationToken authenticationToken = new AuthenticationToken();
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS512, tokenSecret)
                .setSubject(ac.getUsername())
                .compact();
        authenticationToken.setToken(jwt);
        return authenticationToken;
    }

}
