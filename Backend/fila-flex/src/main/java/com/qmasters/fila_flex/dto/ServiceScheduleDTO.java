package com.qmasters.fila_flex.dto;

import com.qmasters.fila_flex.model.AppointmentType;

import java.util.List;

public class ServiceScheduleDTO {
    private AppointmentType appointmentType;
    private List<String> days;

    private String appointmentTypeId; //transient
    private String appointmentTypeName; //transient
    private String appointmentTypeDescription; //transient

    public ServiceScheduleDTO() {
    }

    public ServiceScheduleDTO(AppointmentType appointmentType, List<String> days) {
        this.appointmentType = appointmentType;
        this.days = days;

        this.appointmentTypeId = appointmentType.getId().toString();
        this.appointmentTypeName = appointmentType.getAppointmentTypeDetails().getName();
        this.appointmentTypeDescription = appointmentType.getAppointmentTypeDetails().getDescription();
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(String appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public String getAppointmentTypeName() {
        return appointmentTypeName;
    }

    public void setAppointmentTypeName(String appointmentTypeName) {
        this.appointmentTypeName = appointmentTypeName;
    }

    public String getAppointmentTypeDescription() {
        return appointmentTypeDescription;
    }

    public void setAppointmentTypeDescription(String appointmentTypeDescription) {
        this.appointmentTypeDescription = appointmentTypeDescription;
    }
}
