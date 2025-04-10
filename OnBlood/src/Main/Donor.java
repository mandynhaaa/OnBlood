package Main;

import java.time.LocalDate;

public class Donor {
	private int id;
	private String cpf;
	private LocalDate birthDate;
	private User user;
	private BloodType bloodType;
	
	public Donor(String cpf, LocalDate birthDate, User user, BloodType bloodType)
	{
		this.cpf = cpf;
		this.birthDate = birthDate;
		this.user = user;
		this.bloodType = bloodType;
	}
}
