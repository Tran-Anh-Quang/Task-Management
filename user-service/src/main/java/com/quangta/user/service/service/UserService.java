package com.quangta.user.service.service;

import com.quangta.user.service.entity.User;

import java.util.List;

public interface UserService {

    public User getUserProfile(String jwt);

    public List<User> getAllUsers();
}
