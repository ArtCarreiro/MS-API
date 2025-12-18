package com.amc.api.Utils;

public class Exceptions {

    //Recurso não encontrado
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String resourceName, Object id) {
            super(resourceName + " com ID " + id + " não foi encontrado.");
        }

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    //Requisição inválida
    public static class InvalidRequestException extends RuntimeException {
        public InvalidRequestException(String message) {
            super("Requisição inválida: " + message);
        }
    }

    //Acesso não autorizado
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException() {
            super("Acesso não autorizado.");
        }

        public UnauthorizedException(String message) {
            super(message);
        }
    }

    //Operação bloqueada por regra de negócio
    public static class OperationNotAllowedException extends RuntimeException {
        public OperationNotAllowedException(String message) {
            super("Operação não permitida: " + message);
        }
    }

    //Erro de banco de dados
    public static class DatabaseException extends RuntimeException {
        public DatabaseException(String message) {
            super("Erro no banco de dados: " + message);
        }
    }

    //Exceção genérica
    public static class GenericException extends RuntimeException {
        public GenericException(String message) {
            super("Erro inesperado: " + message);
        }
    }
}
