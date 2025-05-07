import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Map;
import Main.Donation;
import Main.BloodCenter;
import Main.Donor;
import Main.User;

class DonationTest {

    @Test
    public void testCreateDonationWithData() {
        Donor donor = new Donor(10);
        donor.setId(10);

        BloodCenter center = new BloodCenter(12345678901234L, "Hemocentro A", new User(1));
        center.setId(20);

        Donation donation = new Donation("Confirmada", 8.0f, LocalDateTime.of(2025, 4, 24, 0, 0), donor, center);

        assertEquals("Confirmada", donation.getStatus());
        assertEquals(8.0f, donation.getVolume());
        assertEquals(LocalDateTime.of(2025, 4, 24, 0, 0), donation.getDatetime());
        assertEquals(donor, donation.getDonor());
        assertEquals(center, donation.getBloodCenter());
    }

    @Test
    public void testToMap() {
        Donor donor = new Donor(100);
        donor.setId(100);

        BloodCenter center = new BloodCenter(12345678901234L, "Hemocentro B", new User(1));
        center.setId(200);

        Donation donation = new Donation("Agendada", 5.5f, LocalDateTime.of(2025, 4, 25, 0, 0), donor, center);

        Map<String, String> map = donation.toMap();

        assertEquals("Agendada", map.get("status"));
        assertEquals("5.5", map.get("volume"));
        assertEquals("2025-04-25 00:00:00", map.get("data_Hora"));
        assertEquals("100", map.get("id_Doador"));
        assertEquals("200", map.get("id_Hemocentro"));
    }

    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "status", "Realizada",
            "volume", "4.4",
            "data_Hora", "2025-04-26 00:00:00",
            "id_Doador", "111",
            "id_Hemocentro", "222"
        );

        Donation donation = new Donation(1);
        donation.populate(data);

        assertEquals("Realizada", donation.getStatus());
        assertEquals(4.4f, donation.getVolume());
        assertEquals(LocalDateTime.of(2025, 4, 26, 0, 0), donation.getDatetime());
        assertEquals(111, donation.getDonor().getId());
        assertEquals(222, donation.getBloodCenter().getId());
    }

    @Test
    public void testConstructorWithId() {
        Donation donation = new Donation(1);
        assertNotNull(donation);
    }

    @Test
    public void testRoundTripMapConversion() {
        Donor donor = new Donor(99);
        donor.setId(99);

        BloodCenter center = new BloodCenter(12345678900000L, "Hemocentro Z", new User(1));
        center.setId(88);

        Donation original = new Donation("Cancelada", 9.9f, LocalDateTime.of(2025, 4, 27, 0, 0), donor, center);
        Map<String, String> map = original.toMap();

        Donation copy = new Donation(0);
        copy.populate(map);

        assertEquals(original.getStatus(), copy.getStatus());
        assertEquals(original.getVolume(), copy.getVolume());
        assertEquals(original.getDatetime(), copy.getDatetime());
        assertEquals(original.getDonor().getId(), copy.getDonor().getId());
        assertEquals(original.getBloodCenter().getId(), copy.getBloodCenter().getId());
    }
}
