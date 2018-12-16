package mms.model;

import java.util.List;

public class TeamMember implements java.io.Serializable {

    private final int id;
    private String name;
    private List<TimeSlot> timeSlots;

    public TeamMember(int id, String name, List<TimeSlot> timeSlots) {
        this.id = id;
        this.name = name;
        this.timeSlots = timeSlots;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void addTimeSlot(TimeSlot timeslot) {
        this.timeSlots.add(timeslot);
    }
}
