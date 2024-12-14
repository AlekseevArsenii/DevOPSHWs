package garage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CarTest {
    @Test
    void getCarId() {
        Car car = new Car(2, "a", "b", 1, 2);
        assertEquals(2, car.getCarId());
    }

    @Test
    void getBrand() {
        Car car = new Car(1, "a", "b", 1, 2);
        assertEquals("a", car.getBrand());
    }

    @Test
    void getModelName() {
        Car car = new Car(1, "a", "b", 1, 2);
        assertEquals("b", car.getModelName());
    }

    @Test
    void getMaxVelocity() {
        Car car = new Car(1, "a", "b", 1, 2);
        assertEquals(1, car.getMaxVelocity());
    }

    @Test
    void getPower() {
        Car car = new Car(1, "a", "b", 1, 2);
        assertEquals(2, car.getPower());
    }

    @Test
    void equals() {
        Car car1 = new Car(1, "a", "b", 1, 2);
        assertEquals(true, car1.equals(car1));
        assertEquals(false, car1.equals(null));
        Car car2 = new Car(1, "a", "b", 1, 2);
        assertEquals(true, car1.equals(car2));
    }
}
