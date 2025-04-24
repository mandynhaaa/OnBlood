import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Map;
import Main.BloodType;

class BloodTypeTest {

    @Test
    public void testCreateBloodTypeWithDescription() {
        BloodType type = new BloodType("A+");
        assertEquals("A+", type.getDescription());
    }

    @Test
    public void testToMap() {
        BloodType type = new BloodType("O-");
        Map<String, String> map = type.toMap();
        assertEquals("O-", map.get("descricao"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of("descricao", "B+");
        BloodType type = new BloodType("");
        type.populate(data);
        assertEquals("B+", type.getDescription());
    }

    @Test
    public void testPopulateWithMissingField() {
        Map<String, String> data = Map.of();
        BloodType type = new BloodType("AB+");
        type.populate(data);
        assertNull(type.getDescription());
    }

    @Test
    public void testRoundTripMapConversion() {
        BloodType original = new BloodType("AB-");
        Map<String, String> map = original.toMap();
        BloodType copy = new BloodType("");
        copy.populate(map);
        assertEquals(original.getDescription(), copy.getDescription());
    }

    @Test
    public void testConstructorWithId() {
        BloodType type = new BloodType(1);
        assertNotNull(type);
    }
}
