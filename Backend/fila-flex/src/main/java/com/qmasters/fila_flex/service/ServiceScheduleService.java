package com.qmasters.fila_flex.service;

import com.qmasters.fila_flex.dto.ServiceScheduleDTO;
import com.qmasters.fila_flex.model.ServiceSchedule;
import com.qmasters.fila_flex.repository.ServiceScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceScheduleService {

    private final ServiceScheduleRepository serviceScheduleRepository;

    public ServiceScheduleService(ServiceScheduleRepository serviceScheduleRepository) {
        this.serviceScheduleRepository = serviceScheduleRepository;
    }

    @Transactional
    public ServiceSchedule saveServiceSchedule(ServiceScheduleDTO serviceScheduleDTO) {
        ServiceSchedule serviceSchedule = new ServiceSchedule(
            serviceScheduleDTO.getAppointmentType(),
            serviceScheduleDTO.getDays()
        );

        return serviceScheduleRepository.save(serviceSchedule);
    }

    public List<ServiceSchedule> listAll() {
        return serviceScheduleRepository.findAll();
    }

    public Optional<ServiceSchedule> findById(Long id) {
        return serviceScheduleRepository.findById(id);
    }

    @Transactional
    public void deleteServiceSchedule(Long id) {
        if (serviceScheduleRepository.existsById(id)) {
            serviceScheduleRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Service schedule not found with id: " + id);
        }
    }

}
