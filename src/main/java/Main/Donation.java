package Main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Donation extends BaseModel {
	private String status;
	private float volume;
	private LocalDateTime datetime;
	private Donor donor;
	private BloodCenter bloodCenter;
	
	public Donation(String status, float volume, LocalDateTime datetime, Donor donor, BloodCenter bloodCenter)
	{
		super("doacao");
		this.status = status;
		this.volume = volume;
		this.datetime = datetime;
		this.donor = donor;
		this.bloodCenter = bloodCenter;
	}
	
	public Donation(int id)
	{
		super("doacao", id);
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

	public Donor getDonor() {
		return donor;
	}

	public void setDonor(Donor donor) {
		this.donor = donor;
	}

	public BloodCenter getBloodCenter() {
		return bloodCenter;
	}

	public void setBloodCenter(BloodCenter bloodCenter) {
		this.bloodCenter = bloodCenter;
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
		
        this.donor = new Donor(data.get("id_Doador") != null ? Integer.parseInt(data.get("id_Doador")) : 0);
        this.bloodCenter = new BloodCenter(data.get("id_Hemocentro") != null ? Integer.parseInt(data.get("id_Hemocentro")) : 0);
    }
    
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("status", this.status);
        data.put("volume", String.valueOf(this.volume));
        data.put("data_Hora", this.datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put("id_Doador", String.valueOf(this.donor.getId()));
        data.put("id_Hemocentro", String.valueOf(this.bloodCenter.getId()));
        return data;
    }
}
