package Standard;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import Connection.MongoConnection;
import java.util.Map;

public abstract class BaseModel {
    protected String collectionName;
    protected ObjectId id;

    public BaseModel(String collectionName) {
        this.collectionName = collectionName;
    }

    public BaseModel(String collectionName, ObjectId id) {
        this.collectionName = collectionName;
        this.id = id;
        read();
    }
    
    public MongoCollection<Document> getCollection() {
        MongoDatabase db = MongoConnection.getDatabase();
        return db.getCollection(this.collectionName);
    }

    public ObjectId create() {
        Document doc = toDoc();
        InsertOneResult result = getCollection().insertOne(doc);
        this.id = result.getInsertedId().asObjectId().getValue();
        return this.id;
    }

    public void read() {
        if (this.id == null) {
            return;
        }
        Document doc = getCollection().find(Filters.eq("_id", this.id)).first();
        if (doc != null) {
            populateFromDoc(doc);
        }
    }

    public void update() {
        if (this.id == null) return;
        Document doc = toDoc();
        getCollection().updateOne(Filters.eq("_id", this.id), new Document("$set", doc));
    }

    public void delete() {
        if (this.id == null) return;
        getCollection().deleteOne(Filters.eq("_id", this.id));
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public abstract Document toDoc();


    public abstract void populateFromDoc(Document data);
    

    @Deprecated
    public Map<String, String> toMap() {
        throw new UnsupportedOperationException("Use toDoc() for MongoDB operations.");
    }

    @Deprecated
    public void populate(Map<String, String> data) {
        throw new UnsupportedOperationException("Use populateFromDoc(Document) for MongoDB operations.");
    }
}