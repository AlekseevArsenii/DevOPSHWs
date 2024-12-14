package garage;

import java.util.Comparator;

public class PowerCarComparator implements Comparator<Car> {
    @Override
    public int compare(Car o1, Car o2) throws ArithmeticException {
        if (o2.getPower() == o1.getPower()) {
            return o2.getCarId() - o1.getCarId();
        }
        return Math.subtractExact(o2.getPower(), o1.getPower());
    }
}