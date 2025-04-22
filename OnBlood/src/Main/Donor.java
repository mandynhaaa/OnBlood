package Main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Standard.BaseModel;

public class Donor extends BaseModel {
	private int cpf;
	private LocalDate birthDate;
	private User user;
	private BloodType bloodType;
	
	public Donor(int cpf, LocalDate birthDate, User user, BloodType bloodType)
	{
		super("doador");
		this.cpf = cpf;
		this.birthDate = birthDate;
		this.user = user;
		this.bloodType = bloodType;
	}
	
	public Donor(int id)
	{
		super("doador", id);
		this.read();
	}
	
	public int getCpf() {
		return cpf;
	}

	public void setCpf(int cpf) {
		this.cpf = cpf;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}

	@Override
	public void populate(Map<String, String> data) {
        this.cpf = Integer.parseInt(data.getOrDefault("cpf", null));
        this.birthDate = LocalDate.parse(data.getOrDefault("data_Nascimento", null));
        this.user = new User(Integer.parseInt(data.getOrDefault("id_Usuario", null)));
        this.bloodType = new BloodType(Integer.parseInt(data.getOrDefault("id_Tipo_Sanguineo", null)));
    }
    
	@Override
	public Map<String, String> toMap() {
        Map<String, String> data = new HashMap<>();
        data.put("cpf", String.valueOf(this.cpf));
        data.put("data_Nascimento", String.valueOf(this.birthDate));
        data.put("id_Usuario", String.valueOf(this.user.getId()));
        data.put("id_Tipo_Sanguineo", String.valueOf(this.bloodType.getId()));
        return data;
    }
}
