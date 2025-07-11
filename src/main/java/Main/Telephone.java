package Main;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.result.InsertOneResult;

import Standard.BaseModel;

public class Telephone extends BaseModel {
    private ObjectId id;
    private String description;
    private String ddd;
    private String number;
    
    public Telephone(ObjectId id) {
    	super("telefones", id);
        this.id = new ObjectId();
    }
    
    public Telephone() {
        super("telefones");
    }

    public ObjectId getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getDdd() { return ddd; }
    public void setDdd(String ddd) { this.ddd = ddd; }
    public String getNumber() { return number; }
    public void setNumber(String n) { this.number = n; }
    
    public ObjectId create() {
        Document doc = toDoc();
        InsertOneResult result = getCollection().insertOne(doc);
        this.id = result.getInsertedId().asObjectId().getValue();
        return this.id;
    }
    
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

	@Override
	public void populateFromDoc(Document doc) {
        if (doc == null) return;
        this.id = doc.getObjectId("_id");
        this.description = doc.getString("descricao");
        this.ddd = doc.getString("ddd");
        this.number = doc.getString("numero");
	}
}