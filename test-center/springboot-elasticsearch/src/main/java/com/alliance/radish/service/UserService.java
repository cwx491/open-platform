package com.alliance.radish.service;

import com.alliance.radish.domain.User;
import com.alliance.radish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> findAll(){
        Iterable<User> users = userRepository.findAll();
        List<User> list = new ArrayList<>();
        users.forEach(user -> {
            list.add(user);
        });
        return list;
    }
}
