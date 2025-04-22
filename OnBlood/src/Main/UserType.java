package Main;

import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class UserType extends BaseModel {
	private String description;
	
	public UserType (String description)
	{
		super("tipo_Usuario");
		this.description = description;
	}
	
	public UserType (int id)
	{
		super("tipo_Usuario", id);
		this.read();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("descricao", this.description);
        return data;
    }
	    
	@Override
	public void populate(Map<String, String> data) {
        this.description = data.getOrDefault("descricao", null);
    }
}
