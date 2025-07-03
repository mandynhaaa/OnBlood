package Main;

import Standard.BaseModel;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserType extends BaseModel {
    private String description;

    public UserType(String description) {
        super("tipos_usuario"); 
        this.description = description;
    }

    public UserType(ObjectId id) {
        super("tipos_usuario", id);
    }
    
    public UserType(int legacyId) {
        super("tipos_usuario");
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