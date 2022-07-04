import Containers.*;
import Exceptions.CargoNotFoundException;
import Exceptions.NotEnoughRoomException;
import Exceptions.OverWeightLimitException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Ship {
    // the id of the ship
    final private int id;
    // maximum number of toxic or explosive containers
    final private int maxNumTE;
    private int numTE;
    // maximum number of heavy containers
    final private int maxNumH;
    private int numH;
    // maximum number of electric containers
    final private int maxNumEl;
    private int numEl;
    // maximum number of all containers
    final private int maxNumCon;
    private int numCon;
    // maximum weight load of the ship;
    final private double maxWeightLoad;
    // the ships weight
    final private double shipWeight;
    // ships weight together with cargo
    private double deadWeight;

    // Basic information about the ship
    final private String shipName;
    final private String homePort;
    private String transportOrigin;
    private String destination;

    // The loaded containers
    private List<StandardContainer> containers;

    // counter for creating the id
    static int counter = 0;

    public Ship(int maxToxExp, int maxHeavy,
         int maxElec, int maxCont,
         double maxLoad, double sWeight,
         String name, String home) {
        id = counter++;

        maxNumTE = maxToxExp;
        numTE = 0;

        maxNumH = maxHeavy;
        numH = 0;

        maxNumEl = maxElec;
        numEl = 0;

        maxNumCon = maxCont;
        numCon = 0;

        maxWeightLoad = maxLoad;
        shipWeight = sWeight;
        deadWeight = sWeight;

        shipName = name;
        homePort = home;
        transportOrigin = home;

        containers = Collections.synchronizedList(new ArrayList<StandardContainer>());
    }

    private void printContainers() {
        if (containers.size() > 0) {
            for (int i = 0; i < containers.size(); i++) {
                System.out.println("["+(i+1)+"] -- " + containers.get(i));
            }
        }
    }

    public String getDestination() {
        return destination;
    }

    public String getTransportOrigin() {
        return transportOrigin;
    }

    @Override
    public String toString() {
        return "Ship: " + shipName + " from " + homePort + " - ID:" + id;
    }

    public String getShipName() { return shipName; }

    public String getAllInfo() {
        return "[" + this.shipName + "] - ID[" + this.id + "] - Home port[" + this.homePort
                + "] - transport origin[" + this.transportOrigin + "] - destination["
                + this.destination + "] -||-  Max Standard Containers[" + this.maxNumCon
                + "] - current amount[" + this.numCon + "] - Max Heavy Containers["
                + this.maxNumH + "] - current amount[" + this.numH
                + "] - Max Toxic and Explosive Containers[" + this.maxNumTE + "] - current amount["
                + this.numTE + "] - Max Refrigerated Containers[" + this.maxNumEl
                + "] - current amount[" + this.numEl + "]";
    }

    public List<StandardContainer> getContainers() { return containers; }

    public void setTransportOrigin(String transportOrigin) {
        this.transportOrigin = transportOrigin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public StandardContainer getContainer() {
        if (containers.size() > 0) {
            printContainers();
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print("Pick the container: ");
                int choice = sc.nextInt();
                if (containers.get(choice-1) != null) {
                    return containers.get(choice-1);
                } else {
                    System.out.println("That's not an option... Try Again!");
                }
            }
        }
        System.out.println("There are no containers!");
        return null;
    }

    public void printCargo() {
        System.out.print("[ \\-");
        for (StandardContainer c : containers) {
            System.out.print(" " + c + " -");
        }
        System.out.println("/ ]");
    }

    public void printTransport() {
        System.out.println(shipName+ "'s dead weight: " + deadWeight);
        System.out.println(shipName + " is on the way to: " + destination + " from " + transportOrigin);
        printCargo();
    }

    public boolean loadContainer(StandardContainer cargo, LocalDate date) {
        boolean result = false;

        try {
            if (deadWeight + cargo.getGrossWeight() > maxWeightLoad) {
                throw new OverWeightLimitException();
            } else {
                try {
                    if (cargo instanceof HeavyContainer || cargo instanceof LiquidCargo) {
                        if (numH < maxNumH && numCon < maxNumCon) {
                            containers.add(cargo);
                            numCon++;
                            numH++;
                            deadWeight += cargo.getGrossWeight();
                            cargo.setLastMoved(date);
                            result = true;
                        } else {
                            throw new NotEnoughRoomException();
                        }
                    } else if (cargo instanceof RefrigeratedContainer) {
                        if (numEl < maxNumEl && numCon < maxNumCon) {
                            containers.add(cargo);
                            numCon++;
                            numEl++;
                            deadWeight += cargo.getGrossWeight();
                            cargo.setLastMoved(date);
                            result = true;
                        } else {
                            throw new NotEnoughRoomException();
                        }
                    } else if (cargo instanceof ToxicContainer || cargo instanceof ExplosiveContainer ||
                                cargo instanceof ToxicPowderyContainer) {
                        if (numTE < maxNumTE && numCon < maxNumCon) {
                            containers.add(cargo);
                            numCon++;
                            numTE++;
                            deadWeight += cargo.getGrossWeight();
                            cargo.setLastMoved(date);
                            result = true;
                        } else {
                            throw new NotEnoughRoomException();
                        }
                    } else if (cargo instanceof ToxicLiquidContainer) {
                        if (numH < maxNumH && numCon < maxNumCon && numTE < maxNumTE) {
                            containers.add(cargo);
                            numCon++;
                            numH++;
                            numTE++;
                            deadWeight += cargo.getGrossWeight();
                            cargo.setLastMoved(date);
                            result = true;
                        }
                    } else {
                        if (numCon < maxNumCon) {
                            containers.add(cargo);
                            numCon++;
                            deadWeight += cargo.getGrossWeight();
                            cargo.setLastMoved(date);
                            result = true;
                        } else {
                            throw new NotEnoughRoomException();
                        }
                    }
                } catch (NotEnoughRoomException e) {
                    System.out.println("Can't load " + cargo + " there is no space for it.");
                    result = false;
                }
            }

        } catch(OverWeightLimitException e) {
            System.out.println("Can't load " + cargo + " as this would bring the ship over it's weight limit.");
            result = false;
        }

        return result;
    }

    public boolean unloadContainer(StandardContainer cargo) {
        boolean found = false;
        StandardContainer tmp = null;
        try {
          for (StandardContainer c : containers) {
              if (c == cargo) {
                  tmp = c;
                  found = true;
              }
          }
          if (!found) {
              throw new CargoNotFoundException();
          } else {
              containers.remove(tmp);
              System.out.println(cargo + " has been unloaded from " + this);
              return true;
          }
        } catch (CargoNotFoundException e) {
            System.err.println("Could not remove the container, because it was not found");
        }

        return false;
    }
}
