package com.amc.api.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.amc.api.DTO.UserDTO;
import com.amc.api.Entities.User;
import com.amc.api.Enums.UserRoleEnum;
import com.amc.api.Interfaces.UserBO;
import com.amc.api.Repositories.UserRepository;
import com.amc.api.Utils.Exceptions;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserBO userBO;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void getActiveUsersShouldReturnOnlyActiveUsers() {
        User activeUser = buildUser("user-1", true);
        User inactiveUser = buildUser("user-2", false);
        when(userRepository.findAll()).thenReturn(List.of(activeUser, inactiveUser));

        ResponseEntity<List<User>> response = userController.getActiveUsers();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertSame(activeUser, response.getBody().getFirst());
    }

    @Test
    void getActiveUsersShouldReturnNoContentWhenThereAreNoActiveUsers() {
        when(userRepository.findAll()).thenReturn(List.of(buildUser("user-1", false)));

        ResponseEntity<List<User>> response = userController.getActiveUsers();

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void getUserByUuidShouldReturnUserWhenFound() {
        User user = buildUser("user-1", true);
        when(userRepository.findAll()).thenReturn(List.of(user));

        ResponseEntity<User> response = userController.getUserByUuid("user-1");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(user, response.getBody());
    }

    @Test
    void getUserByUuidShouldReturnNotFoundWhenMissing() {
        when(userRepository.findAll()).thenReturn(List.of(buildUser("user-1", true)));

        ResponseEntity<User> response = userController.getUserByUuid("missing");

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void createUserShouldThrowWhenEmailAlreadyExists() {
        User user = buildUser("user-1", true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(new User());

        Exceptions.DatabaseException exception = assertThrows(Exceptions.DatabaseException.class,
                () -> userController.createUser(user));

        assertEquals("Erro no banco de dados: Já existe um usuário com o e-mail: user-1@email.com.", exception.getMessage());
    }

    @Test
    void createUserShouldReturnOkWhenBusinessLayerCreatesUser() {
        User user = buildUser("user-1", true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userBO.createUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(user, response.getBody());
    }

    @Test
    void createUserShouldReturnBadRequestWhenBusinessLayerReturnsNull() {
        User user = buildUser("user-1", true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userBO.createUser(user)).thenReturn(null);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void updateUserShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByUuid("missing")).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(Exceptions.ResourceNotFoundException.class,
                () -> userController.updateUser("missing", new UserDTO()));

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void updateUserShouldReturnOkWhenBusinessLayerUpdatesUser() {
        User user = buildUser("user-1", true);
        UserDTO dto = new UserDTO();
        when(userRepository.findByUuid("user-1")).thenReturn(user);
        when(userBO.updateUser("user-1", dto)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser("user-1", dto);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(user, response.getBody());
    }

    @Test
    void updateUserShouldReturnBadRequestWhenBusinessLayerReturnsNull() {
        User user = buildUser("user-1", true);
        UserDTO dto = new UserDTO();
        when(userRepository.findByUuid("user-1")).thenReturn(user);
        when(userBO.updateUser("user-1", dto)).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser("user-1", dto);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void deleteUserShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByUuid("missing")).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(Exceptions.ResourceNotFoundException.class,
                () -> userController.deleteUser("missing"));

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void deleteUserShouldReturnNoContentWhenDeletionSucceeds() {
        User user = buildUser("user-1", true);
        when(userRepository.findByUuid("user-1")).thenReturn(user);
        when(userBO.deleteUser("user-1")).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUser("user-1");

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void deleteUserShouldReturnBadRequestWhenDeletionFails() {
        User user = buildUser("user-1", true);
        when(userRepository.findByUuid("user-1")).thenReturn(user);
        when(userBO.deleteUser("user-1")).thenReturn(false);

        ResponseEntity<?> response = userController.deleteUser("user-1");

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void updateUserPasswordShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByUuid("missing")).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(Exceptions.ResourceNotFoundException.class,
                () -> userController.updateUserPassword("missing", "nova"));

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void updateUserPasswordShouldReturnOkWhenPasswordIsUpdated() {
        User user = buildUser("user-1", true);
        when(userRepository.findByUuid("user-1")).thenReturn(user);
        when(userBO.updateUserPassword("user-1", "nova")).thenReturn(true);

        ResponseEntity<?> response = userController.updateUserPassword("user-1", "nova");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(userBO).updateUserPassword("user-1", "nova");
    }

    @Test
    void updateUserPasswordShouldReturnBadRequestWhenUpdateFails() {
        User user = buildUser("user-1", true);
        when(userRepository.findByUuid("user-1")).thenReturn(user);
        when(userBO.updateUserPassword("user-1", "nova")).thenReturn(false);

        ResponseEntity<?> response = userController.updateUserPassword("user-1", "nova");

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    private User buildUser(String uuid, boolean active) {
        User user = new User();
        user.setUuid(uuid);
        user.setEmail(uuid + "@email.com");
        user.setPassword("senha");
        user.setRole(UserRoleEnum.ADMINISTRATOR);
        user.setActive(active);
        user.setDeleted(false);
        return user;
    }
}
