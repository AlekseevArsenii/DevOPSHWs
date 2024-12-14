package garage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OwnerTest {
    @Test
    void getName() {
        Owner owner = new Owner("a", "b", 12);
        assertEquals("a", owner.getName());
    }

    @Test
    void getLastName() {
        Owner owner = new Owner("a", "b", 12);
        assertEquals("b", owner.getLastName());
    }

    @Test
    void getAge() {
        Owner owner = new Owner("a", "b", 12);
        assertEquals(12, owner.getAge());
    }

    @Test
    void equals() {
        Owner owner1 = new Owner("a", "b", 12);
        assertEquals(true, owner1.equals(owner1));
        assertEquals(false, owner1.equals(null));
        Owner owner2 = new Owner("a", "b", 12);
        assertEquals(true, owner1.equals(owner2));
    }
}
