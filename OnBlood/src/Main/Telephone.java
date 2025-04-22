package Main;

import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Telephone extends BaseModel {
	private String description;
	private int ddd;
	private int number;
	
	public Telephone (String description, int ddd, int number)
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

	public int getDdd() {
		return ddd;
	}

	public void setDdd(int ddd) {
		this.ddd = ddd;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
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
		this.ddd = Integer.parseInt(data.getOrDefault("ddd", null));
		this.number = Integer.parseInt(data.getOrDefault("numero", null));
	}
}
