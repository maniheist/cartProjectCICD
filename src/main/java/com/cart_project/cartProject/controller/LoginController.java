package com.cart_project.cartProject.controller;

import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
public class LoginController {
    @Autowired
    private final LoginService loginService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {

            User user = loginService.getUserById(id);
            return ResponseEntity.ok(user);
    }

    @PostMapping(value="/findByMailAndId")
    public ResponseEntity<Boolean> findByMailAndUserId(@RequestBody User user){
        boolean flag =loginService.findByEmailAndPassword(user);
        return ResponseEntity.ok(flag);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> checkUser(@RequestBody User user) {
        if (loginService.findByEmail(user)) {
            User originalUser=loginService.getUserById(user.getId());
            if (passwordEncoder.matches(user.getPassword(), originalUser.getPassword())) {
                return ResponseEntity.ok("Login Successfully Completed");
            } else {
                return new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("No Such Account Exist",HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {

            if (loginService.findByEmail(user)) {
                return new ResponseEntity<>("Email Already Exist",HttpStatus.CONFLICT);
            }
                loginService.createUser(user);
                return ResponseEntity.ok("Register Successfully Completed");
    }


    @PatchMapping(value = "/updateUserPassword")
    public ResponseEntity<String> updateUser(@RequestParam int userId,@RequestParam String password){

        loginService.updateUserPassword(userId,password);
        return ResponseEntity.ok("Updated successfully");
    }

    @DeleteMapping (value = "/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable int userId){
        loginService.deleteUser(userId);
        return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
    }


}
