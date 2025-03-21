package com.cart_project.cartProject.service;

import com.cart_project.cartProject.exceptions.UserNotFound;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
@RequiredArgsConstructor
@Service
public class LoginService {
    private final LoginRepository loginRepository;

    private final PasswordEncoder passwordEncoder;

    public User createUser(User user){
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User newUser=loginRepository.save(user);
                return newUser;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }
    }


    @Transactional
    public User getUserById(int userId) throws UserNotFound {
        return loginRepository.findById(userId).orElseThrow(()->new UserNotFound("User with ID " + userId + " not found"));
    }

    @Transactional

    public void deleteUser(int userId){
            User existingUser = loginRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFound("User " + userId + " Not Found"));
            loginRepository.delete(existingUser);

    }

    public boolean findByEmailAndPassword(User user){
        CompletableFuture<Boolean> completableFutureFindByEmailAndPassword=CompletableFuture.supplyAsync(()->{
            return loginRepository.findByEmailAndPassword(user.getEmail(),user.getPassword()).isPresent();
        });
        return completableFutureFindByEmailAndPassword.join();
    }

    @Transactional(readOnly = true)
    public boolean findByEmail(User user){
        return loginRepository.findByEmail(user.getEmail()).isPresent();
    }


    @Transactional
    public User updateUser(int userId,User user){

        User userDetails = loginRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User Not Found"));

        userDetails.setName(user.getName());
        userDetails.setEmail(user.getEmail());
        userDetails.setPassword(user.getPassword());
        userDetails.setRole(user.getRole());

        return loginRepository.save(userDetails);
    }

    @Transactional
    public User updateUserPassword(int userId,String password){
        User userDetails = loginRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound("User Not Found"));
        userDetails.setPassword(passwordEncoder.encode(password));
        return loginRepository.save(userDetails);
    }



}
