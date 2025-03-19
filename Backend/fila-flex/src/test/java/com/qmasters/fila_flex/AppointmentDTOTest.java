package com.qmasters.fila_flex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.qmasters.fila_flex.dto.AppointmentDTO;
import com.qmasters.fila_flex.model.Adress;
import com.qmasters.fila_flex.model.Appointment;
import com.qmasters.fila_flex.model.AppointmentType;
import com.qmasters.fila_flex.model.User;
import com.qmasters.fila_flex.util.UserRole;

class AppointmentDTOTest {

    private AppointmentType appointmentType;
    private User user;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        // Setup Adress for AppointmentType
        Adress adress = new Adress("123", "Rua X", "Cidade Y", "Estado Z", "Brasil");
        
        // Setup AppointmentType
        appointmentType = new AppointmentType(
            "Corte de Cabelo", 
            "Corte de cabelo masculino",
            Arrays.asList("Higiene", "Beleza"),
            50.0,
            30,
            LocalDate.now(),
            Arrays.asList("Documento de Identidade"),
            adress
        );

        // Setup User
        user = new User("cliente@exemplo.com", "senha123", UserRole.USER, "Cliente Exemplo");

        // Setup Appointment
        appointment = new Appointment(appointmentType, user, LocalDateTime.now());
    }

    @Test
    void testAppointmentDTO() {
        // Criar AppointmentDTO
        AppointmentDTO appointmentDTO = new AppointmentDTO(appointmentType, user, appointment.getScheduledDateTime(), appointment.getCreatedDateTime());

        // Verificar se o AppointmentDTO foi corretamente construído
        assertNotNull(appointmentDTO);
        assertEquals(appointmentType.getName(), appointmentDTO.getAppointmentTypeName());
        assertEquals(appointmentType.getDescription(), appointmentDTO.getAppointmentTypeDescription());
        assertEquals(appointmentType.getPrice(), Double.parseDouble(appointmentDTO.getAppointmentTypePrice()));
        assertEquals(appointmentType.getEstimatedTime().toString(), appointmentDTO.getAppointmentTypeEstimatedTime());
        assertEquals(appointmentType.getCategory(), appointmentDTO.getAppointmentTypeCategory());
        assertEquals(appointmentType.getRequiredDocumentation(), appointmentDTO.getAppointmentTypeRequiredDocumentation());
        assertEquals(appointmentType.getAdressAsString(), appointmentDTO.getAppointmentTypeAdress());

        // Verificar informações do usuário
        assertEquals(user.getEmail(), appointmentDTO.getUserEmail());
        assertEquals(user.getId().toString(), appointmentDTO.getUserId());

        // Verificar datas
        assertNotNull(appointmentDTO.getScheduledDateTime());
        assertNotNull(appointmentDTO.getCreatedDateTime());
    }
}
