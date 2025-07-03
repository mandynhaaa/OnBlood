package Main;

import Standard.BaseModel;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class BloodStock extends BaseModel {
    private LocalDateTime lastUpdateDate;
    private float volume;
    private ObjectId bloodCenterId; 
    private String bloodType;       

    public BloodStock(LocalDateTime lastUpdateDate, float volume, ObjectId bloodCenterId, String bloodType) {
        super("estoque");
        this.lastUpdateDate = lastUpdateDate;
        this.volume = volume;
        this.bloodCenterId = bloodCenterId;
        this.bloodType = bloodType;
    }
    
    public BloodStock(ObjectId id) {
        super("estoque", id);
    }
    
    public BloodStock() {
        super("estoque");
    }

    public LocalDateTime getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(LocalDateTime d) { this.lastUpdateDate = d; }
    public float getVolume() { return volume; }
    public void setVolume(float v) { this.volume = v; }
    public ObjectId getBloodCenterId() { return bloodCenterId; }
    public void setBloodCenterId(ObjectId id) { this.bloodCenterId = id; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String type) { this.bloodType = type; }

    @Override
    public Document toDoc() {
        return new Document("data_atualizacao", this.lastUpdateDate)
                .append("volume", this.volume)
                .append("id_hemocentro", this.bloodCenterId)
                .append("tipo_sanguineo", this.bloodType);
    }

    @Override
    public void populateFromDoc(Document doc) {
        if (doc == null) return;
        this.id = doc.getObjectId("_id");
        Date lastUpdateDateFromDb = doc.get("data_atualizacao", Date.class);
        if (lastUpdateDateFromDb != null) {
            this.lastUpdateDate = lastUpdateDateFromDb.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        this.volume = doc.getDouble("volume").floatValue();
        this.bloodCenterId = doc.getObjectId("id_hemocentro");
        this.bloodType = doc.getString("tipo_sanguineo");
    }
}