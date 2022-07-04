package Containers;

import Containers.ContainerInformation.SecurityLevel;
import Containers.ContainerInformation.Sender;

import java.util.ArrayList;

public class ToxicContainer extends HeavyContainer {
    private boolean isSafeToTransport;

    public ToxicContainer(Sender sen, double net, ArrayList<String> cert, double tare, SecurityLevel level) {
        super(sen, net, cert, tare, level);
    }

    @Override
    public String getType() {
        return "ToxicContainer";
    }
}
