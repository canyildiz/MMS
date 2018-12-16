package mms.controller;

import java.util.*;
import mms.model.Equipment;
import mms.model.TeamMember;
import mms.model.TimeSlot;

public class MMS implements java.io.Serializable {

    Map<Integer, Equipment> equipments;
    Map<Integer, TeamMember> teamMembers;

    public MMS() {
        equipments = new HashMap<>();
        teamMembers = new HashMap<>();
    }
    
    // TEAM MEMBERS
    public Map<Integer, TeamMember> getTeamMembers() {
        return teamMembers;
    }
    
    public TeamMember getTeamMember(int id) {
        return teamMembers.get(id);
    }
    
    public void deleteTeamMember(int id) {
        teamMembers.remove(id);
    }
    
    public void addOrChangeTeamMember(int id, String name, List<Date[]> timeSlots) {
        List<TimeSlot> timeSlotsToAdd = new ArrayList<>();
        for(int i = 0; i<timeSlots.size(); i++) {
            Date[] dates = timeSlots.get(i);
            timeSlotsToAdd.add(new TimeSlot(dates[0], dates[1]));
        }
        TeamMember teamMember = teamMembers.get(id);
        if (null == teamMember) {
            teamMember = new TeamMember(id);
        }
        teamMember.setName(name);
        teamMember.setTimeSlots(timeSlotsToAdd);
        teamMembers.put(id, teamMember);
    }

    // EQUIPMENTS
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
