package garage;

import java.util.*;

public class GarageImpl implements Garage {
    private final Map<Owner, List<Car>> ownerToCars;
    private final Map<Integer, Owner> carIdToOwner;
    private final Map<Integer, Car> carIdToCar;
    private final Map<String, List<Car>> brandToCars;
    private final TreeSet<Car> carsSortedByPower;
    private final TreeSet<Car> carsSortedByVelocity;

    public GarageImpl() {
        ownerToCars = new HashMap<>();
        carIdToOwner = new HashMap<>();
        carIdToCar = new HashMap<>();
        brandToCars = new HashMap<>();
        carsSortedByPower = new TreeSet<>(new PowerCarComparator());
        carsSortedByVelocity = new TreeSet<>(new MaxVelocityCarComparator());
    }

    @Override
    public Collection<Owner> allCarsUniqueOwners() {
        return Collections.unmodifiableSet(ownerToCars.keySet());
    }

    @Override
    public Collection<Car> topThreeCarsByMaxVelocity() {
        List<Car> result = new ArrayList<>();
        Iterator<Car> iterator = carsSortedByVelocity.iterator();
        for (int i = 0; i < 3 && iterator.hasNext(); ++i) {
            result.add(iterator.next());
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public Collection<Car> allCarsOfBrand(String brand) {
        return Collections.unmodifiableList(brandToCars.getOrDefault(brand, new ArrayList<>()));
    }

    @Override
    public Collection<Car> carsWithPowerMoreThan(int power) {
        List<Car> result = new ArrayList<>();
        for (Car car : carsSortedByPower) {
            if (car.getPower() <= power) {
                break;
            }
            result.add(car);
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public Collection<Car> allCarsOfOwner(Owner owner) {
        return Collections.unmodifiableList(ownerToCars.getOrDefault(owner, new ArrayList<>()));
    }

    @Override
    public int meanOwnersAgeOfCarBrand(String brand) {
        var carsOfThisBrand = allCarsOfBrand(brand);
        if (carsOfThisBrand == null || carsOfThisBrand.isEmpty()) {
            throw new IllegalArgumentException("There is no cars of this brand!");
        }
        HashSet<Owner> uniqueOwners = new HashSet<>();
        int sumOfAges = 0;
        for (var car : carsOfThisBrand) {
            var owner = carIdToOwner.get(car.getCarId());
            if (uniqueOwners.contains(owner)) {
                continue;
            }
            sumOfAges += owner.getAge();
            uniqueOwners.add(owner);
        }
        return sumOfAges / carsOfThisBrand.size();
    }

    @Override
    public int meanCarNumberForEachOwner() {
        var allOwners = allCarsUniqueOwners();
        if (allOwners.isEmpty()) {
            return 0;
        }
        var allCars = carIdToCar.keySet();
        return allCars.size() / allOwners.size();
    }

    @Override
    public Car removeCar(int carId) {
        if (!carIdToOwner.containsKey(carId)) {
            throw new IllegalArgumentException("This car is not in garage!");
        }
        var car = carIdToCar.get(carId);
        removeFromOwnerToCars(car);
        removeFromBrandToCars(car);
        removeFromSortedCollections(car);
        carIdToOwner.remove(carId);
        return carIdToCar.remove(carId);
    }

    private void removeFromOwnerToCars(Car car) {
        var owner = carIdToOwner.get(car.getCarId());

        ownerToCars.get(owner).remove(car);
        if (ownerToCars.get(owner).isEmpty()) {
            ownerToCars.remove(owner);
        }
    }

    private void removeFromBrandToCars(Car car) {
        brandToCars.get(car.getBrand()).remove(car);
        if (brandToCars.get(car.getBrand()).isEmpty()) {
            brandToCars.remove(car.getBrand());
        }
    }

    private void removeFromSortedCollections(Car car) {
        carsSortedByPower.remove(car);
        carsSortedByVelocity.remove(car);
    }

    @Override
    public void addNewCar(Car car, Owner owner) {
        if (car == null || owner == null) {
            throw new IllegalArgumentException("Arguments should not be nulls!");
        }

        if (carIdToOwner.containsKey(car.getCarId())) {
            return;
        }

        ownerToCars.computeIfAbsent(owner, k -> new ArrayList<>());
        ownerToCars.get(owner).add(car);

        carIdToOwner.put(car.getCarId(), owner);
        carIdToCar.put(car.getCarId(), car);

        brandToCars.computeIfAbsent(car.getBrand(), k -> new ArrayList<>());
        brandToCars.get(car.getBrand()).add(car);

        carsSortedByPower.add(car);
        carsSortedByVelocity.add(car);
    }
}