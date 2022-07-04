import Containers.ContainerInformation.Sender;
import Containers.StandardContainer;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class SaveFileManager {
    // private BufferedReader br;
    private BufferedWriter bw;
    private List<StandardContainer> orderedContainers;
    private ArrayList<Ship> orderedShips;
    private ArrayList<Ship> orderedSentShips;
    private List<StandardContainer> orderedWarehouseContainers;
    private ArrayList<Sender> orderedSenders;
    private final Warehouse warehouse;
    private final Terminal terminal;

    // private static int dumpCount = 0;

    public SaveFileManager(ArrayList<Ship> orderedShips, ArrayList<Ship> orderedSentShips,
                           ArrayList<Sender> orderedSenders, Terminal terminal) {
        this.orderedShips = orderedShips;
        sortShips(this.orderedShips);

        this.orderedSentShips = orderedSentShips;
        sortShips(this.orderedSentShips);

        this.orderedWarehouseContainers = terminal.getWarehouse().getContainers();
        sortWarehouse(this.orderedWarehouseContainers);

        this.orderedSenders = orderedSenders;
        sortSenders(this.orderedSenders);

        this.warehouse = terminal.getWarehouse();
        this.terminal = terminal;
    }

    private void sortShips(ArrayList<Ship> list) {
        for (int i = 0; (i + 1) < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getShipName().compareTo(list.get(j).getShipName()) > 0) {
                    Collections.swap(list, i, j);
                }
            }
        }
    }

    private void sortContainers(List<StandardContainer> list) {
        for (int i = 0; (i + 1) < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getGrossWeight() > list.get(j).getGrossWeight()) {
                    Collections.swap(list, i, j);
                }
            }
        }
    }

    private void sortSenders(ArrayList<Sender> list) {
        for (int i = 0; (i + 1) < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getFullName().compareTo(list.get(j).getFullName()) > 0) {
                    Collections.swap(list, i, j);
                }
            }
        }
    }

    private void sortWarehouse(List<StandardContainer> list) {
        for (int i = 0; (i + 1) < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getLastMoved().isBefore(list.get(j).getLastMoved())) {
                    Collections.swap(list, i, j);
                } else if (list.get(i).getLastMoved().equals(list.get(j).getLastMoved())) {
                    if (list.get(i).getSender().getFullName().compareTo(list.get(j).getSender().getFullName()) > 0) {
                        Collections.swap(list, i, j);
                    }
                }
            }
        }
    }

    private void saveShip(ArrayList<Ship> shipList) {
        for (Ship s : shipList) {
            try {
                bw.write(s.getAllInfo() + '\n');
                this.orderedContainers = s.getContainers();
                if (this.orderedContainers.size() > 0) {
                    sortContainers(this.orderedContainers);
                    bw.write("-- The containers:\n");
                    for (StandardContainer c : this.orderedContainers) {
                        bw.write("\t--- " + c.getAllInfo() + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to write ship " + s.getShipName());
                return;
            }
        }
    }

    private boolean initSave() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the file name: ");
        String fileName = sc.next();
        try {
            this.bw = new BufferedWriter(
                    new FileWriter(fileName+".txt"));
        } catch (IOException e) {
            System.out.println("The new file " + fileName + ".txt could not be created");
            return false;
        }
        System.out.println("Created new save file");
        return true;
    }

    /*private void dumpContainers(List<StandardContainer> orderedContainers) throws IOException {
        sortContainers(orderedContainers);
        for (StandardContainer c : orderedContainers) {
            bw.write("new " + c.getType() + "(");
            bw.write(c.getId() + ",");
            bw.write(c.getType() + ",");
            bw.write(c.getNetWeight() + ",");
            bw.write(c.getTareWeight() + ",");
            bw.write(c.getSecurityLevel() + ",");
            bw.write(c.getCertificates() + ",");
            bw.write(c.getLastMoved() + ");\n");
        }
    }

    private void dumpShips(ArrayList<Ship> orderedSentShips, List<StandardContainer> orderedContainers) throws IOException {
        for (Ship s : orderedSentShips) {
            bw.write("----\n");
            bw.write(s.getShipName()+";");
            bw.write(s.getId() + ";");
            bw.write(s.getHomePort() + ";");
            bw.write(s.getTransportOrigin() + ";");
            bw.write(s.getDestination() + ";");
            bw.write(s.getMaxNumCon() + ";");
            bw.write(s.getMaxNumH() + ";");
            bw.write(s.getMaxNumTE() + ";");
            bw.write(s.getMaxNumEl() + ";");
            if (s.getContainers().size() > 0) {
                orderedContainers = s.getContainers();
                dumpContainers(orderedContainers);
            }
        }
    }

    private void saveDump() {
        try {
            bw = new BufferedWriter(
                    new FileWriter("dump" + dumpCount++ + ".txt"));
        } catch (IOException e) {
            System.out.println("The dump file could not be created");
        }

        try {
            bw.write("LADEDATEI\n");
            bw.write("-\n");
            bw.write(dumpCount + ";");
            for (Sender s : orderedSenders) {
                bw.write("--\n");
                bw.write("new Sender(");
                bw.write(s.getFirstName() + ",");
                bw.write(s.getLastName() + ",");
                bw.write(s.getPesel() + ",");
                bw.write(s.getAddress() + ");\n");
                if (s.getDisposedContainers().size() > 0) {
                    orderedContainers = s.getDisposedContainers();
                    dumpContainers(orderedContainers);
                }
            }

            dumpShips(orderedShips, orderedContainers);
            dumpShips(orderedSentShips, orderedContainers);

            bw.write("------\n");
            bw.write(warehouse.getMaxCon() + ";");
            dumpContainers(orderedWarehouseContainers);

            bw.write("-------\n");
            bw.write(terminal.getTrain().dump() +";\n");
            if (terminal.getTrain().getContainers().size() > 0) {
                orderedContainers = terminal.getTrain().getContainers();
                sortContainers(orderedContainers);
                dumpContainers(orderedContainers);
            }

            bw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while writing the dump file");
        }
    }*/

    public void save() {
        if (initSave()) {
            try {
                bw.write("----------- Current Date[" + terminal.getCurrDate() + "]\n");
                bw.write("\n----------\n- Stored Senders -\n----------\n");
                for (Sender s : orderedSenders) {
                    bw.write("--- " + s + "\n\n");
                    this.orderedContainers = s.getDisposedContainers();
                    if (this.orderedContainers.size() > 0) {
                        sortContainers(this.orderedContainers);
                        bw.write("-- Disposed containers:\n");
                        for (StandardContainer c : this.orderedContainers) {
                            bw.write("\t--- " + c.getAllInfo() + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Something went wrong while saving the senders...");
                return;
            }

        }
        try {
            bw.write("\n----------\n- Ships in the port -\n----------\n");
            saveShip(orderedShips);
        } catch (IOException e) {
            System.out.println("Something went wrong while saving the stored ships...");
            return;
        }

        try {
            bw.write("\n----------\n- Sent Ships -\n----------\n");
            saveShip(orderedSentShips);
        } catch (IOException e) {
            System.out.println("Something went wrong while saving the sent out ships...");
        }

        try {
            bw.write("\n----------\n- The Warehouse -\n----------\n");
            bw.write("Max capacity[" + warehouse.getMaxCon()
                    + "] - current stock[" + warehouse.getConSize() + "]\n\n");
            if (warehouse.getConSize() > 0) {
                for (StandardContainer c : orderedWarehouseContainers) {
                    bw.write("\t--- " + c.getAllInfo() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while saving the warehouse...");
            return;
        }

        try {
            bw.write("\n----------\n- The Railway Transport -\n----------\n");
            bw.write(terminal.getTrain().getAllInfo());
            orderedContainers = terminal.getTrain().getContainers();
            if (orderedContainers.size() > 0) {
                sortContainers(orderedContainers);
                for (StandardContainer c : orderedContainers) {
                    bw.write("\t--- " + c.getAllInfo() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to save the railway transport");
            return;
        }
        try {
            bw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong whilst closing the file...");
        }

        //saveDump();
    }

    /*public void load() {
        String fileName;

        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the name of the save file: ");
            fileName = sc.next();
        } catch (InputMismatchException e) {
            System.out.println("Wrong type of input...");
            return;
        }
        try {
            br = new BufferedReader(new FileReader(fileName + ".txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Failed to read the file " + fileName + ".txt");
            return;
        }

    }*/
}
