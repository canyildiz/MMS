package mms;

import mms.controller.MMS;
import java.io.*;
import mms.ui.*;

public class Main {

    public static final String FILENAME = "mmsv2.dat";
    public static MMS controller;
    public static mms.ui.Console console = new mms.ui.Console();

    public static void main(String[] args) throws Exception {
        loadRoot();
        mainMenu();
        saveRoot();
    }

    private static void mainMenu() {
        int ret = 0;
        while (ret >= 0) {
            ret = console.waitInt("\n"
                    + "Maintenance Management System\n"
                    + "-----------------------------\n"
                    + "1. Manage Equipments\n"
                    + "2. Manage Team Members\n"
                    + "3. Manage Corrective Maintenance Requests\n\n"
                    + "(0 for save and exit)\n", new int[]{0, 1, 2, 3}, "");

            switch (ret) {
                case 1:
                    UC1UI uc1 = new UC1UI();
                    ret = uc1.mainMenu();
                    break;
                case 2:
                    UC2UI uc2 = new UC2UI();
                    ret = uc2.mainMenu();
                    break;
                case 3:
                    UC3UI uc3 = new UC3UI();
                    ret = uc3.mainMenu();
                    break;
                default:
                    return;
            }
        }
    }

    public static void loadRoot() throws Exception {
        File file = new File(FILENAME);
        if (!file.exists()) {
            controller = new MMS();
        } else {
            try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILENAME))) {
                controller = (MMS) stream.readObject();
            }
        }
    }

    public static void saveRoot() throws Exception {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            stream.writeObject(controller);
        }
    }

}
