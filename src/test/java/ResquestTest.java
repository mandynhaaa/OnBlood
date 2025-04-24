import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;
import Main.Request;
import Main.BloodCenter;
import Main.BloodType;
import Main.User;

class RequestTest {

    @Test
    public void testCreateRequestWithData() {
        BloodCenter center = new BloodCenter(12345678901234L, "Hemocentro 1", new User(1));
        center.setId(1);

        BloodType type = new BloodType("A+");
        type.setId(2);

        Request request = new Request("Pendente", 3.5f, LocalDate.of(2025, 4, 28), center, type);

        assertEquals("Pendente", request.getStatus());
        assertEquals(3.5f, request.getVolume());
        assertEquals(LocalDate.of(2025, 4, 28), request.getDatetime());
        assertEquals(center, request.getBloodCenter());
        assertEquals(type, request.getBloodType());
    }

    @Test
    public void testToMap() {
        BloodCenter center = new BloodCenter(12345678900000L, "Hemocentro 2", new User(1));
        center.setId(10);

        BloodType type = new BloodType("B+");
        type.setId(20);

        Request request = new Request("Aprovada", 2.2f, LocalDate.of(2025, 4, 29), center, type);

        Map<String, String> map = request.toMap();

        assertEquals("Aprovada", map.get("status"));
        assertEquals("2.2", map.get("volume"));
        assertEquals("2025-04-29", map.get("data_Hora"));
        assertEquals("10", map.get("id_Hemocentro"));
        assertEquals("20", map.get("id_Tipo_Sanguineo"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "status", "Finalizada",
            "volume", "6.6",
            "data_Hora", "2025-04-30",
            "id_Hemocentro", "111",
            "id_Tipo_Sanguineo", "222"
        );

        Request request = new Request(1);
        request.populate(data);

        assertEquals("Finalizada", request.getStatus());
        assertEquals(6.6f, request.getVolume());
        assertEquals(LocalDate.of(2025, 4, 30), request.getDatetime());
        assertEquals(111, request.getBloodCenter().getId());
        assertEquals(222, request.getBloodType().getId());
    }

    @Test
    public void testConstructorWithId() {
        Request request = new Request(1);
        assertNotNull(request);
    }

    @Test
    public void testRoundTripMapConversion() {
        BloodCenter center = new BloodCenter(12345678900001L, "Hemocentro 3", new User(1));
        center.setId(30);

        BloodType type = new BloodType("O-");
        type.setId(40);

        Request original = new Request("Cancelada", 4.4f, LocalDate.of(2025, 5, 1), center, type);
        Map<String, String> map = original.toMap();

        Request copy = new Request(0);
        copy.populate(map);

        assertEquals(original.getStatus(), copy.getStatus());
        assertEquals(original.getVolume(), copy.getVolume());
        assertEquals(original.getDatetime(), copy.getDatetime());
        assertEquals(original.getBloodCenter().getId(), copy.getBloodCenter().getId());
        assertEquals(original.getBloodType().getId(), copy.getBloodType().getId());
    }
}
