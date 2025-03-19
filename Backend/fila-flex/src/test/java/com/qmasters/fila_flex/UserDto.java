package com.qmasters.fila_flex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.qmasters.fila_flex.dto.UserDTO;
import com.qmasters.fila_flex.util.UserRole;

public class UserDto {
    @Test
    void testConstrutorComParametros() {
         UserDTO user = new UserDTO("email@test.com", "senha123", UserRole.ADMIN, "Alice");
    
        assertEquals("email@test.com", user.getEmail());
        assertEquals("senha123", user.getPassword());
        assertEquals(UserRole.ADMIN, user.getRole());
        assertEquals("Alice", user.getName());
    }
    
    @Test
    void testSettersEGetters() {
        UserDTO user = new UserDTO();
            
        user.setEmail("novoemail@test.com");
            
        user.setPassword("novaSenha");
            
        user.setRole(UserRole.USER);
        user.setName("Bob");
    
        assertEquals("novoemail@test.com", user.getEmail());
           
        assertEquals("novaSenha", user.getPassword());
        assertEquals(UserRole.USER, user.getRole());
        assertEquals("Bob", user.getName());
    }
}

