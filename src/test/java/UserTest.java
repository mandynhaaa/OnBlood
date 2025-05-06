import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;

import Main.User;
import Main.UserType;
import Main.Telephone;
import Main.Address;

class UserTest {

    @Test
    public void testCreateUserWithData() {
        UserType userType = new UserType("Admin");
        userType.setId(1);
        
        User user = new User("João", "joao@email.com", "123456", LocalDate.of(2024, 1, 1), userType);

        assertEquals("João", user.getName());
        assertEquals("joao@email.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals(LocalDate.of(2024, 1, 1), user.getCreationDate());
        assertEquals(userType, user.getUserType());
    }

    @Test
    public void testToMap() {
        UserType userType = new UserType("Admin");
        userType.setId(1);

        User user = new User("João", "joao@email.com", "123456", LocalDate.now(), userType);

        Map<String, String> data = user.toMap();

        assertEquals("João", data.get("nome"));
        assertEquals("joao@email.com", data.get("email"));
        assertEquals("123456", data.get("senha"));
        assertEquals("1", data.get("id_Tipo_Usuario"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "nome", "Maria",
            "email", "maria@email.com",
            "senha", "abc123",
            "data_Criacao", "2023-12-10",
            "id_Tipo_Usuario", "5"
        );

        User user = new User("", "", "", null, null);
        user.populate(data);

        assertEquals("Maria", user.getName());
        assertEquals("maria@email.com", user.getEmail());
        assertEquals("abc123", user.getPassword());
        assertEquals(LocalDate.of(2023, 12, 10), user.getCreationDate());

        assertNotNull(user.getUserType());
        assertEquals(5, user.getUserType().getId());

    }

    @Test
    public void testConstructorWithId() {
        User user = new User(1);
        assertNotNull(user);
    }
}
