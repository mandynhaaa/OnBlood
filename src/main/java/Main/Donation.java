package Main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Donation extends BaseModel {
	private String status;
	private float volume;
	private LocalDate datetime;
	private Donor donor;
	private BloodCenter bloodCenter;
	
	public Donation(String status, float volume, LocalDate datetime, Donor donor, BloodCenter bloodCenter)
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

	public LocalDate getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDate datetime) {
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
        this.datetime = LocalDate.parse(data.getOrDefault("data_Hora", null));
        this.donor = new Donor(data.get("id_Doador") != null ? Integer.parseInt(data.get("id_Doador")) : 0);
        this.bloodCenter = new BloodCenter(data.get("id_Hemocentro") != null ? Integer.parseInt(data.get("id_Hemocentro")) : 0);
    }
    
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("status", this.status);
        data.put("volume", String.valueOf(this.volume));
        data.put("data_Hora", String.valueOf(this.datetime));
        data.put("id_Doador", String.valueOf(this.donor.getId()));
        data.put("id_Hemocentro", String.valueOf(this.bloodCenter.getId()));
        return data;
    }
}
