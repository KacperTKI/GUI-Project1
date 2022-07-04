package Containers;

import Containers.ContainerInformation.SecurityLevel;
import Containers.ContainerInformation.Sender;

import java.util.ArrayList;

public class ToxicLiquidContainer extends ToxicContainer implements LiquidCargo {
    private double liters;

    public ToxicLiquidContainer(Sender sen, double net, ArrayList<String> cert, double tare, SecurityLevel level) {
        super(sen, net, cert, tare, level);
    }

    @Override
    public String getType() {
        return "ToxicLiquidContainer";
    }
}
