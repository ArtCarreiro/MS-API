package com.amc.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amc.api.dto.UserDTO;
import com.amc.api.entities.User;
import com.amc.api.enums.UserRoleEnum;
import com.amc.api.repositories.UserRepository;
import com.amc.api.utils.Exceptions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserShouldEncodePasswordAndSave() {
        User user = buildUser();

        when(userRepository.findByUuid(user.getUuid())).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertSame(user, result);
        assertEquals("encoded-password", user.getPassword());
        assertEquals(UserRoleEnum.ADMINISTRATOR, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void createUserShouldThrowWhenUserIsNotFoundInValidation() {
        User user = buildUser();

        when(userRepository.findByUuid(user.getUuid())).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(
                Exceptions.ResourceNotFoundException.class,
                () -> userService.createUser(user));

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void createUserShouldThrowWhenEmailAlreadyExists() {
        User user = buildUser();

        when(userRepository.findByUuid(user.getUuid())).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(new User());

        Exceptions.DatabaseException exception = assertThrows(
                Exceptions.DatabaseException.class,
                () -> userService.createUser(user));

        assertEquals("Erro no banco de dados: Já existe um usuário com o e-mail: user@test.com.", exception.getMessage());
    }

    @Test
    void updateUserShouldMapAndSaveWhenValidationPasses() {
        User user = buildUser();
        UserDTO dto = new UserDTO();
        dto.setEmail("updated@test.com");

        when(userRepository.findByUuid(user.getUuid())).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user.getUuid(), dto);

        assertSame(user, result);
        verify(mapper).map(dto, user);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldWrapUnexpectedErrors() {
        User user = buildUser();
        UserDTO dto = new UserDTO();
        RuntimeException failure = new RuntimeException("mapper-failed");

        when(userRepository.findByUuid(user.getUuid())).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        doThrow(failure).when(mapper).map(dto, user);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.updateUser(user.getUuid(), dto));

        assertSame(failure, exception.getCause());
    }

    @Test
    void updateUserPasswordShouldEncodeAndSaveNewPassword() {
        User user = buildUser();

        when(userRepository.findByUuid(user.getUuid())).thenReturn(user);
        when(passwordEncoder.encode("nova-senha")).thenReturn("encoded-new-password");
        when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.updateUserPassword(user.getUuid(), "nova-senha");

        assertTrue(result);
        assertEquals("encoded-new-password", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserPasswordShouldWrapUnexpectedErrors() {
        RuntimeException failure = new RuntimeException("save-failed");
        User user = buildUser();

        when(userRepository.findByUuid(user.getUuid())).thenReturn(user);
        when(passwordEncoder.encode("nova-senha")).thenReturn("encoded-new-password");
        doThrow(failure).when(userRepository).save(user);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.updateUserPassword(user.getUuid(), "nova-senha"));

        assertSame(failure, exception.getCause());
    }

    @Test
    void deleteUserShouldReturnTrueWhenRepositoryDeletes() {
        boolean result = userService.deleteUser("user-uuid");

        assertTrue(result);
        verify(userRepository).deleteUserByUuid("user-uuid");
    }

    @Test
    void deleteUserShouldWrapRepositoryErrors() {
        RuntimeException failure = new RuntimeException("delete-failed");
        doThrow(failure).when(userRepository).deleteUserByUuid("user-uuid");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser("user-uuid"));

        assertSame(failure, exception.getCause());
    }

    @Test
    void validateUserShouldThrowWhenUuidIsNotFound() {
        User user = buildUser();

        when(userRepository.findByUuid(user.getUuid())).thenReturn(null);

        assertThrows(Exceptions.ResourceNotFoundException.class, () -> userService.validateUser(user));
    }

    @Test
    void validateUserShouldThrowWhenEmailAlreadyExists() {
        User user = buildUser();

        when(userRepository.findByUuid(user.getUuid())).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(new User());

        assertThrows(Exceptions.DatabaseException.class, () -> userService.validateUser(user));
    }

    private User buildUser() {
        User user = new User();
        user.setUuid("user-uuid");
        user.setEmail("user@test.com");
        user.setPassword("123456");
        user.setRole(UserRoleEnum.ADMINISTRATOR);
        return user;
    }
}
