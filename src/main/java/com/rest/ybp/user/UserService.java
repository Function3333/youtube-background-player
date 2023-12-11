package com.rest.ybp.user;

import com.rest.ybp.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public int saveUser(User user) {
        return userRepository.save(user);
    }
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public Result validateName(String name) {
        return isDuplicateName(name) ? Result.DUPLICATE_NAME : Result.SUCCESS;
    }

    public Result validateEmail(String email) {
        return isDuplicateEmail(email) ? Result.DUPLICATE_EMAIL : Result.SUCCESS;
    }

    public boolean isDuplicateName(String name) {
        User user = getUserByName(name);
        return user != null;
    }

    public boolean isDuplicateEmail(String email) {
        User user = getUserByEmail(email);
        return user != null;
    }
}
