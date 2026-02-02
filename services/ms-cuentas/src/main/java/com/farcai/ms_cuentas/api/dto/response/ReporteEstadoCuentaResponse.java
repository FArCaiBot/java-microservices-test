package com.farcai.ms_cuentas.api.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ReporteEstadoCuentaResponse(ClienteReporteResponse cliente,
        LocalDate desde,
        LocalDate hasta,
        List<CuentaReporteResponse> cuentas) {

}
