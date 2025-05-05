package Main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Request extends BaseModel {
	private String status;
	private float volume;
	private LocalDate datetime;
	private BloodCenter bloodCenter;
	private BloodType bloodType;
	
	public Request(String status, float volume, LocalDate datetime, BloodCenter bloodCenter, BloodType bloodType)
	{
		super("solicitacao");
		this.status = status;
		this.volume = volume;
		this.datetime = datetime;
		this.bloodCenter = bloodCenter;
		this.bloodType = bloodType;
	}
	
	public Request(int id)
	{
		super("solicitacao", id);
		this.read();
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public LocalDate getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDate datetime) {
		this.datetime = datetime;
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
		this.status = data.getOrDefault("status", null);
		this.volume = data.get("volume") != null ? Float.parseFloat(data.get("volume")) : 0f;
        this.datetime = LocalDate.parse(data.getOrDefault("data_Hora", null));
        this.bloodCenter = new BloodCenter(data.get("id_Hemocentro") != null ? Integer.parseInt(data.get("id_Hemocentro")) : 0);
        this.bloodType = new BloodType(data.get("id_Tipo_Sanguineo") != null ? Integer.parseInt(data.get("id_Tipo_Sanguineo")) : 0);
    }
    
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("status", this.status);
        data.put("volume", String.valueOf(this.volume));
        data.put("data_Hora", String.valueOf(this.datetime));
        data.put("id_Hemocentro", String.valueOf(this.bloodCenter.getId()));
        data.put("id_Tipo_Sanguineo", String.valueOf(this.bloodType.getId()));
        return data;
    }
}