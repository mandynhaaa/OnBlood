package Main;

import Standard.BaseModel;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Donation extends BaseModel {
    private String status;
    private float volume;
    private LocalDateTime datetime;
    private ObjectId donorId;       
    private ObjectId bloodCenterId; 

    public Donation(String status, float volume, LocalDateTime datetime, ObjectId donorId, ObjectId bloodCenterId) {
        super("doacoes");
        this.status = status;
        this.volume = volume;
        this.datetime = datetime;
        this.donorId = donorId;
        this.bloodCenterId = bloodCenterId;
    }

    public Donation(ObjectId id) {
        super("doacoes", id);
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = volume; }
    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }
    public ObjectId getDonorId() { return donorId; }
    public void setDonorId(ObjectId donorId) { this.donorId = donorId; }
    public ObjectId getBloodCenterId() { return bloodCenterId; }
    public void setBloodCenterId(ObjectId bloodCenterId) { this.bloodCenterId = bloodCenterId; }

    @Override
    public Document toDoc() {
        return new Document("status", this.status)
                .append("volume", this.volume)
                .append("data_hora", this.datetime)
                .append("id_doador", this.donorId)
                .append("id_hemocentro", this.bloodCenterId);
    }

    @Override
    public void populateFromDoc(Document doc) {
        if (doc == null) return;
        this.id = doc.getObjectId("_id");
        this.status = doc.getString("status");
        this.volume = doc.getDouble("volume").floatValue();
        Date datetimeFromDb = doc.get("data_hora", Date.class);
        if (datetimeFromDb != null) {
            this.datetime = datetimeFromDb.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        this.donorId = doc.getObjectId("id_doador");
        this.bloodCenterId = doc.getObjectId("id_hemocentro");
    }
}