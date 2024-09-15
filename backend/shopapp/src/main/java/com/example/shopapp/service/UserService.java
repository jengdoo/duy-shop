package com.example.shopapp.service;

import com.example.shopapp.Model.User;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exception.DataNotFoundException;

public interface UserService {
    User createUser (UserDTO userDTO) throws DataNotFoundException, Exception;
    String login(String phone,String password,Long roleId) throws DataNotFoundException, Exception;
}
