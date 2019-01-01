package mms.model;

import java.util.Date;
import mms.Main;

public class MaintenanceRequest implements java.io.Serializable {

    private final int maintenanceID;
    private int errorCode = -1;
    private String description;
    private int equipmentId = -1;
    private int priority = -1;

    public MaintenanceRequest(int Id) {
        maintenanceID = Id;
    }

    public int getMaintenanceID() {
        return maintenanceID;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Equipment getEquipment() {
        return Main.controller.getEquipment(equipmentId);
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setEquipment(Equipment equipment) {
        this.equipmentId = equipment.getEquipmentID();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
