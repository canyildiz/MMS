package mms.ui;

import java.time.Instant;
import java.util.*;
import mms.Main;
import mms.model.Equipment;

public class UC1UI {

    public int mainMenu() {
        int ret = 0;
        while (ret >= 0) {
            ret = Main.console.waitInt("\n"
                    + " > Manage Equipments\n"
                    + "-----------------------------\n"
                    + "1. List Equipments\n"
                    + "2. Create New Equipment\n"
                    + "-----------------------------\n"
                    + "(-1 for return to main menu)\n"
                    + "(0 for save and exit)\n", new int[]{0, 1, 2, 3}, "");

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
        int ret = 0;
        while (ret >= 0) {
            List<Integer> validEntries = new ArrayList<Integer>();

            String list = "\n"
                    + " > Manage Equipments > List Equipments\n"
                    + "-----------------------------\n";

            Map<Integer, Equipment> equipments = Main.controller.getEquipments();
            for (Map.Entry<Integer, Equipment> equipment : equipments.entrySet()) {
                list += String.format("%3d. %-15s\n", equipment.getKey(), equipment.getValue().getEquipmentName());
                validEntries.add(equipment.getKey());
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
                    ret = details(equipments.get(ret));
                    break;
            }
        }
        return ret;
    }

    public int details(Equipment equipment) {
        int ret = 0;
        while (ret >= 0) {
            String details = "\n"
                    + " > Manage Equipments > Equipment Details\n"
                    + "-----------------------------\n"
                    + "Id: " + equipment.getEquipmentID() + "\n"
                    + "Name: " + equipment.getEquipmentName() + "\n"
                    + "Last Maintenance Date: " + equipment.getLastMaintenanceDate().toString() + "\n"
                    + "Last Error Code: " + equipment.getLastErrorCode() + "\n"
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
                    ret = createOrEditMenu(equipment);
                    break;
                case 4:
                    return deleteMenu(equipment);
            }
        }
        return ret;
    }

    public int deleteMenu(Equipment equipment) {
        String details = "\n"
                + " > Manage Equipments > Delete Equipment\n"
                + "-----------------------------\n"
                + "Are you sure to delete this equipment?\n"
                + "Id: " + equipment.getEquipmentID() + "\n"
                + "Name: " + equipment.getEquipmentName() + "\n"
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
                Main.controller.deleteEquipment(equipment.getEquipmentID());
                Main.console.println("Deleted!");
                return 0;
        }
        return ret;
    }

    public int createOrEditMenu(Equipment equipment) {
        int Id;
        String name;
        Date lastMaintenanceDate;
        int lastErrorCode;

        if (equipment == null) {
            //create new equipment

            Main.console.print("\n"
                    + " > Manage Equipments > Create Equipment\n"
                    + "-----------------------------\n"
                    + "Enter the required values regarding its data type\n");

            Id = Main.console.waitInt("\nId (integer)(0 for cancel):", "Invalid id number, (0 for cancel)");
            if (Id == 0) {
                return 0;
            }

            name = Main.console.waitString("\nName (String):", "");
            lastMaintenanceDate = Main.console.waitDate("\nLast Maintenance Date (Date in yyyy-mm-dd or * for today):\n", "yyyy-MM-dd", "", "*", Date.from(Instant.now()));
            lastErrorCode = Main.console.waitInt("\nLast Error Code:", "");
        } else {
            //edit equipment
            Main.console.print("\n"
                    + " > Manage Equipments > Edit Equipment\n"
                    + "-----------------------------\n"
                    + "Enter the required values regarding its data type\n"
                    + "(type * for keep the current values)\n");

            Id = equipment.getEquipmentID();
            name = Main.console.waitStringOrEmpty("\nName (String):", "*", equipment.getEquipmentName());
            lastMaintenanceDate = Main.console.waitDate("\nLast Maintenance Date (Date in yyyy-mm-dd):\n", "yyyy-MM-dd", "", "*", equipment.getLastMaintenanceDate());
            lastErrorCode = Main.console.waitInt("\nLast Error Code:", "");
        }

        Main.controller.addOrChangeEquipment(Id, name, lastMaintenanceDate, lastErrorCode);
        if (equipment == null) {
            Main.console.println("### Created ###");
        } else {
            Main.console.println("### Edit Completed ###");
        }
        return 0;
    }
}
