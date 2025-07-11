package Main;

import Standard.BaseModel;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
        
        Object dateObj = doc.get("data_atualizacao");
        if (dateObj instanceof Date) {
            this.lastUpdateDate = ((Date) dateObj).toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
        }

        Object volumeObj = doc.get("volume");
        if (volumeObj instanceof Number) {
            this.volume = ((Number) volumeObj).floatValue();
        } else {
            this.volume = 0.0f; 
        }

        this.bloodCenterId = doc.getObjectId("id_hemocentro");
        this.bloodType = doc.getString("tipo_sanguineo");
    }
}