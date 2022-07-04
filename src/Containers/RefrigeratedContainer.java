package Containers;

import Containers.ContainerInformation.SecurityLevel;
import Containers.ContainerInformation.Sender;

import java.util.ArrayList;

public class RefrigeratedContainer extends HeavyContainer {
    private double neededVoltage;

    public RefrigeratedContainer(Sender sen, double net, ArrayList<String> cert, double tare, SecurityLevel level) {
        super(sen, net, cert, tare, level);
    }

    @Override
    public String getType() {
        return "RefrigeratedContainer";
    }
}
