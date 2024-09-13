package com.example.shopapp.service.implement;

import com.example.shopapp.Model.Role;
import com.example.shopapp.Model.User;
import com.example.shopapp.components.JwtTokenUtil;
import com.example.shopapp.config.SecurityConfig;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.exception.PermissionDenyException;
import com.example.shopapp.repositories.RoleRepo;
import com.example.shopapp.repositories.UserRepo;
import com.example.shopapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepo.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role  = roleRepo.findById(userDTO.getRoleId()).orElseThrow(()->new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("You cannot register an admin count");
        }
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        user.setRole(role);
        if(userDTO.getFacebookAccountId()==0&&userDTO.getFacebookAccountId()==0){
            String password= userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
        }
        return userRepo.save(user);
    }

    @Override
    public String login(String phone, String password) throws Exception {
       Optional<User> userOptional= userRepo.findByPhoneNumber(phone);
       if(userOptional.isEmpty()){
           throw new DataNotFoundException("invalid phone number or password");
       }
       User existedUser = userOptional.get();
       // check password
        if(existedUser.getGoogleAccountId()==0 && existedUser.getFacebookAccountId()==0){
            if(!passwordEncoder.matches(password,existedUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone,password, existedUser.getAuthorities());
       // authenticate with java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existedUser); // do you want to return ve jwt token
    }
}
