import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.time.LocalDateTime;

import Main.BloodCenter;
import Main.User;
import Main.UserType;
import Main.Telephone;
import Main.Address;

class BloodCenterTest {

    @Test
    public void testCreateBloodCenterWithData() {
        UserType type = new UserType("Admin");
        type.setId(1);

        User user = new User("João", "joao@email.com", "123456", LocalDateTime.now(), type);
        user.setId(4);

        BloodCenter center = new BloodCenter("12345678901234", "Banco de Sangue", user);

        assertEquals(12345678901234L, center.getCnpj());
        assertEquals("Banco de Sangue", center.getCompanyName());
        assertEquals(user, center.getUser());
    }

    @Test
    public void testToMap() {
        User user = new User(5);
        user.setId(5);

        BloodCenter center = new BloodCenter("123456789", "Hemocentro A", user);
        Map<String, String> map = center.toMap();

        assertEquals("123456789", map.get("cnpj"));
        assertEquals("Hemocentro A", map.get("razao_Social"));
        assertEquals("5", map.get("id_Usuario"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "cnpj", "111111111",
            "razao_Social", "Hemocentro B",
            "id_Usuario", "7"
        );

        BloodCenter center = new BloodCenter(null, null, null);
        center.populate(data);

        assertEquals(111111111, center.getCnpj());
        assertEquals("Hemocentro B", center.getCompanyName());
        assertNotNull(center.getUser());
        assertEquals(7, center.getUser().getId());
    }

    @Test
    public void testPopulateWithMissingFields() {
        Map<String, String> data = Map.of("razao_Social", "Sem CNPJ");

        BloodCenter center = new BloodCenter(null, "", new User(1));
        center.populate(data);

        assertEquals(0L, center.getCnpj());
        assertEquals("Sem CNPJ", center.getCompanyName());
        assertEquals(0, center.getUser().getId());
    }

    @Test
    public void testRoundTripConversion() {
        User user = new User(9);
        user.setId(9);

        BloodCenter original = new BloodCenter("987654321", "Hemocentro Z", user);
        Map<String, String> map = original.toMap();

        BloodCenter copy = new BloodCenter(null, null, null);
        copy.populate(map);

        assertEquals(original.getCnpj(), copy.getCnpj());
        assertEquals(original.getCompanyName(), copy.getCompanyName());
        assertEquals(original.getUser().getId(), copy.getUser().getId());
    }

    @Test
    public void testConstructorWithId() {
        BloodCenter center = new BloodCenter(1);
        assertNotNull(center);
    }
}
