package Main;

import org.bson.Document;

public class BloodCenter {
    private String cnpj;
    private String companyName;

    public BloodCenter(String cnpj, String companyName) {
        this.cnpj = cnpj;
        this.companyName = companyName;
    }
    
    public BloodCenter() {}

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public Document toDoc() {
        return new Document("cnpj", this.cnpj)
                .append("razao_social", this.companyName);
    }

    public static BloodCenter fromDoc(Document doc) {
        if (doc == null) return null;
        BloodCenter center = new BloodCenter();
        center.cnpj = doc.getString("cnpj");
        center.companyName = doc.getString("razao_social");
        return center;
    }
}