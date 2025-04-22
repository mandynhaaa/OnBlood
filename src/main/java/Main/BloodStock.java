package Main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class BloodStock extends BaseModel {
	private LocalDate lastUpdateData;
	private float volume;
	private BloodCenter bloodCenter;
	private BloodType bloodType;
	
	public BloodStock(LocalDate lastUpdateData, float volume, BloodCenter bloodCenter, BloodType bloodType)
	{
		super("estoque");
		this.lastUpdateData = lastUpdateData;
		this.volume = volume;
		this.bloodCenter = bloodCenter;
		this.bloodType = bloodType;
	}
	
	public BloodStock(int id)
	{
		super("estoque", id);
		this.read();
	}

	public LocalDate getLastUpdateData() {
		return lastUpdateData;
	}

	public void setLastUpdateData(LocalDate lastUpdateData) {
		this.lastUpdateData = lastUpdateData;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public BloodCenter getBloodCenter() {
		return bloodCenter;
	}

	public void setBloodCenter(BloodCenter bloodCenter) {
		this.bloodCenter = bloodCenter;
	}

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}
	
	@Override
	public void populate(Map<String, String> data) {
        this.lastUpdateData = LocalDate.parse(data.getOrDefault("data_Atualizacao", null));
        this.volume = Float.parseFloat(data.getOrDefault("volume", null));
        this.bloodCenter = new BloodCenter(Integer.parseInt(data.getOrDefault("id_Hemocentro", null)));
        this.bloodType = new BloodType(Integer.parseInt(data.getOrDefault("id_TipoSanguineo", null)));
    }
    
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("data_Atualizacao", String.valueOf(this.lastUpdateData));
        data.put("volume", String.valueOf(this.volume));
        data.put("id_Hemocentro", String.valueOf(this.bloodCenter.getId()));
        data.put("id_TipoSanguineo", String.valueOf(this.bloodType.getId()));
        return data;
    }
}

