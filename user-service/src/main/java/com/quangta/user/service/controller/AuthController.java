package com.quangta.user.service.controller;

import com.quangta.user.service.config.JwtProvider;
import com.quangta.user.service.dto.payload.AuthResponse;
import com.quangta.user.service.dto.payload.LoginRequest;
import com.quangta.user.service.entity.User;
import com.quangta.user.service.repository.UserRepository;
import com.quangta.user.service.service.impl.CustomUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CustomUserServiceImpl customUserService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{

        User isExist = userRepository.findByEmail(user.getEmail());
        if(isExist != null) {
            throw new Exception("Email already exists");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setFullName(user.getFullName());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setRole(user.getRole());

        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(JwtProvider.generateToken(authentication));
        authResponse.setMessage("Register successfully");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){

        Authentication authentication = authenticate(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthResponse  authResponse = new AuthResponse();

        authResponse.setMessage("Login Successfully");
        authResponse.setStatus(true);
        authResponse.setJwt(JwtProvider.generateToken(authentication));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
