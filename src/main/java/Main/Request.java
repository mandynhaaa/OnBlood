package Main;

import Standard.BaseModel;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Request extends BaseModel {
    private String status;
    private float volume;
    private LocalDateTime datetime;
    private ObjectId bloodCenterId; 
    private String bloodType;      

    public Request(String status, float volume, LocalDateTime datetime, ObjectId bloodCenterId, String bloodType) {
        super("solicitacoes");
        this.status = status;
        this.volume = volume;
        this.datetime = datetime;
        this.bloodCenterId = bloodCenterId;
        this.bloodType = bloodType;
    }

    public Request(ObjectId id) {
        super("solicitacoes", id);
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = volume; }
    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }
    public ObjectId getBloodCenterId() { return bloodCenterId; }
    public void setBloodCenterId(ObjectId id) { this.bloodCenterId = id; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String type) { this.bloodType = type; }


    @Override
    public Document toDoc() {
        return new Document("status", this.status)
                .append("volume", this.volume)
                .append("data_hora", this.datetime)
                .append("id_hemocentro", this.bloodCenterId)
                .append("tipo_sanguineo", this.bloodType);
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
        this.bloodCenterId = doc.getObjectId("id_hemocentro");
        this.bloodType = doc.getString("tipo_sanguineo");
    }
}