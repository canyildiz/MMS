package mms.controller;

import java.util.*;
import mms.model.MaintenanceSchedule;
import mms.model.Equipment;
import mms.model.MaintenanceRequest;
import mms.model.TeamMember;
import mms.model.TimeSlot;

public class MMS implements java.io.Serializable {

    Map<Integer, Equipment> equipments;
    Map<Integer, TeamMember> teamMembers;
    Map<Integer, MaintenanceRequest> maintenanceRequests;
    Map<Integer, MaintenanceSchedule> maintenanceSchedules;

    public MMS() {
        equipments = new HashMap<>();
        teamMembers = new HashMap<>();
        maintenanceRequests = new HashMap<>();
        maintenanceSchedules = new HashMap<>();
    }

    // <editor-fold defaultstate="collapsed" desc="TEAM MEMBERS">
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
        for (int i = 0; i < timeSlots.size(); i++) {
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

    public List<TeamMember> getAvailableTeamMembers(Date dueDate) {
        List<TeamMember> retVal = new ArrayList<>();

        for (Map.Entry<Integer, TeamMember> item : teamMembers.entrySet()) {
            TeamMember tm = item.getValue();
            List<TimeSlot> timeSlots = tm.getTimeSlots();
            for (TimeSlot ts : timeSlots) {
                if (ts.getEndTime().before(dueDate)) {
                    retVal.add(tm);
                    break;
                }
            }
        }
        return retVal;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EQUIPMENTS">
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CORRECTIVE MAINTENANCE REQUESTS">
    public Map<Integer, MaintenanceRequest> getMaintenanceRequests() {
        return maintenanceRequests;
    }

    public MaintenanceRequest getMaintenanceRequest(int id) {
        return maintenanceRequests.get(id);
    }

    public void addOrChangeMaintenanceRequest(int id, String description, int errorCode, int priority, int equipmentId) {
        MaintenanceRequest request;

        if (maintenanceRequests.containsKey(id)) {
            request = getMaintenanceRequest(id);
        } else {
            request = new MaintenanceRequest(id);
        }

        request.setDescription(description);
        request.setErrorCode(errorCode);
        request.setPriority(priority);
        request.setEquipmentId(equipmentId);
        maintenanceRequests.put(id, request);
    }

    public void deleteMaintenanceRequest(int id) {
        maintenanceRequests.remove(id);
    }

    public List<MaintenanceRequest> getWaitingRequests() {
        List<MaintenanceRequest> retVal = new ArrayList<>();
        for (Map.Entry<Integer, MaintenanceRequest> item : maintenanceRequests.entrySet()) {
            MaintenanceRequest req = item.getValue();
            if (!req.isScheduled()) {
                retVal.add(req);
            }
        }
        return retVal;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CORRECTIVE MAINTENANCE SCHEDULES">
    public Map<Integer, MaintenanceSchedule> getMaintenanceSchedules() {
        return maintenanceSchedules;
    }

    public MaintenanceSchedule getMaintenanceSchedule(int id) {
        return maintenanceSchedules.get(id);
    }

    public void deleteMaintenanceSchedule(int id) {
        for (Map.Entry<Integer, MaintenanceRequest> item : maintenanceRequests.entrySet()) {
            MaintenanceRequest req = item.getValue();
            if (req.getSchedule() != null && req.getSchedule().getId() == id) {
                req.setSchedule(null);
            }
        }
        maintenanceSchedules.remove(id);
    }

    public void addOrChangeMaintenanceSchedule(int id, Date dueDate, MaintenanceRequest request, List<TeamMember> teamMembers) {
        MaintenanceSchedule schedule;

        if (maintenanceSchedules.containsKey(id)) {
            schedule = getMaintenanceSchedule(id);
        } else {
            schedule = new MaintenanceSchedule(id);
        }

        schedule.setDueDate(dueDate);
        schedule.setTeamMembers(teamMembers);

        if (schedule.getMaintenanceRequest() != null && !schedule.getMaintenanceRequest().equals(request)) {
            schedule.getMaintenanceRequest().setSchedule(null);
        }

        schedule.setMaintenanceRequest(request);
        request.setSchedule(schedule);
        maintenanceSchedules.put(id, schedule);
    }

    // </editor-fold>
}
