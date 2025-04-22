package Main;

import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Address extends BaseModel {
    private String description;
    private int cep;
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private int number;
    private String complement;

    public Address(String description, int cep, String country, String state, String city, String neighborhood, String street, int number, String complement) {
    	super("endereco");
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
    	super("endereco", id);
        this.read();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
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

    @Override
    public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("descricao", this.description);
        data.put("cep", String.valueOf(this.cep));
        data.put("pais", this.country);
        data.put("estado", this.state);
        data.put("cidade", this.city);
        data.put("bairro", this.neighborhood);
        data.put("rua", this.street);
        data.put("numero", String.valueOf(this.number));
        data.put("complemento", this.complement);
        return data;
    }	
    
    @Override
    public void populate(Map<String, String> data) {
        this.description = data.getOrDefault("descricao", null);
        this.cep = Integer.parseInt(data.getOrDefault("cep", null));
        this.country = data.getOrDefault("pais", null);
        this.state = data.getOrDefault("estado", null);
        this.city = data.getOrDefault("cidade", null);
        this.neighborhood = data.getOrDefault("bairro", null);
        this.street = data.getOrDefault("rua", null);
        this.number = Integer.parseInt(data.getOrDefault("numero", null));
        this.complement = data.getOrDefault("complemento", null);
    }
}