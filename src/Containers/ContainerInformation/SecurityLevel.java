package Containers.ContainerInformation;

public enum SecurityLevel {
    LOW, MEDIUM, HIGH;

    public String printLevel() {
        return switch (this) {
            case LOW -> "low";
            case HIGH -> "high";
            case MEDIUM -> "medium";
        };
    }
}
