package org.taker.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taker.reddit.dto.RegisterRequest;
import org.taker.reddit.service.AuthService;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {

        authService.signup(registerRequest);

        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> accountVerification(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activiated Successfully ", HttpStatus.OK);
    }
}
