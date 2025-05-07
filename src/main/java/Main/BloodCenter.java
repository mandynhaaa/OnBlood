package Main;

import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class BloodCenter extends BaseModel {
	private String cnpj;
	private String companyName;
	private User user;
	
	public BloodCenter(String cnpj, String companyName, User user)
	{
		super("hemocentro");
		this.cnpj = cnpj;
		this.companyName = companyName;
		this.user = user;
	}
	
    public BloodCenter(int id) {
    	super("hemocentro", id);
        this.read();
    }

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
   
	@Override
	public void populate(Map<String, String> data) {
        this.cnpj = data.getOrDefault("cnpj", null);
        this.companyName = data.getOrDefault("razao_Social", null);
        this.user = new User(data.get("id_Usuario") != null ? Integer.parseInt(data.get("id_Usuario")) : 0);
    }
    
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("cnpj", this.cnpj);
        data.put("razao_Social", this.companyName);
        data.put("id_Usuario", String.valueOf(this.user.getId()));
        return data;
    }
}
