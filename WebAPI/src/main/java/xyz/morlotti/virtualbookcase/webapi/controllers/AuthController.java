package xyz.morlotti.virtualbookcase.webapi.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import xyz.morlotti.virtualbookcase.webapi.EmailSender;
import xyz.morlotti.virtualbookcase.webapi.models.User;
import xyz.morlotti.virtualbookcase.webapi.daos.UserDAO;
import xyz.morlotti.virtualbookcase.webapi.security.jwt.JwtUtils;

@RestController
public class AuthController
{
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(path = "/auth/login", method = RequestMethod.GET)
    public String login(
        @RequestParam("login") String login,
        @RequestParam("password") String password
    ) throws Exception
    {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "Token:" + jwtUtils.generateJwtToken(authentication);
    }

    @RequestMapping(path = "/auth/remind-password", method = RequestMethod.GET)
    public ResponseEntity<Void> remindPassword(@RequestParam("email") String email) throws Exception
    {
        Optional<User> optional = userDAO.findByEmail(email);

        if(optional.isPresent())
        {
            User user = optional.get();

            emailSender.sendMessage(
                user.getEmail(),
                user.getEmail(),
                "",
                "Mot de passe - Bibliothèque",
                "Bonjour,\nVotre mot de passe est " + user.getPassword() + ".\n" +
                "Cordialement,\n" +
                "Votre bibliothèque"
            );
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
