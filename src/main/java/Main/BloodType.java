package Main;

import Standard.BaseModel;
import org.bson.Document;
import org.bson.types.ObjectId;

public class BloodType extends BaseModel {
    private String description;

    public BloodType(String description) {
        super("tipos_sanguineo"); 
        this.description = description;
    }

    public BloodType(ObjectId id) {
        super("tipos_sanguineo", id);
    }
    
    public BloodType(int legacyId) {
        super("tipos_sanguineo");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Document toDoc() {
        Document doc = new Document();
        doc.append("descricao", this.description);
        return doc;
    }

    @Override
    public void populateFromDoc(Document doc) {
        if (doc == null) return;
        this.id = doc.getObjectId("_id");
        this.description = doc.getString("descricao");
    }
}