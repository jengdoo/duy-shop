package com.example.shopapp.controller;

import com.example.shopapp.Model.User;
import com.example.shopapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.shopapp.dto.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<?> createRegister(@Valid @ModelAttribute UserDTO userDTO, BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> messageError = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageError);
            }
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
                return ResponseEntity.badRequest().body("Password doesn't match");
            }
            User user = userService.createUser(userDTO);
//            return ResponseEntity.ok("register successfully");
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @ModelAttribute UserLoginDTO userLoginDTO){
        try {
            String token  = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

