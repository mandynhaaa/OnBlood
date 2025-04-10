package Main;

import java.time.LocalDate;

public class BloodCenter {
	private int id;
	private String cnpj;
	private String companyName;
	private User user;
	private BloodType bloodType;
	
	public BloodCenter(String cnpj, String companyName, User user, BloodType bloodType)
	{
		this.cnpj = cnpj;
		this.companyName = companyName;
		this.user = user;
		this.bloodType = bloodType;
	}
}
