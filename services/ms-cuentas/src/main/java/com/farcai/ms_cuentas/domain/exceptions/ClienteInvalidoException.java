package com.farcai.ms_cuentas.domain.exceptions;

public class ClienteInvalidoException extends RuntimeException {
    public ClienteInvalidoException(String message) {
        super(message);
    }

}
