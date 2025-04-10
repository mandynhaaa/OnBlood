package Main;

import java.time.LocalDate;

public class Request {
	private int id;
	private String status;
	private float volume;
	private LocalDate datetime;
	private BloodCenter bloodCenter;
	private BloodType bloodType;
	
	public Request(String status, float volume, LocalDate datetime, BloodCenter bloodCenter, BloodType bloodType)
	{
		this.status = status;
		this.volume = volume;
		this.datetime = datetime;
		this.bloodCenter = bloodCenter;
		this.bloodType = bloodType;
	}
}
