package com.amc.api.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserShouldEncodePasswordAndPersistUser() {
        User user = buildUser();
        when(passwordEncoder.encode("senha")).thenReturn("senha-criptografada");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertSame(user, result);
        assertEquals("senha-criptografada", user.getPassword());
        assertEquals(UserRoleEnum.ADMINISTRATOR, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void createUserShouldWrapUnexpectedErrors() {
        User user = buildUser();
        when(passwordEncoder.encode("senha")).thenReturn("senha-criptografada");
        when(userRepository.save(user)).thenThrow(new IllegalStateException("falha ao salvar"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(user));

        assertInstanceOf(IllegalStateException.class, exception.getCause());
    }

    @Test
    void updateUserShouldMapAndSaveWhenUserExists() {
        User user = buildUser();
        UserDTO dto = new UserDTO();
        dto.setEmail("novo@email.com");
        when(userRepository.findByUuid("user-1")).thenReturn(user);

        User result = userService.updateUser("user-1", dto);

        assertSame(user, result);
        verify(modelMapper).map(dto, user);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldWrapNotFoundError() {
        when(userRepository.findByUuid("missing")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser("missing", new UserDTO()));

        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("Usuário não encontrado", exception.getCause().getMessage());
    }

    @Test
    void updateUserPasswordShouldEncodePasswordAndSaveUser() {
        User user = buildUser();
        when(userRepository.findByUuid("user-1")).thenReturn(user);
        when(passwordEncoder.encode("nova")).thenReturn("nova-criptografada");

        boolean updated = userService.updateUserPassword("user-1", "nova");

        assertTrue(updated);
        assertEquals("nova-criptografada", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserPasswordShouldWrapUnexpectedErrors() {
        when(userRepository.findByUuid("user-1")).thenReturn(null);
        when(passwordEncoder.encode("nova")).thenReturn("nova-criptografada");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUserPassword("user-1", "nova"));

        assertInstanceOf(NullPointerException.class, exception.getCause());
    }

    @Test
    void deleteUserShouldReturnTrueWhenRepositoryDeletes() {
        boolean deleted = userService.deleteUser("user-1");

        assertTrue(deleted);
        verify(userRepository).deleteUserByUuid("user-1");
    }

    @Test
    void deleteUserShouldWrapUnexpectedErrors() {
        doThrow(new IllegalStateException("falha ao deletar")).when(userRepository).deleteUserByUuid("user-1");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser("user-1"));

        assertInstanceOf(IllegalStateException.class, exception.getCause());
    }

    @Test
    void validationUserShouldReturnUserWhenItIsValid() {
        User user = buildUser();
        user.setDeleted(false);
        when(userRepository.findByUuid("user-1")).thenReturn(user);

        User result = userService.validationUser("user-1");

        assertSame(user, result);
    }

    @Test
    void validationUserShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByUuid("missing")).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(Exceptions.ResourceNotFoundException.class,
                () -> userService.validationUser("missing"));

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    @Test
    void validationUserShouldThrowWhenUserIsDeleted() {
        User user = buildUser();
        user.setDeleted(true);
        when(userRepository.findByUuid("user-1")).thenReturn(user);

        Exceptions.DatabaseException exception = assertThrows(Exceptions.DatabaseException.class,
                () -> userService.validationUser("user-1"));

        assertEquals("Erro no banco de dados: Usuário deletado.", exception.getMessage());
    }

    @Test
    void validationUserShouldNotCallSaveDuringValidation() {
        User user = buildUser();
        when(userRepository.findByUuid("user-1")).thenReturn(user);

        assertDoesNotThrow(() -> userService.validationUser("user-1"));
        verify(userRepository, never()).save(any());
    }

    private User buildUser() {
        User user = new User();
        user.setUuid("user-1");
        user.setEmail("user@email.com");
        user.setPassword("senha");
        user.setRole(UserRoleEnum.ADMINISTRATOR);
        user.setDeleted(false);
        user.setActive(true);
        return user;
    }
}
