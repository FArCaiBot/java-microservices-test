package com.farcai.ms_clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import com.farcai.ms_clientes.dto.ClienteRequest;
import com.farcai.ms_clientes.exception.NotFoundException;
import com.farcai.ms_clientes.mapper.ClienteMapper;
import com.farcai.ms_clientes.messaging.dto.ClienteEventMessage;
import com.farcai.ms_clientes.messaging.rabbit.ClienteEventsPublisher;
import com.farcai.ms_clientes.messaging.rabbit.TxAfterCommitPublisher;
import com.farcai.ms_clientes.model.ClienteEntity;
import com.farcai.ms_clientes.model.PersonaEntity;
import com.farcai.ms_clientes.repository.ClienteJpaRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteJpaRepository clienteJpaRepository;
    @Mock
    private ClienteMapper mapper;
    @Mock
    private ClienteEventsPublisher publisher;
    @Mock
    private TxAfterCommitPublisher afterCommit;

    @Test
    void crear_debeGuardarYPublicarEvento() {
        ClienteService service = new ClienteService(clienteJpaRepository, mapper, publisher, afterCommit);
        ClienteRequest req = request();
        ClienteEntity nuevo = cliente(1L, true);
        ClienteEntity guardado = cliente(10L, true);
        ClienteEventMessage evento = new ClienteEventMessage("e1", "ClienteCreado", Instant.now(), 1, null);

        when(mapper.toNewEntity(req)).thenReturn(nuevo);
        when(clienteJpaRepository.save(nuevo)).thenReturn(guardado);
        when(mapper.toEventMessage(anyString(), eq("ClienteCreado"), any(Instant.class), eq(guardado))).thenReturn(evento);
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(0);
            action.run();
            return null;
        }).when(afterCommit).runAfterCommit(any(Runnable.class));

        ClienteEntity resultado = service.crear(req);

        assertSame(guardado, resultado);
        verify(clienteJpaRepository).save(nuevo);
        verify(afterCommit).runAfterCommit(any(Runnable.class));
        verify(publisher).publish("clientes.created", evento);
    }

    @Test
    void listar_debeRetornarTodos() {
        ClienteService service = new ClienteService(clienteJpaRepository, mapper, publisher, afterCommit);
        List<ClienteEntity> clientes = List.of(cliente(1L, true), cliente(2L, false));
        when(clienteJpaRepository.findAll()).thenReturn(clientes);

        List<ClienteEntity> resultado = service.listar();

        assertEquals(clientes, resultado);
        verify(clienteJpaRepository).findAll();
    }

    @Test
    void actualizar_debeModificarGuardarYPublicarEvento() {
        ClienteService service = new ClienteService(clienteJpaRepository, mapper, publisher, afterCommit);
        Long id = 15L;
        ClienteRequest req = request();
        ClienteEntity actual = cliente(id, true);
        ClienteEntity guardado = cliente(id, true);
        ClienteEventMessage evento = new ClienteEventMessage("e2", "ClienteActualizado", Instant.now(), 1, null);

        when(clienteJpaRepository.findById(id)).thenReturn(Optional.of(actual));
        when(clienteJpaRepository.save(actual)).thenReturn(guardado);
        when(mapper.toEventMessage(anyString(), eq("ClienteActualizado"), any(Instant.class), eq(guardado))).thenReturn(evento);
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(0);
            action.run();
            return null;
        }).when(afterCommit).runAfterCommit(any(Runnable.class));

        ClienteEntity resultado = service.actualizar(id, req);

        assertSame(guardado, resultado);
        verify(mapper).updateEntity(actual, req);
        verify(clienteJpaRepository).save(actual);
        verify(publisher).publish("clientes.updated", evento);
    }

    @Test
    void desactivar_debeCambiarEstadoGuardarYPublicarEvento() {
        ClienteService service = new ClienteService(clienteJpaRepository, mapper, publisher, afterCommit);
        Long id = 22L;
        ClienteEntity actual = cliente(id, true);
        ClienteEntity guardado = cliente(id, false);
        ClienteEventMessage evento = new ClienteEventMessage("e3", "ClienteDesactivado", Instant.now(), 1, null);

        when(clienteJpaRepository.findById(id)).thenReturn(Optional.of(actual));
        when(clienteJpaRepository.save(actual)).thenReturn(guardado);
        when(mapper.toEventMessage(anyString(), eq("ClienteDesactivado"), any(Instant.class), eq(guardado))).thenReturn(evento);
        doAnswer(invocation -> {
            Runnable action = invocation.getArgument(0);
            action.run();
            return null;
        }).when(afterCommit).runAfterCommit(any(Runnable.class));

        ClienteEntity resultado = service.desactivar(id);

        assertSame(guardado, resultado);
        assertEquals(false, actual.getEstado());
        verify(clienteJpaRepository).save(actual);
        verify(publisher).publish("clientes.disabled", evento);
    }

    @Test
    void obtener_debeRetornarClienteCuandoExiste() {
        ClienteService service = new ClienteService(clienteJpaRepository, mapper, publisher, afterCommit);
        Long id = 99L;
        ClienteEntity cliente = cliente(id, true);
        when(clienteJpaRepository.findById(id)).thenReturn(Optional.of(cliente));

        ClienteEntity resultado = service.obtener(id);

        assertSame(cliente, resultado);
        verify(clienteJpaRepository).findById(id);
    }

    @Test
    void obtener_debeLanzarNotFoundCuandoNoExiste() {
        ClienteService service = new ClienteService(clienteJpaRepository, mapper, publisher, afterCommit);
        Long id = 100L;
        when(clienteJpaRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.obtener(id));

        assertEquals("Cliente no encontrado: 100", ex.getMessage());
        verify(clienteJpaRepository).findById(id);
    }

    private static ClienteRequest request() {
        return new ClienteRequest(
                "Juan Perez",
                "M",
                30,
                "0123456789",
                "Av. Siempre Viva",
                "0987654321",
                "secret123");
    }

    private static ClienteEntity cliente(Long id, boolean estado) {
        PersonaEntity persona = new PersonaEntity();
        persona.setNombre("Juan Perez");
        persona.setIdentificacion("0123456789");

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(id);
        cliente.setPersona(persona);
        cliente.setContrasena("secret123");
        cliente.setEstado(estado);
        return cliente;
    }
}
