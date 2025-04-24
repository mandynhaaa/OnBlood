import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import Main.UserType;

import java.util.Map;

class UserTypeTest {

    @Test
    public void testCreateUserTypeWithData() {
        UserType userType = new UserType("Administrador");

        assertEquals("Administrador", userType.getDescription());
    }

    @Test
    public void testToMap() {
        UserType userType = new UserType("Operador");

        Map<String, String> data = userType.toMap();

        assertEquals("Operador", data.get("descricao"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of("descricao", "Visitante");

        UserType userType = new UserType("");
        userType.populate(data);

        assertEquals("Visitante", userType.getDescription());
    }

    @Test
    public void testPopulateWithMissingField() {
        Map<String, String> data = Map.of();

        UserType userType = new UserType("Padrão");
        userType.populate(data);

        assertNull(userType.getDescription());
    }

    @Test
    public void testRoundTripMapConversion() {
        UserType original = new UserType("Gestor");

        Map<String, String> map = original.toMap();
        UserType copy = new UserType("");
        copy.populate(map);

        assertEquals(original.getDescription(), copy.getDescription());
    }

    @Test
    public void testConstructorWithId() {
        UserType userType = new UserType(1);
        assertNotNull(userType);
    }
}
