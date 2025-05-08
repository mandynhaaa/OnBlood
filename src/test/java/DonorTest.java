import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;
import Main.Donor;
import Main.User;
import Main.BloodType;

class DonorTest {

    @Test
    public void testCreateDonorWithData() {
        User user = new User(1);
        user.setId(1);

        BloodType type = new BloodType("A+");
        type.setId(2);

        Donor donor = new Donor("123456789", LocalDate.of(1990, 5, 10), user, type);

        assertEquals(123456789, donor.getCpf());
        assertEquals(LocalDate.of(1990, 5, 10), donor.getBirthDate());
        assertEquals(user, donor.getUser());
        assertEquals(type, donor.getBloodType());
    }

    @Test
    public void testToMap() {
        User user = new User(10);
        user.setId(10);

        BloodType type = new BloodType("B-");
        type.setId(20);

        Donor donor = new Donor("987654321", LocalDate.of(1985, 1, 20), user, type);

        Map<String, String> map = donor.toMap();

        assertEquals("987654321", map.get("cpf"));
        assertEquals("1985-01-20", map.get("data_Nascimento"));
        assertEquals("10", map.get("id_Usuario"));
        assertEquals("20", map.get("id_Tipo_Sanguineo"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "cpf", "111222333",
            "data_Nascimento", "2000-07-15",
            "id_Usuario", "5",
            "id_Tipo_Sanguineo", "7"
        );

        Donor donor = new Donor(0);
        donor.populate(data);

        assertEquals(111222333, donor.getCpf());
        assertEquals(LocalDate.of(2000, 7, 15), donor.getBirthDate());
        assertEquals(5, donor.getUser().getId());
        assertEquals(7, donor.getBloodType().getId());
    }

    @Test
    public void testConstructorWithId() {
        Donor donor = new Donor(1);
        assertNotNull(donor);
    }

    @Test
    public void testRoundTripMapConversion() {
        User user = new User(3);
        user.setId(3);

        BloodType type = new BloodType("AB+");
        type.setId(4);

        Donor original = new Donor("222333444", LocalDate.of(1999, 12, 31), user, type);
        Map<String, String> map = original.toMap();

        Donor copy = new Donor(0);
        copy.populate(map);

        assertEquals(original.getCpf(), copy.getCpf());
        assertEquals(original.getBirthDate(), copy.getBirthDate());
        assertEquals(original.getUser().getId(), copy.getUser().getId());
        assertEquals(original.getBloodType().getId(), copy.getBloodType().getId());
    }
}
