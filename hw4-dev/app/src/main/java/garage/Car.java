package garage;

import java.util.Objects;

public class Car {
    private final int carId;
    private final String brand;
    private final String modelName;

    private final int maxVelocity;
    private final int power;
//    private final int ownerId; // not used

    public Car(int carId, String brand, String modelName, int maxVelocity, int power) {
        this.carId = carId;
        this.brand = brand;
        this.modelName = modelName;
        this.maxVelocity = maxVelocity;
        this.power = power;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car car)) return false;
        return carId == car.carId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(carId);
    }

    public int getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModelName() {
        return modelName;
    }

    public int getMaxVelocity() {
        return maxVelocity;
    }

    public int getPower() {
        return power;
    }
}
