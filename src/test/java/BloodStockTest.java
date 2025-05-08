import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import Main.BloodStock;
import Main.BloodCenter;
import Main.BloodType;
import Main.User;

class BloodStockTest {

    @Test
    public void testCreateBloodStockWithData() {
        BloodCenter center = new BloodCenter("10", "Hemocentro Central", new User(1));
        center.setId(100);

        BloodType type = new BloodType(1);
        type.setId(200);

        BloodStock stock = new BloodStock(LocalDate.of(2025, 4, 20), 12.5f, center, type);

        assertEquals(LocalDate.of(2025, 4, 20), stock.getLastUpdateData());
        assertEquals(12.5f, stock.getVolume());
        assertEquals(center, stock.getBloodCenter());
        assertEquals(type, stock.getBloodType());
    }

    @Test
    public void testToMap() {
        BloodCenter center = new BloodCenter("10", "Hemocentro", new User(1));
        center.setId(300);

        BloodType type = new BloodType(2);
        type.setId(400);

        BloodStock stock = new BloodStock(LocalDate.of(2025, 4, 21), 7.8f, center, type);

        Map<String, String> map = stock.toMap();

        assertEquals("2025-04-21", map.get("data_Atualizacao"));
        assertEquals("7.8", map.get("volume"));
        assertEquals("300", map.get("id_Hemocentro"));
        assertEquals("400", map.get("id_TipoSanguineo"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "data_Atualizacao", "2025-04-22",
            "volume", "5.5",
            "id_Hemocentro", "101",
            "id_TipoSanguineo", "202"
        );

        BloodStock stock = new BloodStock(1);
        stock.populate(data);

        assertEquals(LocalDate.of(2025, 4, 22), stock.getLastUpdateData());
        assertEquals(5.5f, stock.getVolume());
        assertNotNull(stock.getBloodCenter());
        assertEquals(101, stock.getBloodCenter().getId());
        assertNotNull(stock.getBloodType());
        assertEquals(202, stock.getBloodType().getId());
    }

    @Test
    public void testConstructorWithId() {
        BloodStock stock = new BloodStock(1);
        assertNotNull(stock);
    }

    @Test
    public void testRoundTripMapConversion() {
        BloodCenter center = new BloodCenter("999", "Banco de Sangue", new User(1));
        center.setId(888);

        BloodType type = new BloodType(3);
        type.setId(777);

        BloodStock original = new BloodStock(LocalDate.of(2025, 4, 23), 9.1f, center, type);
        Map<String, String> map = original.toMap();

        BloodStock copy = new BloodStock(0);
        copy.populate(map);

        assertEquals(original.getLastUpdateData(), copy.getLastUpdateData());
        assertEquals(original.getVolume(), copy.getVolume());
        assertEquals(original.getBloodCenter().getId(), copy.getBloodCenter().getId());
        assertEquals(original.getBloodType().getId(), copy.getBloodType().getId());
    }
}
