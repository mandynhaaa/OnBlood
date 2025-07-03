package Main;

import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Donor {
    private String cpf;
    private LocalDate birthDate;
    private String bloodType;

    public Donor(String cpf, LocalDate birthDate, String bloodType) {
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.bloodType = bloodType;
    }
    
    public Donor() {}

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    
    public Document toDoc() {
        return new Document("cpf", this.cpf)
                .append("data_nascimento", this.birthDate)
                .append("tipo_sanguineo", this.bloodType);
    }

    public static Donor fromDoc(Document doc) {
        if (doc == null) return null;
        Donor donor = new Donor();
        donor.cpf = doc.getString("cpf");
        
        Object birthDateObj = doc.get("data_nascimento");
        if (birthDateObj instanceof Date) {
            donor.birthDate = ((Date) birthDateObj).toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDate();
        }

        donor.bloodType = doc.getString("tipo_sanguineo");
        return donor;
    }
}