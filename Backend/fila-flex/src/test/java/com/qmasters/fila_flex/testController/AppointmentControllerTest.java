package com.qmasters.fila_flex.testController;

import com.qmasters.fila_flex.dto.AppointmentDTO;
import com.qmasters.fila_flex.model.Appointment;
import com.qmasters.fila_flex.service.AppointmentService;
import com.qmasters.fila_flex.util.PriorityCondition;
import com.qmasters.fila_flex.controller.AppointmentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setScheduledDateTime(LocalDateTime.now());
    }

    @Test
    void testGetAllAppointment() {
        // Arrange
        List<Appointment> appointments = List.of(appointment);
        when(appointmentService.getAllAppointment()).thenReturn(appointments);

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAllAppointment();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointments, response.getBody());
    }

    @Test
    void testCreateAppointment() {
        // Arrange
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setScheduledDateTime(LocalDateTime.now());
        when(appointmentService.saveAppointment(any(AppointmentDTO.class))).thenReturn(appointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.createAppointment(appointmentDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
    }

    @Test
    void testGetAppointmentById() {
        // Arrange
        when(appointmentService.findAppointmentById(1L)).thenReturn(Optional.of(appointment));

        // Act
        ResponseEntity<Optional<Appointment>> response = appointmentController.getAppointmentById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(appointment, response.getBody().get());
    }

    @Test
    void testGetAppointmentById_NotFound() {
        // Arrange
        when(appointmentService.findAppointmentById(1L)).thenReturn(Optional.empty());

        // Act
        try {
            appointmentController.getAppointmentById(1L);
        } catch (NoSuchElementException e) {
            // Assert
            assertEquals("Agendamento não encontrado", e.getMessage());
        }
    }

    @Test
    void testSetPriorityCondition() {
        // Arrange
        PriorityCondition priorityCondition = mock(PriorityCondition.class);
        when(appointmentService.setPriorityCondition(1L, priorityCondition)).thenReturn(appointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.setPriorityCondition(1L, priorityCondition);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
    }

    @Test
    void testDeleteAppointmentById() {
        // Arrange
        doNothing().when(appointmentService).deleteAppointment(1L);

        // Act
        ResponseEntity<String> response = appointmentController.deleteAppointmentById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Agendamento removido com sucesso", response.getBody());
    }

    @Test
    void testDeleteAppointmentById_NotFound() {
        // Arrange
        doThrow(new IllegalArgumentException("Agendamento não encontrado")).when(appointmentService).deleteAppointment(1L);

        // Act
        ResponseEntity<String> response = appointmentController.deleteAppointmentById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Agendamento não encontrado", response.getBody());
    }
}
