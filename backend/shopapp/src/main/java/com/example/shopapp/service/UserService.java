package com.example.shopapp.service;

import com.example.shopapp.Model.User;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exception.DataNotFoundException;

public interface UserService {
    User createUser (UserDTO userDTO) throws DataNotFoundException, Exception;
    String login(String phone,String password) throws DataNotFoundException, Exception;
    User getUserDetailsFromToken(String token) throws Exception;
    boolean hasRole(User user,String roleName);
    User userFindById(Long userId);
}
