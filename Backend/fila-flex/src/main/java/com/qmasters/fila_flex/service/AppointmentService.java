package com.qmasters.fila_flex.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.qmasters.fila_flex.exception.InvalidDayException;
import com.qmasters.fila_flex.model.AppointmentType;
import com.qmasters.fila_flex.model.ServiceSchedule;
import com.qmasters.fila_flex.model.User;
import com.qmasters.fila_flex.repository.AppointmentTypeRepository;
import com.qmasters.fila_flex.repository.ServiceScheduleRepository;
import com.qmasters.fila_flex.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.qmasters.fila_flex.dto.AppointmentDTO;
import com.qmasters.fila_flex.exception.InvalidDateRangeException;
import com.qmasters.fila_flex.exception.TooLateToChangeException;
import com.qmasters.fila_flex.model.Appointment;
import com.qmasters.fila_flex.repository.AppointmentRepository;
import com.qmasters.fila_flex.util.PriorityCondition;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    private final AppointmentTypeRepository appointmentTypeRepository;

    private final UserRepository userRepository;

    private final ServiceScheduleService serviceScheduleService;

    private final QueueService queueService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              AppointmentTypeRepository appointmentTypeRepository,
                              UserRepository userRepository,
                              ServiceScheduleService serviceScheduleService,
                              QueueService queueService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentTypeRepository = appointmentTypeRepository;
        this.userRepository = userRepository;
        this.serviceScheduleService = serviceScheduleService;
        this.queueService = queueService;
    }

    @Transactional
    public Appointment saveAppointment(AppointmentDTO dto) {
        AppointmentType appointmentType = appointmentTypeRepository.findById(dto.getAppointmentType().getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Tipo de atendimento não encontrado: " + dto.getAppointmentType().getId()));

        User user = userRepository.findById(dto.getUser().getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Usuário não encontrado: " + dto.getUserId()));

        Appointment appointment = new Appointment(appointmentType, user, dto.getScheduledDateTime());

        List<ServiceSchedule> schedules = appointmentType.getServiceSchedule();
        if (schedules == null || schedules.isEmpty()) {
            throw new InvalidDayException(
                    "Não há horários disponíveis para o tipo de agendamento selecionado.");
        }

        DayOfWeek selected = appointment.getScheduledDateTime().getDayOfWeek();
        boolean ok = schedules.stream()
                .anyMatch(s -> s.getDays().contains(selected.name()));

        if (!ok) {
            throw new InvalidDayException(
                    "O dia selecionado não está disponível para o tipo de agendamento.");
        }

        appointment = queueService.assignQueuePosition(appointment);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointment() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    

    public Appointment setPriorityCondition(Long appointmentId, PriorityCondition priorityCondition) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if(optionalAppointment.isEmpty()){
            throw new NoSuchElementException("Agendamento não encontrado.");
        } 

        Appointment appointment = optionalAppointment.get();

        appointment.setPriorityCondition(priorityCondition);
        appointmentRepository.save(appointment);

        if (priorityCondition != PriorityCondition.NO_PRIORITY) {
            queueService.insertWithPriority(appointmentId);
        }
        
        return appointment;
    }

    //função para buscar Appointment por intervalo de datas.
    public List<Appointment> findByScheduledDateTime(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Data de início não pode ser posterior a data final");
        } 
        return appointmentRepository.findByScheduledDateTime(startDate, endDate);
    }

    public List<Appointment> findFullAppointmentsByUserId(Long userId) { //talvez função desatualizada
        if (userId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        return appointmentRepository.findByUserId(userId);
    }
    
    @Transactional
    public Appointment updateAppointment(Long id, AppointmentDTO appointmentDto) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            LocalDateTime now = LocalDateTime.now();

            if (appointmentDto.getScheduledDateTime().isAfter(now.plusHours(12))) {//se o agendamento vai ocorrer em mais de 12 horas permite reagendar
                LocalDateTime createdDateTime = appointment.getCreatedDateTime();//mantem a data de criação original
                
                appointment.setScheduledDateTime(appointmentDto.getScheduledDateTime());
                appointment.setCreatedDateTime(createdDateTime);
                return appointmentRepository.save(appointment);

            } else {
                throw new TooLateToChangeException("Só é possível reagendar uma consulta com pelo menos 12 horas de antecedência.");
            }
        } else {
            throw new NoSuchElementException("Agendamento não encontrado.");
        }
    }

    @Transactional
    public void deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            //captura informações necessárias para reorganização da fila.
            var appointmentTemp = appointmentRepository.findById(id);
            Long appointmentTypeId = appointmentTemp.get().getAppointmentType().getId();
            Integer queuePosition = appointmentTemp.get().getQueueOrder();
            
            queueService.reorganizeQueueAfterRemoval(appointmentTypeId, queuePosition);
            
            appointmentRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Agendamento não encontrado, remoção não foi realizada");
        }
    }
}