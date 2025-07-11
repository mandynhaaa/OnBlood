package Main;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Address {
    private ObjectId id; 
    private String description;
    private String cep;
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private int number;
    private String complement;

    public Address() {
        this.id = new ObjectId();
    }
    
    public ObjectId getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getCountry() { return country; }
    public void setCountry(String c) { this.country = c; }
    public String getState() { return state; }
    public void setState(String s) { this.state = s; }
    public String getCity() { return city; }
    public void setCity(String c) { this.city = c; }
    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String n) { this.neighborhood = n; }
    public String getStreet() { return street; }
    public void setStreet(String s) { this.street = s; }
    public int getNumber() { return number; }
    public void setNumber(int num) { this.number = num; }
    public String getComplement() { return complement; }
    public void setComplement(String c) { this.complement = c; }

    public Document toDoc() {
        return new Document("id_endereco", this.id)
                .append("descricao", this.description)
                .append("cep", this.cep)
                .append("pais", this.country)
                .append("estado", this.state)
                .append("cidade", this.city)
                .append("bairro", this.neighborhood)
                .append("rua", this.street)
                .append("numero", this.number)
                .append("complemento", this.complement);
    }

    public static Address fromDoc(Document doc) {
        if (doc == null) return null;
        Address address = new Address();
        address.id = doc.getObjectId("id_endereco");
        address.description = doc.getString("descricao");
        address.cep = doc.getString("cep");
        address.country = doc.getString("pais");
        address.state = doc.getString("estado");
        address.city = doc.getString("cidade");
        address.neighborhood = doc.getString("bairro");
        address.street = doc.getString("rua");
        address.number = doc.getInteger("numero", 0);
        address.complement = doc.getString("complemento");
        return address;
    }
}