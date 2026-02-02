package com.farcai.ms_cuentas.infrastructure.persistence.jpa.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.farcai.ms_cuentas.domain.ports.ClienteSnapshotRepositoryPort;
import com.farcai.ms_cuentas.infrastructure.persistence.jpa.repository.ClienteSnapshotJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ClienteSnapshotRepositoryAdapter implements ClienteSnapshotRepositoryPort {
    private final ClienteSnapshotJpaRepository jpa;

    @Override
    public Optional<ClienteSnapshotView> findById(Long clienteId) {
        return jpa.findById(clienteId)
                .map(e -> new ClienteSnapshotView(
                        e.getClienteId(),
                        e.getNombre(),
                        e.getIdentificacion(),
                        e.getEstado()));
    }

}
