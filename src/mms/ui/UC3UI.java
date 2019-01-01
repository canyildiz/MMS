package mms.ui;

import java.time.Instant;
import java.util.*;
import mms.Main;
import mms.model.Equipment;
import mms.model.MaintenanceRequest;

public class UC3UI {

    public int mainMenu() {
        int ret = 0;
        while (ret >= 0) {
            ret = Main.console.waitInt("\n"
                    + " > Manage Corrective Maintenance Requests\n"
                    + "-----------------------------\n"
                    + "1. List Maintenance Requests\n"
                    + "2. Create New Maintenance Request\n"
                    + "-----------------------------\n"
                    + "(-1 for return to main menu)\n"
                    + "(0 for save and exit)\n", new int[]{-1, 0, 1, 2}, "");

            switch (ret) {
                case 1:
                    ret = listMenu();
                    break;
                case 2:
                    ret = createOrEditMenu(null);
                    break;
                case -1:
                    return 0;
                default:
                    return -1;
            }
        }

        return ret;
    }

    public int listMenu() {
        if (Main.controller.getMaintenanceRequests().isEmpty()) {
            Main.console.waitMessage("There is no equipment record!");
            return 0;
        }
        
        int ret = 0;
        while (ret >= 0) {
            List<Integer> validEntries = new ArrayList<>();

            String list = "\n"
                    + " > Manage Corrective Maintenance Requests > List Maintenance Requests\n"
                    + "Select one of the record below\n"
                    + "-----------------------------\n";

            Map<Integer, MaintenanceRequest> requests = Main.controller.getMaintenanceRequests();
            for (Map.Entry<Integer, MaintenanceRequest> request : requests.entrySet()) {
                list += String.format("%3d. %-15s\n", request.getKey(), request.getValue().getEquipment().getEquipmentName());
                validEntries.add(request.getKey());
            }
            list += "-----------------------------\n"
                    + "(0 for save and exit)\n"
                    + "(-1 for previous menu)\n"
                    + "(-2 for create new equipment)\n";

            validEntries.add(0);
            validEntries.add(-1);
            validEntries.add(-2);
            ret = Main.console.waitInt(list, validEntries.stream().mapToInt(i -> i).toArray(), "");
            switch (ret) {
                case 0:
                    return -1;
                case -1:
                    return 0;
                case -2:
                    ret = createOrEditMenu(null);
                    break;
                default:
                    ret = details(requests.get(ret));
                    break;
            }
        }
        return ret;
    }

    public int details(MaintenanceRequest request) {
        int ret = 0;
        while (ret >= 0) {
            String details = "\n"
                    + " > Manage Corrective Maintenance Requests > Maintenance Request Details\n"
                    + "-----------------------------\n"
                    + "Id: " + request.getMaintenanceID() + "\n"
                    + "Description: " + request.getDescription() + "\n"
                    + "Equipment Name: " + request.getEquipment().getEquipmentName() + "\n"
                    + "Error Code: " + request.getErrorCode() + "\n"
                    + "Priority (1-3): " + request.getPriority() + "\n"
                    + "-----------------------------\n"
                    + "(0 for save and exit)\n"
                    + "(1 for previous menu)\n"
                    + "(2 for create new equipment)\n"
                    + "(3 for edit this equipment)\n"
                    + "(4 for delete this equipment)\n";

            ret = Main.console.waitInt(details, new int[]{0, 1, 2, 3, 4}, "");
            switch (ret) {
                case 0:
                    return -1;
                case 1:
                    return 0;
                case 2:
                    return createOrEditMenu(null);
                case 3:
                    ret = createOrEditMenu(request);
                    break;
                case 4:
                    return deleteMenu(request);
            }
        }
        return ret;
    }

    public int deleteMenu(MaintenanceRequest request) {
        String details = "\n"
                + " > Manage MaintenanceRequests > Delete MaintenanceRequest\n"
                + "-----------------------------\n"
                + "Are you sure to delete this equipment?\n"
                + "Id: " + request.getMaintenanceID() + "\n"
                + "Description: " + request.getDescription() + "\n"
                + "-----------------------------\n"
                + "(1 for delete the equipment)\n"
                + "(-1 for previous menu)\n"
                + "(0 for save and exit)\n";

        int ret = Main.console.waitInt(details, new int[]{0, -1, 1}, "");
        switch (ret) {
            case 0:
                return -1;
            case -1:
                return 0;
            case 1:
                Main.controller.deleteMaintenanceRequest(request.getMaintenanceID());
                Main.console.waitMessage("Record Deleted!");
                return 0;
        }
        return ret;
    }

    public int createOrEditMenu(MaintenanceRequest request) {
        if (Main.controller.getEquipments().isEmpty()) {
            Main.console.waitMessage("ERROR: There is no equipment records available!"
                    + "\nPlease create the equipment records first.");
            return 0;
        }

        int Id;
        String description;
        int errorCode;
        int priority;
        int[] equipments;
        int equipmentId;

        if (request == null) {
            //create new equipment

            Main.console.print("\n"
                    + " > Manage Corrective Maintenance Requests > Create Maintenance Request\n"
                    + "-----------------------------\n"
                    + "Enter the required values regarding its data type\n");

            Id = Main.console.waitInt("\nId (integer)(0 for cancel):", "Invalid id number, (0 for cancel)");
            if (Id == 0) {
                return 0;
            }

            description = Main.console.waitString("\nDescription (String):", "");
            errorCode = Main.console.waitInt("\nError Code:", "");
            priority = Main.console.waitInt("\nPriority (1: high, 2:normal, 3:low):", new int[]{1, 2, 3}, "");
            equipments = listAvailableEquipments();
            equipmentId = Main.console.waitInt("\nEquipment (enter id ):", equipments, "");
        } else {
            //edit equipment
            Main.console.print("\n"
                    + " > Manage Maintenance Requests > Edit Maintenance Request\n"
                    + "-----------------------------\n"
                    + "Enter the required values regarding its data type\n"
                    + "(type * for keep the current values)\n");

            Id = request.getMaintenanceID();
            description = Main.console.waitStringOrEmpty("\nDescription (String):", "*", request.getDescription());
            errorCode = Main.console.waitIntOrEmpty("\nError Code:", "*", request.getErrorCode());
            priority = Main.console.waitIntOrEmpty("\nPriority (1: high, 2:normal, 3:low):", new int[]{1, 2, 3}, "*", request.getErrorCode(), "");
            equipments = listAvailableEquipments();
            equipmentId = Main.console.waitIntOrEmpty("\nEquipment (enter id or * for keep the current record):", equipments, "*", request.getEquipment().getEquipmentID(), "");
        }

        Main.controller.addOrChangeMaintenanceRequest(Id, description, errorCode, priority, equipmentId);
        String resultMsg;
        if (request == null) {
            resultMsg = "Record Created";
        } else {
            resultMsg = "Record changed";
        }
        Main.console.waitMessage(resultMsg);
        return 0;
    }

    private int[] listAvailableEquipments() {
        List<Integer> validEntries = new ArrayList<>();
        String listString = "\n"
                + "Select one of the equipment record below\n"
                + "---- Available Equipments ---\n";

        Map<Integer, Equipment> equipments = Main.controller.getEquipments();
        for (Map.Entry<Integer, Equipment> equipment : equipments.entrySet()) {
            listString += String.format("%3d. %-15s\n", equipment.getKey(), equipment.getValue().getEquipmentName());
            validEntries.add(equipment.getKey());
        }
        listString += "-----------------------------\n";
        Main.console.println(listString);
        return validEntries.stream().mapToInt(i -> i).toArray();
    }
}
