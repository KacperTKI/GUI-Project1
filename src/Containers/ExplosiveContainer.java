package Containers;

import Containers.ContainerInformation.SecurityLevel;
import Containers.ContainerInformation.Sender;

import java.util.ArrayList;

public class ExplosiveContainer extends StandardContainer {
    private int tonnesTNT;

    public ExplosiveContainer(Sender sen, double net, ArrayList<String> cert, double tare, SecurityLevel level) {
        super(sen, net, cert, tare, level);
    }

    @Override
    public String getType() {
        return "ExplosiveContainer";
    }
}
