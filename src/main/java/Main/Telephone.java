package Main;

import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Telephone extends BaseModel {
	private String description;
	private String ddd;
	private String number;
	
	public Telephone (String description, String ddd, String number)
	{
		super("telefone");
		this.description = description;
		this.ddd = ddd;
		this.number = number;
	}
	
	public Telephone (int id)
	{
		super("telefone", id);
		this.read();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> data = new HashMap<>();
		data.put("descricao", this.description);
		data.put("ddd", String.valueOf(this.ddd));
		data.put("numero", String.valueOf(this.number));
		return data;
	}
    
	@Override
	public void populate(Map<String, String> data) {
		this.description = data.getOrDefault("descricao", null);
		this.ddd = data.getOrDefault("ddd", null);
		this.number = data.getOrDefault("numero", null);
	}
}
