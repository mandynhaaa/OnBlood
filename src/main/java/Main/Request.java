package Main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Request extends BaseModel {
	private String status;
	private float volume;
	private LocalDateTime datetime;
	private BloodCenter bloodCenter;
	private BloodType bloodType;
	
	public Request(String status, float volume, LocalDateTime datetime, BloodCenter bloodCenter, BloodType bloodType)
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

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
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

		String rawDate = data.getOrDefault("data_Hora", null);
		if (rawDate != null) {
		    try {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        this.datetime = LocalDateTime.parse(rawDate, formatter);
		    } catch (Exception e) {
		        this.datetime = LocalDateTime.parse(rawDate);
		    }
		}
		
        this.bloodCenter = new BloodCenter(data.get("id_Hemocentro") != null ? Integer.parseInt(data.get("id_Hemocentro")) : 0);
        this.bloodType = new BloodType(data.get("id_Tipo_Sanguineo") != null ? Integer.parseInt(data.get("id_Tipo_Sanguineo")) : 0);
    }
    
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("status", this.status);
        data.put("volume", String.valueOf(this.volume));
        data.put("data_Hora", this.datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put("id_Hemocentro", String.valueOf(this.bloodCenter.getId()));
        data.put("id_Tipo_Sanguineo", String.valueOf(this.bloodType.getId()));
        return data;
    }
}