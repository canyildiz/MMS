package mms.ui;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mms.Main;
import mms.model.MaintenanceSchedule;
import mms.model.MaintenanceRequest;
import mms.model.TeamMember;

public class UC4UI {

    private List<TeamMember> teamMembersAdded;
    private final String dateFormat = "yyyy-MM-dd'T'HH:mm";

    public int mainMenu() {
        int retVal = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(" > Manage Corrective Maintenance Schedule\n");
        sb.append("-----------------------------\n");
        sb.append("1. List Maintenance Schedules\n");
        sb.append("2. Add New Maintenance Schedule\n");
        sb.append("3. Edit Maintenance Schedule\n");
        sb.append("4. Delete Maintenance Schedule\n");
        sb.append("-----------------------------\n");
        sb.append("(0 for return to main menu)\n");

        while (retVal >= 0) {
            retVal = Main.console.waitInt(sb.toString(), new int[]{-1, 0, 1, 2, 3, 4}, "");

            switch (retVal) {
                case 1:
                    retVal = listMenu();
                    break;
                case 2:
                    retVal = addForm();
                    break;
                case 3:
                    int id;
                    id = Main.console.waitInt("Give the ID of Maintenance Schedule to edit: \n", "Invalid ID given!");
                    retVal = editForm(Main.controller.getMaintenanceSchedule(id));
                    break;
                case 4:
                    int idToDelete;
                    idToDelete = Main.console.waitInt("Give the ID of Maintenance Schedule to delete: \n", "Invalid ID given!");
                    Main.controller.deleteTeamMember(idToDelete);
                    Main.console.println("\n### Maintenance Schedule deleted! ###\n");
                    retVal = 0;
                    break;
                case 0:
                default:
                    return 0;
            }
        }

        return retVal;
    }

    private int listMenu() {
        int retVal = 0;
        while (retVal >= 0) {
            List<Integer> validEntries = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(" > Manage Maintenance Schedules > List Maintenance Schedules\n");
            sb.append("-----------------------------\n");

            Map<Integer, MaintenanceSchedule> schedules = Main.controller.getMaintenanceSchedules();
            if (schedules.isEmpty()) {
                Main.console.waitMessage("There is no Maintenance Schedules!");
                return 0;
            } else {
                for (Map.Entry<Integer, MaintenanceSchedule> item : schedules.entrySet()) {
                    sb.append(String.format("%3d. %-15s %s\n", item.getKey(), item.getValue().getId(), item.getValue().getDueDate()));
                    validEntries.add(item.getKey());
                }
            }
            sb.append("-----------------------------\n");
            sb.append("(0 for previous menu)\n");

            validEntries.add(0);
            retVal = Main.console.waitInt(sb.toString(), validEntries.stream().mapToInt(i -> i).toArray(), "");
            switch (retVal) {
                case 0:
                    return 0;
                default:
                    retVal = details(schedules.get(retVal));
                    break;
            }
        }
        return retVal;
    }

    private void teamMembersList(StringBuilder sb, List<TeamMember> teamMembersList, boolean notAddedOnly) {
        for (TeamMember tm : teamMembersList) {
            if (notAddedOnly && teamMembersAdded.contains(tm)) {
                continue;
            }
            sb.append(String.format(" :: %3d. %s", tm.getId(), tm.getName())).append("\n");
        }
    }

    private int details(MaintenanceSchedule schedule) {
        int retVal = 1;
        while (retVal > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(" > Manage Maintenance Schedules > Maintenance Schedule Details\n");
            sb.append("-----------------------------\n");
            sb.append("Id: ").append(schedule.getId()).append("\n");
            sb.append("Request Description: ").append(schedule.getMaintenanceRequest().getDescription()).append("\n");
            sb.append("Request Priority: ").append(schedule.getMaintenanceRequest().getPriority()).append("\n");
            sb.append("Assigned Team Members: \n");
            teamMembersList(sb, schedule.getTeamMembers(), false);
            sb.append("\n");
            sb.append("-----------------------------\n");
            sb.append("(0 for Maintenance Schedule list)\n");
            sb.append("(1 to edit Maintenance Schedule)\n");
            sb.append("(2 to delete Maintenance Schedule)\n");

            retVal = Main.console.waitInt(sb.toString(), new int[]{0, 1, 2}, "");
            switch (retVal) {
                case 0:
                    return 0;
                case 1:
                    editForm(schedule);
                    retVal = 0;
                    break;
                case 2:
                    Main.controller.deleteMaintenanceSchedule(schedule.getId());
                    Main.console.println("\n### Maintenance Schedule deleted! ###\n");
                    retVal = 0;
                    break;
            }
        }
        return retVal;
    }

    private MaintenanceRequest selectRequestForm() {
        return selectRequestForm(null);
    }
    
    private MaintenanceRequest selectRequestForm(MaintenanceRequest selectedRequest) {
        List<MaintenanceRequest> requests = Main.controller.getWaitingRequests();

        MaintenanceRequest retVal = null;
        
        StringBuilder sb = new StringBuilder();
        sb.append("\nSelect Maintenance Request to Schedule:\n");
        sb.append("------------------------------------------\n");
        sb.append("Maintenance Request List:\n");
        sb.append("__________________________________________\n");
        if(selectedRequest != null) {
            sb.append("  0. [CURRENT] ").append(selectedRequest.getDescription()).append("\n");
        }
        List<Integer> validEntries = new ArrayList<>();
        requests.forEach((mr) -> {
            sb.append(String.format("%3d. %s", mr.getMaintenanceID(), mr.getDescription())).append("\n");
            validEntries.add(mr.getMaintenanceID());
        });
        if(selectedRequest != null) {
            validEntries.add(0);
        }
        sb.append("\n\n");

        int requestId;
        boolean requestChosen = false;
        while (!requestChosen) {
            requestId = Main.console.waitInt(sb.toString(), validEntries.stream().mapToInt(i -> i).toArray(), "Invalid Request ID given!");
            if(requestId == 0 && selectedRequest != null)
                return selectedRequest;
            MaintenanceRequest r = Main.controller.getMaintenanceRequest(requestId);
            if (null != r) {
                retVal = r;
                requestChosen = true;
            } else {
                Main.console.println("\n ERROR: Given request not found in DB!\n");
            }
        }
        return retVal;
    }
    
    private int addForm() {
        Main.console.println("\n > Manage Maintenance Schedules > Add Maintenance Schedule\n");

        int id;
        id = Main.console.waitInt("Maintenance Schedule's ID?\n", "Invalid ID given!");

        MaintenanceRequest request = selectRequestForm();
        
        Date dueDate;
        dueDate = Main.console.waitDate("Maintenance Schedule's Due Date (Give * for now) ?\n", dateFormat, "Invalid due date!", "*", Date.from(Instant.now()));

        teamMembersAdded = new ArrayList<>();
        showTeamMembersForm(dueDate);
        List<TeamMember> teamMembers = getTeamMembers();

        Main.controller.addOrChangeMaintenanceSchedule(id, dueDate, request, teamMembers);

        Main.console.println("\n### Maintenance Schedule added with ID: " + id + " ###\n");

        return 0;
    }

    private int editForm(MaintenanceSchedule schedule) {
        Main.console.println("\n > Manage Maintenance Schedules > Edit Maintenance Schedule (ID: " + schedule.getId() + ") \n");

        Date dueDate = Main.console.waitDate("Please give new due date [* to leave unchanged]", dateFormat, "Invalid Due Date given!", "*", schedule.getDueDate());
        
        MaintenanceRequest request = selectRequestForm(schedule.getMaintenanceRequest());
        
        
        teamMembersAdded = schedule.getTeamMembers();
        showTeamMembersForm(dueDate);
        
        Main.controller.addOrChangeMaintenanceSchedule(schedule.getId(), dueDate, request , getTeamMembers());

        Main.console.println("\n### Maintenance Schedule updated with ID: " + schedule.getId() + " ###\n");

        return 0;
    }

    private void showTeamMembersForm(Date dueDate) {
        
        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n > Manage Maintenance Schedules > Add Maintenance Schedule > Add Team Members\n");
            sb.append("Current Members Selected:\n");
            teamMembersList(sb, teamMembersAdded, false);
            sb.append("\n-----------------------------\n");
            sb.append("(0 to finish adding Team Members)\n");
            sb.append("(1 to add new Team Member)\n");
            int sel;
            sel = Main.console.waitInt(sb.toString(), new int[]{0, 1}, "Invalid option selected!");
            switch (sel) {
                case 0:
                    return;
                case 1:
                    TeamMember tm = showTeamMemberSelectionForm(dueDate);
                    if (tm != null) {
                        teamMembersAdded.add(tm);
                    }
                    break;
            }
        }

    }

    private TeamMember showTeamMemberSelectionForm(Date dueDate) {
        StringBuilder sb = new StringBuilder();
        List<TeamMember> availableTeamMembers = Main.controller.getAvailableTeamMembers(dueDate);
        teamMembersList(sb, availableTeamMembers, true);
        sb.append("Please choose a Team Member to assign \n");
        int teamMemberId;
        boolean teamMemberSelected = false;
        TeamMember teamMember = null;
        int[] validEntries = new int[availableTeamMembers.size() + 1];
        validEntries[0] = 0;
        int i = 1;
        for (TeamMember t : availableTeamMembers) {
            validEntries[i++] = t.getId();
        }
        while (!teamMemberSelected) {
            teamMemberId = Main.console.waitInt(sb.toString(), validEntries, "Invalid Team Member id!");
            if (teamMemberId == 0) {
                return null;
            }
            teamMember = Main.controller.getTeamMember(teamMemberId);
            if (null != teamMember) {
                teamMemberSelected = true;
            } else {
                Main.console.println("ERROR: Team Member not found with given ID: " + teamMemberId + "\n");
            }
        }
        return teamMember;
    }

    private List<TeamMember> getTeamMembers() {
        return teamMembersAdded;
    }

}
