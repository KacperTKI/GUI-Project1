import Containers.StandardContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Train implements Runnable {
    private boolean isFull;
    private LocalDate currDate;
    private LocalDate comeback;

    private List<StandardContainer> containers;

    private Thread thread;
    private boolean running;

    public Train(LocalDate currDate) {
        this.isFull = false;
        this.running = true;
        this.containers = Collections.synchronizedList(new ArrayList<StandardContainer>());
        this.currDate = currDate;
        this.comeback = currDate;
    }

    public boolean isFull() {
        return isFull;
    }

    public LocalDate getComeback() {
        return comeback;
    }

    public List<StandardContainer> getContainers() {
        return containers;
    }

    public String getAllInfo() {
        return "Train Status["+isFull+"] - comeback["+comeback+"]";
    }

    public boolean loadContainer(StandardContainer cargo) {
        if (!isFull) {
            if (cargo != null) {
                cargo.setLastMoved(currDate);
                containers.add(cargo);
                System.out.println("The container ID[" + cargo.getId() + "] has been put on the train");
                return true;
            } else {
                System.out.println("This container can not be loaded onto the train");
                return false;
            }
        } else {
            System.out.println("There is no room on this train. Please wait for a new one");
            System.out.println("A new train comes on the " + comeback);
            return false;
        }
    }

    public void updateDate(LocalDate currDate) {
        this.currDate = currDate;
    }

    private void checkSize() {
        if (containers.size() == 10)
            isFull = true;
    }

    @Override
    public void run() {
        while (running) {
            checkSize();
            if (isFull) {
                try {
                    comeback = currDate.plusDays(6);
                    System.out.println("The railway transport is full and waits for departure...");
                    Thread.sleep(30000);
                    System.out.println("A new train has arrived");
                    containers = Collections.synchronizedList(new ArrayList<StandardContainer>());
                    isFull = false;
                } catch (InterruptedException e) {
                    System.out.println("Something went wrong with the trains. The railway transport is currently offline");
                    stop();
                }
            }
        }
    }

    public void start() {
        this.thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }
}
