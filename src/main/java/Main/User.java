package Main;

import Standard.BaseModel;
import Standard.PasswordCrypt;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;
import java.util.stream.Collectors;

public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    private LocalDateTime creationDate;
    private String userType;
    private Donor donorInfo;
    private BloodCenter bloodCenterInfo;
    private List<Address> addresses = new ArrayList<>();
    private List<Telephone> telephones = new ArrayList<>();

    public User(String name, String password) {
        super("usuarios");
        this.name = name;
        this.password = password;
    }
    
    public User() {
        super("usuarios");
    }

    public User(ObjectId id) {
        super("usuarios", id);
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = PasswordCrypt.hash(password); }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime d) { this.creationDate = d; }
    public String getUserType() { return userType; }
    public void setUserType(String type) { this.userType = type; }
    public Donor getDonorInfo() { return donorInfo; }
    public void setDonorInfo(Donor info) { this.donorInfo = info; }
    public BloodCenter getBloodCenterInfo() { return bloodCenterInfo; }
    public void setBloodCenterInfo(BloodCenter info) { this.bloodCenterInfo = info; }
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
    public List<Telephone> getTelephones() { return telephones; }
    public void setTelephones(List<Telephone> telephones) { this.telephones = telephones; }

    @Override
    public Document toDoc() {
        Document doc = new Document("nome", this.name)
                .append("email", this.email)
                .append("senha", this.password)
                .append("data_criacao", this.creationDate)
                .append("tipo_usuario", this.userType);

        if (this.donorInfo != null) {
            doc.append("doador_info", this.donorInfo.toDoc());
        }
        if (this.bloodCenterInfo != null) {
            doc.append("hemocentro_info", this.bloodCenterInfo.toDoc());
        }
        if (this.addresses != null && !this.addresses.isEmpty()) {
            doc.append("enderecos", this.addresses.stream().map(Address::toDoc).collect(Collectors.toList()));
        }
        if (this.telephones != null && !this.telephones.isEmpty()) {
            doc.append("telefones", this.telephones.stream().map(Telephone::toDoc).collect(Collectors.toList()));
        }
        return doc;
    }

    @Override
    public void populateFromDoc(Document doc) {
        if (doc == null) return;
        this.id = doc.getObjectId("_id");
        this.name = doc.getString("nome");
        this.email = doc.getString("email");
        this.password = doc.getString("senha");
        
        Object creationDateObj = doc.get("data_criacao");
        if (creationDateObj instanceof Date) {
            this.creationDate = ((Date) creationDateObj).toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
        }

        this.userType = doc.getString("tipo_usuario");
        
        this.donorInfo = Donor.fromDoc(doc.get("doador_info", Document.class));
        
        this.bloodCenterInfo = BloodCenter.fromDoc(doc.get("hemocentro_info", Document.class));
        
        List<Document> addressDocs = doc.getList("enderecos", Document.class);
        if (addressDocs != null) {
            this.addresses = addressDocs.stream().map(Address::fromDoc).collect(Collectors.toList());
        }

        List<Document> phoneDocs = doc.getList("telefones", Document.class);
        if (phoneDocs != null) {
            this.telephones = phoneDocs.stream().map(Telephone::fromDoc).collect(Collectors.toList());
        }
    }
    
    public boolean login() {
        User userData = searchByName();
        if (userData == null) {
            return false;
        }
        return PasswordCrypt.verify(this.password, userData.getPassword());
    }

    public User searchByName() {
        if (this.name == null || this.name.isEmpty()) return null;
        Document doc = getCollection().find(Filters.eq("nome", this.name)).first();
        if (doc != null) {
            User user = new User();
            user.populateFromDoc(doc);
            return user;
        }
        return null;
    }
}