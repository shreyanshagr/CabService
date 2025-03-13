//package com.cab.user.service.impl;
//
//import com.cab.user.entity.User;
//import com.cab.user.enums.Role;
//import com.cab.user.auth.payload.AuthDTO;
//import com.cab.user.payload.UserDTO.*;
//import com.cab.user.repository.UserRepository;
//import com.cab.user.service.IUserService;
//import com.cab.user.auth.service.JWTService;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//@Slf4j
//@Service
//public class UserService implements IUserService {
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;
//    private final AuthenticationManager authenticationManager;
//    private final JWTService jwtService;
//
//    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//        this.authenticationManager = authenticationManager;
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    public UserSignupResponseDTO save(UserSignUpDTO userSignUpDTO) {
//        userSignUpDTO.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword()));
//        final User user = userSignUpDTO.toEntity();
//        user.setRole(Role.DRIVER);
//        user.setSignupDate(LocalDateTime.now());
//        user.setEnabled(true);
//        User savedUser = userRepository.save(user);
//        log.info("Saved user -> {}", savedUser);
//        UserSignupResponseDTO userSignupResponseDTO = savedUser.toSignUpResponseDTO();
//        log.info("Response  -> {}", userSignupResponseDTO);
//        String accessToken = UUID.randomUUID().toString();
//        String refreshToken = UUID.randomUUID().toString();
////        userSignupResponseDTO.setAccessToken(accessToken);
////        userSignupResponseDTO.setRefreshToken(refreshToken);
//        return userSignupResponseDTO;
//    }
//
//    @Override
//    public Optional<AuthDTO.JwtResponseDTO> login(AuthDTO.LogInRequestDTO logInRequestDTO) {
//        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInRequestDTO.getEmail(), loginRequestDTO.getPassword()));
//        String accessToken = "";
//        if(authentication.isAuthenticated()){
//            accessToken = jwtService.generateToken(loginRequestDTO.getEmail());
//        }
//        //TODO : return refresh token too..
//        return accessToken;
//    }
//}
