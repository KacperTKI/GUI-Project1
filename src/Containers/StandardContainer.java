package Containers;

import Containers.ContainerInformation.SecurityLevel;
import Containers.ContainerInformation.Sender;

import java.time.LocalDate;
import java.util.ArrayList;

public class StandardContainer {
    // the id of the container
    private final int id;
    // the sender details
    private final Sender sender;
    // weight of the container (packaging)
    private final double tareWeight;
    // weight of the product (packaging does not count)
    private final double netWeight;
    // net and tare weight in one
    private final double grossWeight;
    // The security level of the container
    private SecurityLevel securityLevel;
    private final ArrayList<String> certificates;

    // hold the date for when the container was last moved
    private LocalDate lastMoved;

    // counter for creating the id
    static int counter = 0;

    public StandardContainer(Sender sender, double netWeight,
                             ArrayList<String> certificates, double tareWeight,
                             SecurityLevel securityLevel) {
        this.id = counter++;

        this.sender = sender;

        this.tareWeight = tareWeight;
        this.netWeight = netWeight;
        this.grossWeight = tareWeight + netWeight;

        this.securityLevel = securityLevel;

        this.certificates = certificates;
    }

    @Override
    public String toString() {
        return  "["+getType() +"] - ID[" + id + "] - Sender[" + sender + "] - Gross Weight["
                + grossWeight + "] Security Level[" + securityLevel
                + "] - Certificate(s)[" + certificates + "]" + "- Last Moved[" + lastMoved + "]";
    }

    public String getAllInfo() {
        return this + " - Tare Weight[" + tareWeight + "] - Net Weight["
                + netWeight + "] - Last Moved[" + lastMoved + "]";
    }

    public String getType() {
        return "StandardContainer";
    }

    public int getId() {
        return id;
    }

    public Sender getSender() {
        return sender;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public SecurityLevel getSecurityLevel() {
        return securityLevel;
    }

    public LocalDate getLastMoved() {
        return lastMoved;
    }

    public void setLastMoved(LocalDate lastMoved) {
        this.lastMoved = lastMoved;
    }
}
