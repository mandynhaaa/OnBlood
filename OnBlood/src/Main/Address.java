package Main;

import java.util.HashMap;
import java.util.Map;

import Connection.SQLGenerator;
import Standard.CRUD;

public class Address implements CRUD {
    private static final String TABLE = "endereco";
    private int id;
    private String description;
    private String cep;
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private int number;
    private String complement;

    public Address(String description, String cep, String country, String state, String city, String neighborhood, String street, int number, String complement) {
        this.description = description;
        this.cep = cep;
        this.country = country;
        this.state = state;
        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
        this.complement = complement;
    }

    public Address(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public int create() {
        int idAddress = SQLGenerator.insertSQL(TABLE, toMap());
        this.id = idAddress;
        return idAddress;
    }
   
    public void read() {
        if (this.id <= 0) {
            return;
        }

        Map<String, String> filters = new HashMap<>();
        filters.put("id_Endereco", String.valueOf(this.id));

        String[][] result = SQLGenerator.selectSQL(TABLE, null, filters);

        if (result.length < 2) {
            return;
        }

        String[] columns = result[0];
        String[] values = result[1];

        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            data.put(columns[i], values[i]);
        }

        this.description = data.getOrDefault("descricao", null);
        this.cep = data.getOrDefault("cep", null);
        this.country = data.getOrDefault("pais", null);
        this.state = data.getOrDefault("estado", null);
        this.city = data.getOrDefault("cidade", null);
        this.neighborhood = data.getOrDefault("bairro", null);
        this.street = data.getOrDefault("rua", null);

        String numeroStr = data.get("numero");
        if (numeroStr != null && !numeroStr.isEmpty()) {
            try {
                this.number = Integer.parseInt(numeroStr);
            } catch (NumberFormatException e) {
                this.number = 0;
            }
        }

        this.complement = data.getOrDefault("complemento", null);
    }

    public void update() {
        SQLGenerator.updateSQL(TABLE, this.id, toMap());
    }

    public void delete() {
        SQLGenerator.deleteSQL(TABLE, this.id);
    }
    
    private Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("descricao", this.description);
        data.put("cep", this.cep);
        data.put("pais", this.country);
        data.put("estado", this.state);
        data.put("cidade", this.city);
        data.put("bairro", this.neighborhood);
        data.put("rua", this.street);
        data.put("numero", String.valueOf(this.number));
        data.put("complemento", this.complement);
        return data;
    }
}