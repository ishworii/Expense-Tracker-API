package com.ishwor.expenses.repository;

import com.ishwor.expenses.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) 
@DataJpaTest 
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("securepassword123");

        
        User savedUser = userRepository.save(user);

        
        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    void testFindByEmail() {
        
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPassword("password123");
        userRepository.save(user);

        
        Optional<User> foundUser = userRepository.findByEmail("alice@example.com");

        
        assertTrue(foundUser.isPresent());
        assertEquals("Alice", foundUser.get().getName());
    }

    @Test
    void testEmailUniqueness() {
        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("unique@example.com");
        user1.setPassword("pass123");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("unique@example.com");
        user2.setPassword("pass456");

        assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(user2);
        }, "Should fail due to unique constraint");
    }


    @Test
    void testDeleteUser() {
        
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("securepass");
        User savedUser = userRepository.save(user);

        
        userRepository.delete(savedUser);
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());

        
        assertFalse(deletedUser.isPresent());
    }
}
