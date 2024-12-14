package garage;

import static org.junit.jupiter.api.Assertions.*;
import static io.qameta.allure.SeverityLevel.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class GarageImplTest {
    private GarageImpl garage;
    private Owner sample_owner;
    private Owner[] sample_owners;
    static private final Random rand = new Random();

    @BeforeEach
    void setUp() {
        garage = new GarageImpl();
        sample_owner = new Owner("Alexey", "Orlov", 33);

        sample_owners = new Owner[3];
        sample_owners[0] = new Owner("Nick", "Bronson", 23);
        sample_owners[1] = new Owner("John", "Smith", 45);
        sample_owners[2] = new Owner("Theodor", "Schmidt", 29);
    }

    @Test
    void allCarsUniqueOwners() {
        garage.addNewCar(createRandomCar(1), sample_owners[0]);
        garage.addNewCar(createRandomCar(2), sample_owners[1]);
        garage.addNewCar(createRandomCar(3), sample_owners[1]);
        garage.addNewCar(createRandomCar(4), sample_owners[2]);
        garage.addNewCar(createRandomCar(5), sample_owners[2]);
        garage.addNewCar(createRandomCar(6), sample_owners[2]);

        var owners = garage.allCarsUniqueOwners();
        HashSet<Owner> realOwners = new HashSet<>(List.of(sample_owners));
        Assertions.assertEquals(realOwners.size(), owners.size());

        for (var owner : owners) {
            Assertions.assertTrue(realOwners.contains(owner));
        }
    }

    @Test
    void topThreeCarsByMaxVelocity() {
        var car1 = createRandomCarWithExactMaxVelocity(1, 100);
        var car2 = createRandomCarWithExactMaxVelocity(2, 10);
        var car3 = createRandomCarWithExactMaxVelocity(3, 50);
        var car4 = createRandomCarWithExactMaxVelocity(4, 200);
        var car5 = createRandomCarWithExactMaxVelocity(5, 10);
        var car6 = createRandomCarWithExactMaxVelocity(6, 100);
        garage.addNewCar(car1, sample_owner);
        garage.addNewCar(car2, sample_owner);
        garage.addNewCar(car3, sample_owner);
        garage.addNewCar(car4, sample_owner);
        garage.addNewCar(car5, sample_owner);
        garage.addNewCar(car6, sample_owner);
        Assertions.assertEquals(List.of(car4, car6, car1), garage.topThreeCarsByMaxVelocity());

        garage.removeCar(2);
        garage.removeCar(3);
        garage.removeCar(5);
        Assertions.assertEquals(List.of(car4, car6, car1), garage.topThreeCarsByMaxVelocity());
    }

    @Test
    void allCarsOfBrand() {
        List<String> brands = List.of("Audi", "BMW", "Mercedes");
        int carIndex = 0;
        for (var brand : brands) {
            ArrayList<Car> carsWithExactBrand = new ArrayList<>();

            for (int j = 0; j < 3; ++j) {
                var car = createRandomCarWithExactBrand(carIndex++, brand);
                carsWithExactBrand.add(car);
                garage.addNewCar(car, sample_owners[j]);
            }

            Assertions.assertEquals(carsWithExactBrand, garage.allCarsOfBrand(brand));
        }
    }

    @Test
    void carsWithPowerMoreThan() {
        var car1 = createRandomCarWithExactPower(1, 100);
        var car2 = createRandomCarWithExactPower(2, 10);
        var car3 = createRandomCarWithExactPower(3, 50);
        var car4 = createRandomCarWithExactPower(4, 200);
        var car5 = createRandomCarWithExactPower(5, 10);
        var car6 = createRandomCarWithExactPower(6, 100);
        garage.addNewCar(car1, sample_owner);
        garage.addNewCar(car2, sample_owner);
        garage.addNewCar(car3, sample_owner);
        garage.addNewCar(car4, sample_owner);
        garage.addNewCar(car5, sample_owner);
        garage.addNewCar(car6, sample_owner);

        var expected = List.of(car4, car6, car1);
        var actual = garage.carsWithPowerMoreThan(50);
        
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void allCarsOfOwner() {
        var car1 = createRandomCar(1);
        var car2 = createRandomCar(2);
        var car3 = createRandomCar(3);
        var car4 = createRandomCar(4);
        var car5 = createRandomCar(5);
        var car6 = createRandomCar(6);
        garage.addNewCar(car1, sample_owners[0]);
        garage.addNewCar(car2, sample_owners[1]);
        garage.addNewCar(car3, sample_owners[1]);
        garage.addNewCar(car4, sample_owners[2]);
        garage.addNewCar(car5, sample_owners[2]);
        garage.addNewCar(car6, sample_owners[2]);

        Assertions.assertEquals(List.of(car1), garage.allCarsOfOwner(sample_owners[0]));
        Assertions.assertEquals(List.of(car2, car3), garage.allCarsOfOwner(sample_owners[1]));
        Assertions.assertEquals(List.of(car4, car5, car6), garage.allCarsOfOwner(sample_owners[2]));
    }

    @Test
    void meanOwnersAgeOfCarBrand() {
        List<String> brands = List.of("Audi", "BMW", "Mercedes");
        int i = 0;
        for (var brand : brands) {
            garage.addNewCar(createRandomCarWithExactBrand(i++, brand), sample_owners[0]);
            if (i > 1) {
                garage.addNewCar(createRandomCarWithExactBrand(i++, brand), sample_owners[1]);
                garage.addNewCar(createRandomCarWithExactBrand(i++, brand), sample_owners[1]);
            }
            if (i > 1 + 3) {
                garage.addNewCar(createRandomCarWithExactBrand(i++, brand), sample_owners[2]);
                garage.addNewCar(createRandomCarWithExactBrand(i++, brand), sample_owners[2]);
                garage.addNewCar(createRandomCarWithExactBrand(i++, brand), sample_owners[2]);
            }
        }

        Assertions.assertEquals(sample_owners[0].getAge(),
                garage.meanOwnersAgeOfCarBrand(brands.get(0)));
        Assertions.assertEquals((
                sample_owners[0].getAge() + sample_owners[1].getAge()) / 3,
                garage.meanOwnersAgeOfCarBrand(brands.get(1)));
        Assertions.assertEquals(
                (sample_owners[0].getAge() + sample_owners[1].getAge() + sample_owners[2].getAge()) / 6,
                garage.meanOwnersAgeOfCarBrand(brands.get(2)));
    }

    @Test
    void meanOwnersAgeOfCarBrandNegative() {
        List<String> brands = List.of("Audi", "BMW", "Mercedes");
        int i = 0;
        for (var brand : brands) {
            if (!brand.equals(brands.get(0))) {
                garage.addNewCar(createRandomCarWithExactBrand(i++, brand), sample_owner);
            }
        }

        Assertions.assertThrows(IllegalArgumentException.class, () -> garage.meanOwnersAgeOfCarBrand(brands.get(0)));
    }

    @Test
    void meanCarNumberForEachOwner() {
        garage.addNewCar(createRandomCar(1), sample_owners[0]);
        garage.addNewCar(createRandomCar(2), sample_owners[1]);
        garage.addNewCar(createRandomCar(3), sample_owners[1]);
        garage.addNewCar(createRandomCar(4), sample_owners[2]);
        garage.addNewCar(createRandomCar(5), sample_owners[2]);
        garage.addNewCar(createRandomCar(6), sample_owners[2]);

        Assertions.assertEquals(2, garage.meanCarNumberForEachOwner());
    }

    @Test
    void meanCarNumberForEachOwnerNegative() {
        Assertions.assertEquals(0, garage.meanCarNumberForEachOwner());
    }

    @Test
    void removeNonExistentCar() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> garage.removeCar(1));
        Assertions.assertEquals(exception.getMessage(), "This car is not in garage!");
    }

    @Test
    void DoubleRemoveCar() {
        garage.addNewCar(createRandomCar(1), sample_owner);
        garage.removeCar(1);
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> garage.removeCar(1));
        Assertions.assertEquals(exception.getMessage(), "This car is not in garage!");
    }

    @Test
    void checkCarReturnedFromRemoveCar() {
        Owner owner = sample_owner;
        Car car = createRandomCar(1);
        garage.addNewCar(car, owner);
        Assertions.assertEquals(car, garage.removeCar(1));
    }

    @Test
    void checkMultipleCarsReturnedFromRemoveCar() {
        Car car1 = createRandomCar(1);
        Car car2 = createRandomCar(2);
        Car car3 = createRandomCar(3);
        garage.addNewCar(car1, sample_owners[0]);
        garage.addNewCar(car2, sample_owners[1]);
        garage.addNewCar(car3, sample_owners[2]);
        Assertions.assertEquals(car1, garage.removeCar(1));
        Assertions.assertEquals(car2, garage.removeCar(2));
        Assertions.assertEquals(car3, garage.removeCar(3));
    }

    @Test
    void sequentionalAddRemoveCar() {
        for (int i = 0; i < 3; ++i) {
            Car car = createRandomCar(i + 1);
            garage.addNewCar(car, sample_owners[i]);

            Assertions.assertEquals(car, garage.removeCar(i + 1));
        }
    }

    @Test
    void DoubleAddCar() {
        var car = createRandomCar(1);
        garage.addNewCar(car, sample_owner);
        Assertions.assertDoesNotThrow(() -> garage.addNewCar(car, sample_owner));
    }


    public static Car createRandomCar(int carId) {
        return new Car(carId, "Audi", "A7", rand.nextInt(200), rand.nextInt(1000));
    }

    public static Car createRandomCarWithExactBrand(int carId, String brand) {
        return new Car(carId, brand, "", rand.nextInt(200), rand.nextInt(1000));
    }

    public static Car createRandomCarWithExactMaxVelocity(int carId, int maxVelocity) {
        return new Car(carId, "Audi", "A7", maxVelocity, rand.nextInt(1000));
    }

    public static Car createRandomCarWithExactPower(int carId, int power) {
        return new Car(carId, "Audi", "A7", rand.nextInt(200), power);
    }
}