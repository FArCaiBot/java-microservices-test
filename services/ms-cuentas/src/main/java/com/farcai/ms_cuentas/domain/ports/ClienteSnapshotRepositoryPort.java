package com.farcai.ms_cuentas.domain.ports;

import java.util.Optional;

public interface ClienteSnapshotRepositoryPort {

    Optional<ClienteSnapshotView> findById(Long clienteId);

    record ClienteSnapshotView(
            Long clienteId,
            String nombre,
            String identificacion,
            Boolean estado) {
    }

}
