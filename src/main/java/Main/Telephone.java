package Main;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Telephone {
    private ObjectId id;
    private String description;
    private String ddd;
    private String number;
    
    public Telephone() {
        this.id = new ObjectId();
    }

    public ObjectId getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getDdd() { return ddd; }
    public void setDdd(String ddd) { this.ddd = ddd; }
    public String getNumber() { return number; }
    public void setNumber(String n) { this.number = n; }
    
    public Document toDoc() {
        return new Document("id_telefone", this.id)
                .append("descricao", this.description)
                .append("ddd", this.ddd)
                .append("numero", this.number);
    }

    public static Telephone fromDoc(Document doc) {
        if (doc == null) return null;
        Telephone phone = new Telephone();
        phone.id = doc.getObjectId("id_telefone");
        phone.description = doc.getString("descricao");
        phone.ddd = doc.getString("ddd");
        phone.number = doc.getString("numero");
        return phone;
    }
}