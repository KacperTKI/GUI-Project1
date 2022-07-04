import Containers.ExplosiveContainer;
import Containers.StandardContainer;
import Containers.ToxicLiquidContainer;
import Containers.ToxicPowderyContainer;
import Exceptions.IrresponsibleSenderWithDangerousGoods;
import Exceptions.NotEnoughRoomException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Warehouse {
    private final int maxCon;
    private int conSize;
    private List<StandardContainer> containers;

    Warehouse(int max) {
        maxCon = max;
        conSize = 0;
        containers = Collections.synchronizedList(new ArrayList<StandardContainer>());
    }

    public int getMaxCon() { return maxCon; }

    public int getConSize() { return conSize; }

    public List<StandardContainer> getContainers() { return containers; }

    public void printContainers() {
        for (int i = 0; i < containers.size(); i++) {
            System.out.println("["+(i+1)+"] -- " + containers.get(i));
        }
    }

    public boolean hasContainer(StandardContainer cargo) {
        for (StandardContainer s : containers) {
            if (s == cargo) {
                return true;
            }
        }
        return false;
    }

    public boolean addContainer(StandardContainer container, LocalDate date) {
        boolean result;

        try {
            if (conSize + 1 > maxCon) {
                throw new NotEnoughRoomException();
            }
            container.setLastMoved(date);
            conSize++;
            containers.add(container);
            result = true;
        } catch (NotEnoughRoomException e) {
            System.out.println("There is not enough space in the warehouse for the container");
            result = false;
        }

        return result;
    }

    public boolean removeContainer(StandardContainer container) {
        if (hasContainer(container)) {
            containers.remove(container);
            this.conSize--;
            return true;
        }
        return false;
    }

    public StandardContainer checkContainers(LocalDate currDate) {
        if (containers.size() > 0) {
            for (StandardContainer sc : containers) {
                if (sc instanceof ExplosiveContainer) {
                    try {
                        if (sc.getLastMoved().plusDays(5).isBefore(currDate)) {
                            throw new IrresponsibleSenderWithDangerousGoods();
                        }
                    } catch(IrresponsibleSenderWithDangerousGoods e) {
                        System.out.println("WARNING: Container added " + sc.getLastMoved()
                                + " got disposed on " + currDate
                                + " - ID["+sc.getId()+"]");
                        return sc;

                    }
                } else if (sc instanceof ToxicLiquidContainer) {
                    try {
                        if (sc.getLastMoved().plusDays(10).isBefore(currDate)) {
                            throw new IrresponsibleSenderWithDangerousGoods();
                        }
                    } catch (IrresponsibleSenderWithDangerousGoods e) {
                        System.out.println("WARNING: Container added " + sc.getLastMoved()
                                + " got disposed on " + currDate
                                + " - ID[" + sc.getId() + "]");
                        return sc;
                    }
                } else if (sc instanceof ToxicPowderyContainer) {
                    try {
                        if (sc.getLastMoved().plusDays(14).isBefore(currDate)) {
                            throw new IrresponsibleSenderWithDangerousGoods();
                        }
                    } catch (IrresponsibleSenderWithDangerousGoods e) {
                        System.out.println("WARNING: Container added " + sc.getLastMoved()
                                + " got disposed on " + currDate
                                + " - ID[" + sc.getId() + "]");
                        return sc;
                    }
                }
            }
        }
        return null;
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
        System.out.println("There are no containers in the warehouse!");
        return null;
    }
}
