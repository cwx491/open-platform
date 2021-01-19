//package com.alliance.radish.service;
//
//import com.alliance.radish.domain.User;
//import com.alliance.radish.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserService implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//
//    public User findByUserId(Long userId){
//        Optional<User> u = userRepository.findById(userId);
//        if(u != null){
//            return u.get();
//        }
//        return null;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if(user == null) {
//            throw new UsernameNotFoundException("用户名不存在");
//        }
//        return user;
//    }
//}
