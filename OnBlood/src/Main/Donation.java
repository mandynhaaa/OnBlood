package Main;

import java.time.LocalDate;

public class Donation {
	private int id;
	private String status;
	private float volume;
	private LocalDate datetime;
	private Donor donor;
	private BloodCenter bloodCenter;
	
	public Donation(String status, float volume, LocalDate datetime, Donor donor, BloodCenter bloodCenter)
	{
		this.status = status;
		this.volume = volume;
		this.datetime = datetime;
		this.donor = donor;
		this.bloodCenter = bloodCenter;
	}
}
