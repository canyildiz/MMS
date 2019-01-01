package mms.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mms.Main;
import mms.model.TeamMember;

public class UC2UI {

    private List<Date[]> timeSlotsAdded;
    private final String dateFormat = "yyyy-MM-dd'T'HH:mm";

    public int mainMenu() {
        int retVal = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(" > Manage Team Members\n");
        sb.append("-----------------------------\n");
        sb.append("1. List Team Members\n");
        sb.append("2. Add New Team Member\n");
        sb.append("3. Edit Team Member\n");
        sb.append("4. Delete Team Member\n");
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
                    id = Main.console.waitInt("Give the ID of Team Member to edit: \n", "Invalid ID given!");
                    retVal = editForm(Main.controller.getTeamMember(id));
                    break;
                case 4:
                    int idToDelete;
                    idToDelete = Main.console.waitInt("Give the ID of Team Member to delete: \n", "Invalid ID given!");
                    Main.controller.deleteTeamMember(idToDelete);
                    Main.console.println("\n### Team Member deleted! ###\n");
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
            sb.append(" > Manage Team Members > List Team Members\n");
            sb.append("-----------------------------\n");

            Map<Integer, TeamMember> teamMembers = Main.controller.getTeamMembers();
            for (Map.Entry<Integer, TeamMember> item : teamMembers.entrySet()) {
                sb.append(String.format("%3d. %-15s\n", item.getKey(), item.getValue().getName()));
                validEntries.add(item.getKey());
            }
            sb.append("-----------------------------\n");
            sb.append("(0 for previous menu)\n");

            validEntries.add(0);
            retVal = Main.console.waitInt(sb.toString(), validEntries.stream().mapToInt(i -> i).toArray(), "");
            switch (retVal) {
                case 0:
                    return 0;
                default:
                    retVal = details(teamMembers.get(retVal));
                    break;
            }
        }
        return retVal;
    }

    private void timeSlotDetails(StringBuilder sb, List<Date[]> datesList) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        for (Date[] dates : datesList) {
            sb.append(" :: ");
            sb.append(df.format(dates[0]));
            sb.append(" --> ");
            sb.append(df.format(dates[1]));
            sb.append("\n");
        }
    }

    private int details(TeamMember teamMember) {
        int retVal = 1;
        while (retVal > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");
            sb.append(" > Manage Team Members > Team Member Details\n");
            sb.append("-----------------------------\n");
            sb.append("Id: ").append(teamMember.getId()).append("\n");
            sb.append("Name: ").append(teamMember.getName()).append("\n");
            sb.append("Time Slots: \n");
            timeSlotDetails(sb, teamMember.getTimeSlotsAsDate());
            sb.append("\n");
            sb.append("-----------------------------\n");
            sb.append("(0 for team members list)\n");
            sb.append("(1 to edit member)\n");
            sb.append("(2 to delete member)\n");

            retVal = Main.console.waitInt(sb.toString(), new int[]{0, 1, 2}, "");
            switch (retVal) {
                case 0:
                    return 0;
                case 1:
                    editForm(teamMember);
                    retVal = 0;
                    break;
                case 2:
                    Main.controller.deleteTeamMember(teamMember.getId());
                    Main.console.println("\n### Team Member deleted! ###\n");
                    retVal = 0;
                    break;
            }
        }
        return retVal;
    }

    private int addForm() {
        Main.console.println("\n > Manage Team Members > Add Team Member\n");

        int id;
        id = Main.console.waitInt("Team Member's ID?\n", "Invalid ID given!");

        String name;
        name = Main.console.waitString("Team Member's name?\n", "Invalid name given!");

        showTimeSlotsForm();
        List<Date[]> timeSlots = getTimeSlots();

        Main.controller.addOrChangeTeamMember(id, name, timeSlots);

        Main.console.println("\n### Team Member added with ID: " + id + " ###\n");

        return 0;
    }

    private int editForm(TeamMember teamMember) {
        Main.console.println("\n > Manage Team Members > Edit Team Member (ID: " + teamMember.getId() + ") \n");

        String name;
        name = Main.console.waitStringOrEmpty("Team Member's name? [" + teamMember.getName() + "]\n", "", teamMember.getName());

        List<Date[]> timeSlots;
        String editTimeSlotsOption;
        editTimeSlotsOption = Main.console.waitString("Do you want to edit time slots? [y/n]", "Please choose one!");
        if (null == editTimeSlotsOption || editTimeSlotsOption.toLowerCase().equals("y")) {
            showTimeSlotsForm();
            timeSlots = getTimeSlots();
        } else {
            timeSlots = teamMember.getTimeSlotsAsDate();
        }

        Main.controller.addOrChangeTeamMember(teamMember.getId(), name, timeSlots);

        Main.console.println("\n### Team Member updated with ID: " + teamMember.getId() + " ###\n");

        return 0;
    }

    private void showTimeSlotsForm() {
        timeSlotsAdded = new ArrayList<>();
        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n > Manage Team Members > Add Team Member > Add time slots\n");
            timeSlotDetails(sb, timeSlotsAdded);
            sb.append("-----------------------------\n");
            sb.append("(0 to finish adding time slots)\n");
            sb.append("(1 to add new time slot)\n");
            int sel;
            sel = Main.console.waitInt(sb.toString(), new int[]{0, 1}, "Invalid option selected!");
            switch (sel) {
                case 0:
                    return;
                case 1:
                    Date[] ts = showSingleTimeSlotForm();
                    timeSlotsAdded.add(ts);
                    break;
            }
        }

    }

    private Date[] showSingleTimeSlotForm() {
        StringBuilder sb = new StringBuilder();
        sb.append("Please give a start and end date for each time \nslot in following format : '").append(dateFormat).append("' \n");
        sb.append("Start Date: \n");
        Date beginDate, endDate;
        beginDate = Main.console.waitDate(sb.toString(), dateFormat, "Invalid begin date!", "", null);
        endDate = Main.console.waitDate("End Date: \n", dateFormat, "Invalid end date!", "", null);
        return new Date[]{beginDate, endDate};
    }

    private List<Date[]> getTimeSlots() {
        return timeSlotsAdded;
    }

}
