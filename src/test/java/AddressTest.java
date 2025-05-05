import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import Main.Address;
import Main.Telephone;

import java.util.Map;

class AddressTest {

    @Test
    public void testCreateAddressWithData() {
        Address address = new Address(
            "Minha Casa", 
            "12345678", 
            "Brasil", 
            "SC", 
            "Florianópolis", 
            "Centro", 
            "Rua das Flores", 
            100, 
            "Apartamento 202"
        );

        assertEquals("Minha Casa", address.getDescription());
        assertEquals("12345678", address.getCep());
        assertEquals("Brasil", address.getCountry());
        assertEquals("SC", address.getState());
        assertEquals("Florianópolis", address.getCity());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("Rua das Flores", address.getStreet());
        assertEquals(100, address.getNumber());
        assertEquals("Apartamento 202", address.getComplement());
    }

    @Test
    public void testToMap() {
        Address address = new Address(
            "Minha Casa", 
            "12345678", 
            "Brasil", 
            "SC", 
            "Florianópolis", 
            "Centro", 
            "Rua das Flores", 
            100, 
            "Apartamento 202"
        );

        Map<String, String> data = address.toMap();

        assertEquals("Minha Casa", data.get("descricao"));
        assertEquals("12345678", data.get("cep"));
        assertEquals("Brasil", data.get("pais"));
        assertEquals("SC", data.get("estado"));
        assertEquals("Florianópolis", data.get("cidade"));
        assertEquals("Centro", data.get("bairro"));
        assertEquals("Rua das Flores", data.get("rua"));
        assertEquals("100", data.get("numero"));
        assertEquals("Apartamento 202", data.get("complemento"));
    }
    
    @Test
    public void testPopulate() {
        Map<String, String> data = Map.of(
            "descricao", "Minha Casa",
            "cep", "12345678",
            "pais", "Brasil",
            "estado", "SC",
            "cidade", "Florianópolis",
            "bairro", "Centro",
            "rua", "Rua das Flores",
            "numero", "100",
            "complemento", "Apartamento 202"
        );

        Address address = new Address(
            "", "", "", "", "", "", "", 0, ""
        );

        address.populate(data);

        assertEquals("Minha Casa", address.getDescription());
        assertEquals("12345678", address.getCep());
        assertEquals("Brasil", address.getCountry());
        assertEquals("SC", address.getState());
        assertEquals("Florianópolis", address.getCity());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("Rua das Flores", address.getStreet());
        assertEquals(100, address.getNumber());
        assertEquals("Apartamento 202", address.getComplement());
    }
    
    @Test
    public void testPopulateWithMissingFields() {
        Map<String, String> partialData = Map.of(
            "descricao", "Endereço Incompleto",
            "cep", "99999999"
        );

        Address address = new Address("", "", "", "", "", "", "", 0, "");
        address.populate(partialData);

        assertEquals("Endereço Incompleto", address.getDescription());
        assertEquals("99999999", address.getCep());
        assertNull(address.getCountry());
        assertNull(address.getState());
        assertNull(address.getCity());
        assertNull(address.getNeighborhood());
        assertNull(address.getStreet());
        assertEquals(0, address.getNumber());
        assertNull(address.getComplement());
    }

    @Test
    public void testRoundTripMapConversion() {
        Address original = new Address(
            "Casa do João", "11111111", "Brasil", "SP", "São Paulo",
            "Bela Vista", "Rua A", 42, "Casa 1"
        );

        Map<String, String> map = original.toMap();
        Address copy = new Address("", "", "", "", "", "", "", 0, "");
        copy.populate(map);

        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getCep(), copy.getCep());
        assertEquals(original.getCountry(), copy.getCountry());
        assertEquals(original.getState(), copy.getState());
        assertEquals(original.getCity(), copy.getCity());
        assertEquals(original.getNeighborhood(), copy.getNeighborhood());
        assertEquals(original.getStreet(), copy.getStreet());
        assertEquals(original.getNumber(), copy.getNumber());
        assertEquals(original.getComplement(), copy.getComplement());
    }

    @Test
    public void testExtremeValues() {
        Address address = new Address(
            "Teste", "", "Brasil", "RJ", "Rio", "Copacabana",
            "Rua X", Integer.MAX_VALUE, null
        );

        assertEquals("", address.getCep());
        assertEquals(Integer.MAX_VALUE, address.getNumber());
        assertNull(address.getComplement());
    }
    
    @Test
    public void testConstructorWithId() {
        Address address = new Address(1);
        assertNotNull(address);
    }
}
