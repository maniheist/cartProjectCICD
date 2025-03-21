package com.cart_project.cartProject.serviceTest;

import com.cart_project.cartProject.exceptions.UserNotFound;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.repository.LoginRepository;
import com.cart_project.cartProject.service.LoginService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)

public class LoginServiceTest {
    @Mock
    LoginRepository loginRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    LoginService loginService;
    private static User user;
    @BeforeAll
    public static void beforeAll(){
        user=new User();
        user.setId(1);
        user.setName("randy");
        user.setPassword("randy@11");
        user.setEmail("randy2611@gmail.com");
        user.setRole("User");
    }
    @AfterAll
    public static void afterAll(){
        User user=null;
    }
    @Test
    public void getUserByIdTest(){
        Mockito.when(loginRepository.findById(1)).thenReturn(Optional.of(user));
        User userAdded=loginService.getUserById(1);
        Assertions.assertEquals(1,userAdded.getId());
    }

    @Test
    public void deleteUserTest(){
        Mockito.when(loginRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        Mockito.doNothing().when(loginRepository).delete(user);

        loginService.deleteUser(1);
        Mockito.verify(loginRepository,times(1)).delete(user);
    }

    @Test
    public void getUserByIdUseDoesNotExist(){
        Mockito.when(loginRepository.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFound.class,()->{
            loginService.getUserById(2);
        });
    }

    @Test
    public void createUserTest(){
        Mockito.when(loginRepository.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        User newUser=loginService.createUser(user);
        Assertions.assertEquals(user.getName(),newUser.getName());
        Mockito.verify(loginRepository,times(1)).save(user);
    }

    @Test
    public void updateUserTest(){
        Mockito.when(loginRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(loginRepository.save(user)).thenReturn(user);
        loginService.updateUser(1,new User(1,"randy ortan","randy@123","randy2611@gmail.com","user"));
        Assertions.assertEquals("randy ortan",user.getName());
    }

    @Test
    public void updateUserExceptionTest(){
        Mockito.when(loginRepository.findById(2)).thenThrow(new UserNotFound("User not found"));

        Assertions.assertThrows(UserNotFound.class,()->{
            loginService.updateUser(2,new User(2,"randy ortan","randy@123","randy2611@gmail.com","user"));
        });
    }

    @Test
    public void findByEmailTest(){
        Mockito.when(loginRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        boolean flag=loginService.findByEmail(new User(1,"randy ortan","randy2611@gmail.com","Randy@123","user"));
        Assertions.assertTrue(flag);
    }

    @Test
    public void findByEmailAndPasswordTest(){
        Mockito.when(loginRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        boolean flag1=loginService.findByEmailAndPassword(user);
        boolean flag2=loginService.findByEmailAndPassword(new User(1,"randy ortan","Randy@123","randy2611@gmail.com","user"));
        Assertions.assertTrue(flag1);
        Assertions.assertFalse(flag2);

    }

}
