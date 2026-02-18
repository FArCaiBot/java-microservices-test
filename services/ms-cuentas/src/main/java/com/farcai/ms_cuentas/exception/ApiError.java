package com.farcai.ms_cuentas.exception;

import java.time.Instant;

public record ApiError(String code, String message, Instant timestamp) {
}
