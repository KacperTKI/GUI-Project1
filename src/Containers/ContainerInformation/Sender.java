package Containers.ContainerInformation;

import Containers.StandardContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sender {
    private String firstName;
    private String lastName;
    private String pesel;
    private String address;
    // Disposed Conatainers list
    List<StandardContainer> disposedContainers;

    public Sender(String firstName, String lastName, String pesel, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.address = address;
        this.disposedContainers = Collections.synchronizedList(new ArrayList<StandardContainer>());
    }

    @Override
    public String toString() { return "Name[" + this.firstName + " " + this.lastName + "] - Pesel["
            + pesel + "] - Address[" + address + "] - Birthdate[" + getBoD() + "]"; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPesel() {
        return pesel;
    }

    public List<StandardContainer> getDisposedContainers() {
        return disposedContainers;
    }

    public LocalDate getBoD() {
        int year = Integer.parseInt(pesel.substring(0,2));
        int month = Integer.parseInt(pesel.substring(2,4));

        if (month >= 1 && month <= 12) {
            year += 1900;
        } else if (month >= 21 && month <= 32) {
            year += 2000;
            month -= 20;
        } else if (month >= 41 && month <= 52) {
            year += 2100;
            month -= 40;
        } else if (month >= 61 && month <= 72) {
            year += 2200;
            month -= 60;
        } else if (month >= 81 && month <= 92) {
            year += 1800;
            month -= 80;
        }

        int day = Integer.parseInt(pesel.substring(4, 6));

        return LocalDate.of(year, month, day);
    }

    public void addDisposedContainer(StandardContainer disposedContainer) {
        disposedContainers.add(disposedContainer);
    }
}
