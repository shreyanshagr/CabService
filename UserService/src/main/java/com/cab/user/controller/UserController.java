//package com.cab.user.controller;
//
//import com.cab.user.auth.payload.AuthDTO.*;
//import com.cab.user.payload.UserDTO.*;
//import com.cab.user.repository.UserRepository;
//import com.cab.user.service.IUserService;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@AllArgsConstructor
//@Slf4j
//public class UserController {
//
//    private final UserRepository userRepository;
//
//    private final IUserService userService;
//
//
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@Valid @RequestBody final UserSignUpDTO userSignUpDTO,
//                                    final HttpServletResponse response) {
//        // Validate user input
//        log.info(userSignUpDTO.toString());
//        if (userSignUpDTO == null) {
//            return ResponseEntity.badRequest().body("User details cannot be null");
//        }
//
//        // Check if user with the provided email already exists
//        if (userRepository.findByEmail(userSignUpDTO.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("User with this email already exists");
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userSignUpDTO));
//
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody final LogInRequestDTO loginRequestDTO,
//                                                     final HttpServletResponse response) {
//         String accessToken = userService.authenticateAndGenerateToken(loginRequestDTO);
//    }
//
//}
