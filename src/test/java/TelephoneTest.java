import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import Main.Telephone;
import Main.User;
import Main.UserType;

import java.time.LocalDateTime;
import java.util.Map;

class TelephoneTest {

    @Test
    public void testCreateTelephoneWithData() {
        UserType userType = new UserType(1);
        
        User user = new User("João", "joao@email.com", "123456", LocalDateTime.of(2024, 1, 1, 0, 0), userType);
        
        Telephone phone = new Telephone("Celular", "48", "999999999", user);

        assertEquals("Celular", phone.getDescription());
        assertEquals("48", phone.getDdd());
        assertEquals("999999999", phone.getNumber());
    }

    @Test
    public void testToMap() {
        UserType userType = new UserType(1);
        
        User user = new User("João", "joao@email.com", "123456", LocalDateTime.of(2024, 1, 1, 0, 0), userType);
        
        Telephone phone = new Telephone("Residencial", "11", "12345678", user);

        Map<String, String> data = phone.toMap();

        assertEquals("Residencial", data.get("descricao"));
        assertEquals("11", data.get("ddd"));
        assertEquals("12345678", data.get("numero"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "descricao", "Trabalho",
            "ddd", "21",
            "numero", "88887777"
        );

        Telephone phone = new Telephone("", "", "", null);
        phone.populate(data);

        assertEquals("Trabalho", phone.getDescription());
        assertEquals("21", phone.getDdd());
        assertEquals("88887777", phone.getNumber());
    }

    @Test
    public void testPopulateWithMissingFields() {
        Map<String, String> data = Map.of("descricao", "Telefone desconhecido");

        Telephone phone = new Telephone("", "", "", null);
        phone.populate(data);

        assertEquals("Telefone desconhecido", phone.getDescription());
        assertEquals(null, phone.getDdd());
        assertEquals(null, phone.getNumber());
    }

    @Test
    public void testRoundTripMapConversion() {
        UserType userType = new UserType(1);
        
        User user = new User("João", "joao@email.com", "123456", LocalDateTime.of(2024, 1, 1, 0, 0), userType);
        
        Telephone original = new Telephone("Fixo", "41", "22223333", user);

        Map<String, String> map = original.toMap();
        Telephone copy = new Telephone("", "", "", user);
        copy.populate(map);

        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getDdd(), copy.getDdd());
        assertEquals(original.getNumber(), copy.getNumber());
    }

    @Test
    public void testExtremeValues() {
        Telephone phone = new Telephone("Teste", "", "", null);

        assertEquals("Teste", phone.getDescription());
        assertEquals("", phone.getDdd());
        assertEquals("", phone.getNumber());
    }

    @Test
    public void testConstructorWithId() {
        Telephone phone = new Telephone(1);
        assertNotNull(phone);
    }
}
