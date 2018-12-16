package mms.model;

import java.util.Date;

public class Equipment implements java.io.Serializable {

    private final int equipmentID;
    private String equipmentName = "";
    private Date lastMaintenanceDate = null;
    private int lastErrorCode = -1;

    public Equipment(int id, String name) {
        equipmentID = id;
        equipmentName = name;
    }

    public int getEquipmentID() {
        return equipmentID;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public int getLastErrorCode() {
        return lastErrorCode;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public void setLastMaintenanceDate(Date lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public void setLastErrorCode(int lastErrorCode) {
        this.lastErrorCode = lastErrorCode;
    }
}
