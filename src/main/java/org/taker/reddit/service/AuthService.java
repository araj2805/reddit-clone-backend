package org.taker.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.taker.reddit.dto.RegisterRequest;
import org.taker.reddit.exception.SpringRedditException;
import org.taker.reddit.model.NotificationEmail;
import org.taker.reddit.model.User;
import org.taker.reddit.model.VerificationToken;
import org.taker.reddit.repository.UserRepository;
import org.taker.reddit.repository.VerificationTokenRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUsername(registerRequest.getUsername());
        user.setEnabled(false);
        user.setCreated(LocalDateTime.now());

        userRepository.save(user);

        String token = generateVerificationCode(user);
        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), "Thank you for signing up to Spring Reddit," +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationCode(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token")));
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with username -> " + username));

        user.setEnabled(true);
        userRepository.save(user);
    }
}
