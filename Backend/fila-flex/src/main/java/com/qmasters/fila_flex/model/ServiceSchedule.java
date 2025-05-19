package com.qmasters.fila_flex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "service_schedules")
public class ServiceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_type_id", nullable = false)
    @JsonIgnore
    private AppointmentType appointmentType;

    @ElementCollection
    @CollectionTable(name = "service_schedule_days", joinColumns = @JoinColumn(name = "service_schedule_id"))
    @Column(name = "day_of_week")
    private List<String> days; //lista com os dias da semana cujo atendimento é realizado

    //========================= Construtores ==========================

    public ServiceSchedule() {
        this.days = new ArrayList<>();
    }

    public ServiceSchedule(AppointmentType appointmentType ,List<String> days) {
        this.appointmentType = appointmentType;
        this.days = days;
    }

    //========================= Getters e Setters Transients ==========================

    public String getAppointmentTypeId() {
        if (appointmentType != null) {
            return appointmentType.getId().toString();
        }
        return null;
    }

    public String getAppointmentTypeName() {
        if (appointmentType != null && appointmentType.getAppointmentTypeDetails() != null) {
            return appointmentType.getAppointmentTypeDetails().getName();
        }
        return null;
    }

    public String getAppointmentTypeDescription() {
        if (appointmentType != null && appointmentType.getAppointmentTypeDetails() != null) {
            return appointmentType.getAppointmentTypeDetails().getDescription();
        }
        return null;
    }

    //========================= Getters e Setters ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }
}
