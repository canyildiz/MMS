package mms.controller;

import java.util.*;
import mms.model.Equipment;

public class MMS implements java.io.Serializable {

    Map<Integer, Equipment> equipments;

    public MMS() {
        equipments = new HashMap<>();
    }

    public Map<Integer, Equipment> getEquipments() {
        return equipments;
    }

    public Equipment getEquipment(int id) {
        return equipments.get(id);
    }

    public void addOrChangeEquipment(int id, String name, Date lastMaintenanceDate, int lastErrorCode) {
        Equipment equipment;

        if (equipments.containsKey(id)) {
            equipment = getEquipment(id);
        } else {
            equipment = new Equipment(id, name);
        }
        
        equipment.setEquipmentName(name);
        equipment.setLastMaintenanceDate(lastMaintenanceDate);
        equipment.setLastErrorCode(lastErrorCode);
        equipments.put(id, equipment);
    }

    public void deleteEquipment(int id) {
        equipments.remove(id);
    }
}
