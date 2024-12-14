package garage;

import java.util.Comparator;

public class MaxVelocityCarComparator implements Comparator<Car> {
    @Override
    public int compare(Car o1, Car o2) throws ArithmeticException {
        if (o2.getMaxVelocity() == o1.getMaxVelocity()) {
            return o2.getCarId() - o1.getCarId();
        }
        return Math.subtractExact(o2.getMaxVelocity(), o1.getMaxVelocity());
    }
}