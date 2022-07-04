import Containers.ContainerInformation.*;
import Containers.*;

import java.time.LocalDate;
import java.util.*;

public class Terminal implements Runnable {
    private ArrayList<Ship> ships;
    private ArrayList<Ship> sentShips;
    private ArrayList<Sender> pastSenders;
    private final Train train;
    private final Warehouse warehouse;
    private SaveFileManager safeFile;
    private final String portName;

    private LocalDate currDate;
    private Thread thread;
    private boolean running;

    public Terminal(LocalDate date, Warehouse wh, String portName) {
        this.ships = new ArrayList<>();
        this.sentShips = new ArrayList<>();
        this.pastSenders = new ArrayList<>();
        this.train = new Train(date);
        this.warehouse = wh;
        this.currDate = date;
        this.running = true;
        this.portName = portName;
    }

    public void setUpRandomPort() {
        Sender sen1 = new Sender("Kacper", "Tomaszewski", "01250925", "Alst 59");
        Sender sen2 = new Sender("Paula", "Hartmann", "99042025", "Cottbusser Platz 1");
        Sender sen3 = new Sender("Anton", "Zacepin", "01292825", "Blumenstrasse 3");
        Sender sen4 = new Sender("Erik", "Knaub", "01311125", "Brachter Muehle 3");
        pastSenders.add(sen1);
        pastSenders.add(sen2);
        pastSenders.add(sen3);
        pastSenders.add(sen4);


        ArrayList<String> certs = new ArrayList<String>();
        certs.add("Locosquad");
        addContainerWarehouse(new StandardContainer(sen1, 50, certs, 50, SecurityLevel.LOW));
        addContainerWarehouse(new HeavyContainer(sen2, 100, certs, 75, SecurityLevel.MEDIUM));
        certs.add("Habibo");
        addContainerWarehouse(new RefrigeratedContainer(sen3, 75, certs, 90, SecurityLevel.HIGH));
        ships.add(new Ship(5, 5, 2, 10, 500, 50, "Willhelm", "Alst"));
        ships.add(new Ship(3, 6, 2, 8, 500, 100, "Smooya", "London"));
        ships.add(new Ship(9, 3, 10, 25, 850, 150, "Gesamtschule", "Brueggen"));
        ships.get(0).loadContainer(new ToxicLiquidContainer(sen4, 75, certs, 90, SecurityLevel.LOW), currDate);
        ships.get(0).loadContainer(new ToxicPowderyContainer(sen3, 100, certs, 25, SecurityLevel.LOW), currDate);
        ships.get(1).loadContainer(new RefrigeratedContainer(sen4, 75, certs, 90, SecurityLevel.HIGH), currDate);
        certs.add("Esther");
        ships.get(1).loadContainer(new RefrigeratedContainer(sen4, 75, certs, 90, SecurityLevel.HIGH), currDate);
        ships.get(1).loadContainer(new HeavyContainer(sen4, 75, certs, 90, SecurityLevel.MEDIUM), currDate);
        ships.get(2).loadContainer(new StandardContainer(sen1, 75, certs, 90, SecurityLevel.LOW), currDate);
        Ship s = new Ship(10,10 , 2, 10, 500, 50, "Tommy", "New York");
        s.setDestination("Tokyo");
        s.loadContainer(new ToxicLiquidContainer(sen2, 75, certs, 90, SecurityLevel.LOW), currDate);
        certs.add("Graf");
        s.loadContainer(new StandardContainer(sen1, 75, certs, 90, SecurityLevel.LOW), currDate);
        sentShips.add(s);
    }

    public LocalDate getCurrDate() {
        return currDate;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Train getTrain() {
        return train;
    }

    private void printShipContainers(Ship s) {
        if (s.getContainers().size() > 0) {
            System.out.println(s.getShipName() + "'s containers");
            for (int i = 0; i < s.getContainers().size(); i++) {
                List<StandardContainer> cargo = s.getContainers();
                System.out.println("\t["+ (i+1) +"]--" + cargo.get(i));
            }
        }
    }

    public void printShips() {
        System.out.println("");
        System.out.println("----------------------");
        for (int i = 0; i < ships.size(); i++) {
            System.out.println("[" + (i + 1) + "] -- " + ships.get(i));
            printShipContainers(ships.get(i));
        }
        System.out.println("----------------------");
        System.out.println("");
    }
    public void printSentShips() {
        System.out.println("");
        System.out.println("----------------------");
        for (int i = 0; i < sentShips.size(); i++) {
            System.out.println("[" + (i + 1) + "] -- " + sentShips.get(i).getShipName() + " has been sent to "
                                + sentShips.get(i).getDestination() + " from " + portName);
            printShipContainers(sentShips.get(i));
        }
        System.out.println("----------------------");
        System.out.println("");
    }

    private void printTrainContainers(Train train) {

        if (train.getContainers().size() > 0) {
            System.out.println("The containers on the ship:");
            for (int i = 0; i < train.getContainers().size(); i++) {
                List<StandardContainer> tmp = train.getContainers();
                System.out.println("\t["+ (i+1) +"]--" + tmp.get(i));
            }
        } else {
            System.out.println("There are no containers on the train");
        }
    }
    public void printTrain() {
        if (train.isFull()) {
            System.out.println("The train is already full and waits for departure");
            System.out.println("A new train comes at " + train.getComeback());
        }
        printTrainContainers(train);
    }


    private void intro() {
        System.out.println("                  ______                    _             __             \n" +
                "                 /_  __/__  _________ ___  (_)___  ____ _/ /             \n" +
                " ____________     / / / _ \\/ ___/ __ `__ \\/ / __ \\/ __ `/ /  ____________\n" +
                "/_____/_____/    / / /  __/ /  / / / / / / / / / / /_/ / /  /_____/_____/\n" +
                "                /_/  \\___/_/  /_/ /_/ /_/_/_/ /_/\\__,_/_/                \n" +
                "                                                                         ");
    }
    private void printMenu() {
        System.out.println("[1] -- Go to the port");
        System.out.println("[2] -- Check warehouse");
        System.out.println("[3] -- Check the railway transport");
        System.out.println("[4] -- Add Ship");
        System.out.println("[5] -- Add container");
        System.out.println("[6] -- Move a container");
        System.out.println("[7] -- Save current state");
        System.out.println("[8] -- Load a saved file");
        System.out.println("[9] -- Exit program (without saving)");
    }
    public void printStart() {
        intro();
        printMenu();
    }

    public void printPort() {
        System.out.println("[1] -- Check ship(s) in port");
        System.out.println("[2] -- Check sent out ship(s)");
        System.out.println("[3] -- Add a new container to a ship");
        System.out.println("[4] -- Send out a ship");
        System.out.println("[5] -- Back");
    }
    public void moveContainer() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("[1] -- Move container from Warehouse to a ship");
            System.out.println("[2] -- Move container from a ship to the warehouse");
            System.out.println("[3] -- Move container from Warehouse onto the train");
            System.out.println("[4] -- Move container from a ship onto the train");
            System.out.println("[5] -- Back");

            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        moveContainerFromWarehouseS();
                        return;
                    case 2:
                        moveContainerFromShipWH();
                        return;
                    case 3:
                        moveContainerFromWarehouseT();
                        return;
                    case 4:
                        moveContainerFromShipT();
                        return;
                    case 5:
                        return;
                    default:
                        System.out.println("That's not an option... Try Again!");
                }
            }catch (InputMismatchException e) {
                System.out.println("Wrong type of input... Try Again!");
                return;
            }
        }
    }
    public void addContainer() {
        Scanner sc = new Scanner(System.in);

        System.out.println("[1] -- Add container to the warehouse");
        System.out.println("[2] -- Add container to a ship");
        System.out.println("[3] -- Add container to the train");
        System.out.println("[4] -- Back");

        while (true) {
            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        addContainerWarehouse(createContainer());
                        return;
                    case 2:
                        addContainerShip(createContainer());
                        return;
                    case 3:
                        addContainerTrain(createContainer());
                        return;
                    case 4:
                        return;
                    default:
                        System.out.println("That's not an option... Try Again!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input... Try Again!");
                return;
            }
        }
    }
    public void checkPort() {
        while (true) {
            printPort();
            Scanner sc = new Scanner(System.in);
            try {
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        clearConsole();
                        printShips();
                        break;
                    case 2:
                        clearConsole();
                        printSentShips();
                        break;
                    case 3:
                        if (ships.size() > 0)
                            addContainerShip(createContainer());
                        else
                            System.out.println("There are no ships in the port");
                        break;
                    case 4:
                        sendShip();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("That option does not exist");
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input... Try Again!");
                return;
            }
        }
    }

    public void printWarehouse() {
        System.out.println("The warehouse has a limit of " + warehouse.getMaxCon() + " containers");

        if (warehouse.getConSize() > 0) {
            System.out.println("Currently there " + warehouse.getConSize() + " containers");
            List<StandardContainer> tmp = warehouse.getContainers();
            for (int i = 0; i < tmp.size(); i++) {
                System.out.println("\t["+ (i+1) +"]--" + tmp.get(i));
            }
        } else {
            System.out.println("There no containers in the warehouse currently");
        }
    }

    private void sendShip() {
        if (ships.size() > 0) {
            printShips();
            Scanner sc = new Scanner(System.in);

            System.out.println("Which ship do you want to take?");
            try {
                int choice = sc.nextInt();
                System.out.print("Where do you send that ship to: ");
                Scanner sc2 = new Scanner(System.in);
                String dest = sc2.nextLine();
                try {
                    if (ships.get(choice - 1).getContainers().isEmpty()) {
                        System.out.println("To send a ship it has to have at least one container");
                    } else {
                        Ship tmp = ships.get(choice - 1);
                        tmp.setDestination(dest);
                        sentShips.add(tmp);
                        ships.remove(choice - 1);
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Wrong type of input... Try Again!");
                    return;
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input... Try Again!");
                return;
            }
        }
    }

    public void save() {
        safeFile = new SaveFileManager(ships, sentShips, pastSenders, this);
        safeFile.save();
    }
    public void load() {
        System.out.println("Implementation for loading a save file is not ready");

        // TODO: implement the save system
    }


    private void printAddContainer() {
        System.out.println("[1] -- Add a new standard container");
        System.out.println("[2] -- Add a new heavy container");
        System.out.println("[3] -- Add a new refrigerated container");
        System.out.println("[4] -- Add a new liquid container");
        System.out.println("[5] -- Add a new explosive container");
        System.out.println("[6] -- Add a new toxic powdery container");
        System.out.println("[7] -- Add a new toxic liquid container");
    }
    private ArrayList<String> getCerts() {

        System.out.print("How many certificates do you want to enter?: ");
        try {
            Scanner sc = new Scanner(System.in);
            int size = sc.nextInt();

            if (size == 0) {
                return new ArrayList<String>();
            }

            ArrayList<String> tmp = new ArrayList<String>(size);
            for (int i = 0; i < size; i++) {
                System.out.print("Enter certificate (" + (i + 1) + "): ");
                Scanner ch = new Scanner(System.in);
                tmp.add(ch.nextLine());
            }

            return tmp;
        } catch(InputMismatchException e) {
            System.out.println("Wrong type of input!");
            return new ArrayList<String>();
        }
    }
    private Sender addSender() {
        System.out.println("Sender Information");
        String newPesel = "";
        Scanner sc = new Scanner(System.in);

        while (newPesel.length() < 6) {
            System.out.print("Enter the pesel: ");
            try {
                newPesel = sc.nextLine();
            } catch(InputMismatchException e) {
                System.out.println("Wrong type of input!");
                return null;
            }
        }
        int result = checkPesel(newPesel);
        if (result != -1) {
            System.out.println("There already exists such a sender...");
            System.out.println(pastSenders.get(result));
            return pastSenders.get(result);
        } else {
            try {
                System.out.print("Enter the first name of the sender: ");
                String fN = sc.nextLine();
                System.out.print("Enter the last name of the sender: ");
                String lN = sc.nextLine();
                System.out.print("Enter the address of the sender: ");
                String add = sc.nextLine();

                Sender tmp = new Sender(fN, lN, newPesel, add);
                pastSenders.add(tmp);
                return tmp;
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input!");
                return null;
            }
        }

    }
    private StandardContainer createContainer() {
        System.out.println("");
        printAddContainer();
        System.out.println("");
        System.out.print("Choose the type of the container: ");
        Scanner sc = new Scanner(System.in);
        int choice;
        try {
            choice = sc.nextInt();
            while (choice < 1 || choice > 7) {
                System.out.print("Wrong input! Try again: ");
                choice = sc.nextInt();
            }
            System.out.println("");
        } catch (InputMismatchException e) {
            System.out.println("Wrong type of input!");
            return null;
        }

        Sender sen = addSender();
        if (sen != null) {
            try {
                System.out.print("Enter the net weight of the container: ");
                double net = sc.nextDouble();
                System.out.print("Enter the tare weight of the container: ");
                double tare = sc.nextDouble();
                ArrayList<String> certs = getCerts();
                System.out.print("Enter the security level of the container(1 is the lowest and 3 the highest): ");
                int secInt = sc.nextInt();
                SecurityLevel sec = secInt == 1 ? SecurityLevel.LOW : (secInt == 3 ? SecurityLevel.HIGH : SecurityLevel.MEDIUM);

            switch (choice) {
                case 1 -> {
                    return new StandardContainer(sen, net, certs, tare, sec);
                }
                case 2 -> {
                    return new HeavyContainer(sen, net, certs, tare, sec);
                }
                case 3 -> {
                    return new RefrigeratedContainer(sen, net, certs, tare, sec);
                }
                case 4 -> {
                    return new LiquidContainer(sen, net, certs, tare, sec);
                }
                case 5 -> {
                    return new ExplosiveContainer(sen, net, certs, tare, sec);
                }
                case 6 -> {
                    return new ToxicPowderyContainer(sen, net, certs, tare, sec);
                }
                case 7 -> {
                    return new ToxicLiquidContainer(sen, net, certs, tare, sec);
                }
            }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input!");
                return null;
            }
        }
        return null;
    }


    public boolean addContainerWarehouse(StandardContainer cont) {
        if (cont != null && warehouse.addContainer(cont, currDate)) {
            System.out.println("Added the container to the warehouse\n");
            return true;
        }

        System.out.println("Failed to add the container");
        return false;
    }
    public boolean addContainerShip(StandardContainer cont) {
        if (ships.size() > 0) {
            printShips();
            Scanner sc = new Scanner(System.in);

            System.out.println("Which ship do you want to take?");
            try {
                int choice = sc.nextInt();
                if (ships.get(choice - 1).loadContainer(cont, currDate)) {
                    return true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input!");
                return false;
            }

            System.out.println("Failed to add the container to the ship");
            return false;
        }
        System.out.println("There are no ships in your port");
        return false;
    }
    public boolean addContainerTrain(StandardContainer cont) {
        if (cont != null && train.loadContainer(cont)) {
            return true;
        }
        return false;
    }

    private void moveContainerFromWarehouseS() {
        if (ships.size() > 0) {
            StandardContainer tmp = warehouse.getContainer();
            if (warehouse.hasContainer(tmp)) {
                if (addContainerShip(tmp)) {
                    if (warehouse.removeContainer(tmp)) {
                        System.out.println("Container has been moved from the warehouse onto the ship");
                    }
                } else {
                    System.out.println("The container stayed in the warehouse");
                }
            }
        } else {
            System.out.println("There are no ships in the port to move the container...");
        }
    }
    private void moveContainerFromShipWH() {
        if (ships.size() > 0) {
            printShips();
            Scanner sc = new Scanner(System.in);

            System.out.println("Which ship do you want to take?");
            try {
                int choice = sc.nextInt();
                StandardContainer tmp = ships.get(choice - 1).getContainer();
                if (ships.get(choice - 1).unloadContainer(tmp)) {
                    if (warehouse.addContainer(tmp, currDate)) {
                        System.out.println("The container has been moved from the ship to the warehouse");
                    } else {
                        System.out.println("There is no place in the warehouse for this container. " +
                                "The container has stayed on the ship");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input!");
                return;
            }
        }
    }
    private void moveContainerFromWarehouseT() {
        StandardContainer tmp = warehouse.getContainer();
        if (warehouse.hasContainer(tmp)) {
            if (addContainerTrain(tmp)) {
                if (warehouse.removeContainer(tmp)) {
                    System.out.println("Container has been moved from the warehouse onto the train");
                }
            } else {
                System.out.println("The container could not be moved and stayed in the warehouse");
            }
        }
    }
    private void moveContainerFromShipT() {
        if (ships.size() > 0) {
            printShips();
            Scanner sc = new Scanner(System.in);

            System.out.println("Which ship do you want to take?");
            try {
                int choice = sc.nextInt();
                try {
                    StandardContainer tmp = ships.get(choice - 1).getContainer();
                    if (ships.get(choice - 1).unloadContainer(tmp)) {
                        if (addContainerTrain(tmp)) {
                            System.out.println("Container has been moved onto the train");
                        } else {
                            System.out.println("Container could not be moved onto the train");
                            ships.get(choice-1).loadContainer(tmp, currDate);
                            return;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Wrong type of input");
                    return;
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input!");
                return;
            }
        }
    }

    public void createShip () {
        System.out.println("");
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the ship's name: ");
            String name = sc.nextLine();
            System.out.print("Enter the home port of this ship: ");
            String hPort = sc.nextLine();
            System.out.print("Enter the ship's weight: ");
            double sWeight = sc.nextDouble();
            System.out.print("Enter how much toxic and explosive cargo the ship can carry: ");
            int maxExTox = sc.nextInt();
            System.out.print("Enter how much heavy cargo the ship can carry: ");
            int maxHea = sc.nextInt();
            System.out.print("Enter how much refrigerated cargo the ship can carry: ");
            int maxRef = sc.nextInt();
            System.out.print("Enter how much cargo overall the ship can carry: ");
            int maxCon = sc.nextInt();
            System.out.print("Enter the maximum weight of the ship: ");
            double maxWeight = sc.nextDouble();

            Ship tmp = new Ship(maxExTox, maxHea, maxRef, maxCon, maxWeight, sWeight, name, hPort);
            ships.add(tmp);
        } catch (InputMismatchException e) {
            System.out.println("Wrong type of input!");
            return;
        }
    }

    private int checkPesel(String pesel) {
        int index = 0;
        for (Sender a : pastSenders) {
            if (a.getPesel().equalsIgnoreCase(pesel))
                return index;

            index++;
        }

        return -1;
    }

    public void clearConsole() {
        for (int i = 0; i < 50; i++)
            System.out.println();
    }

    public static Warehouse initializeWarehouse() {
        System.out.print("Enter the maximum amount of containers in this warehouse: ");
        Scanner sc = new Scanner(System.in);
        try {
            int max = sc.nextInt();

            return new Warehouse(max);
        } catch(InputMismatchException e) {
            System.out.println("Wrong type of input!");
            return initializeWarehouse();
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(5000);
                synchronized (warehouse.getContainers()) {
                    StandardContainer tmp = warehouse.checkContainers(currDate);
                    if (tmp != null) {
                        warehouse.removeContainer(tmp);
                        tmp.getSender().addDisposedContainer(tmp);
                    }
                    currDate = currDate.plusDays(1);
                    train.updateDate(currDate);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("The thread for passing the day has failed. Time does not pass anymore");
            stop();
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }
}