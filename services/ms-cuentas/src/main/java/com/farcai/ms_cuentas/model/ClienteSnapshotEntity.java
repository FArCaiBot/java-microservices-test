package com.farcai.ms_cuentas.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente_snapshot")
public class ClienteSnapshotEntity {

    @Id
    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String identificacion;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}

