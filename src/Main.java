import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Terminal terminal;
        int term = 0;
        while (true) {
            System.out.println("Do you want to create a port from scratch or use the example port? (1 or 2)");
            while (true) {
                try {
                    Scanner sc = new Scanner(System.in);
                    term = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong input type...");
                }
                if (term == 1 || term == 2)
                    break;

                System.out.println("Try Again!");
            }


            if (term == 1) {
                String portName = "";
                try {
                    Scanner port = new Scanner(System.in);
                    System.out.print("Enter the name of the port: ");
                    portName = port.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong input type...");
                }
                terminal = new Terminal(LocalDate.now(), Terminal.initializeWarehouse(), portName);

                terminal.start();
                terminal.getTrain().start();
                terminal.clearConsole();
                break;
            } else {
                terminal = new Terminal(LocalDate.now(), new Warehouse(15), "Example Port");
                terminal.setUpRandomPort();
                terminal.start();
                terminal.getTrain().start();
                terminal.clearConsole();
                break;
            }
        }

        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                terminal.printStart();

                int choice = sc.nextInt();
                while (choice < 1 || choice > 9) {
                    System.out.print("Oops, Wrong Input! Try Again: ");
                    choice = sc.nextInt();
                }

                switch (choice) {
                    case 1:
                        terminal.checkPort();
                        break;
                    case 2:
                        terminal.printWarehouse();
                        break;
                    case 3:
                        terminal.printTrain();
                        break;
                    case 4:
                        terminal.createShip();
                        break;
                    case 5:
                        terminal.addContainer();
                        break;
                    case 6:
                        terminal.moveContainer();
                        break;
                    case 7:
                        terminal.save();
                        break;
                    case 8:
                        terminal.load();
                        break;
                    case 9:
                        System.out.println("Exiting the program...");
                        terminal.stop();
                        terminal.getTrain().stop();
                        return;
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong type of input!");
            }
        }
    }
}
