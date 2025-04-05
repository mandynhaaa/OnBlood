package Main;

import java.time.LocalDate;

public class BloodStock {
	private int id;
	private LocalDate lastUpdateData;
	private float volume;
	private LocalDate datetime;
	private BloodCenter bloodCenter;
	private BloodType bloodType;
	
	public BloodStock(LocalDate lastUpdateData, float volume, LocalDate datetime, BloodCenter bloodCenter, BloodType bloodType)
	{
		this.lastUpdateData = lastUpdateData;
		this.volume = volume;
		this.datetime = datetime;
		this.bloodCenter = bloodCenter;
		this.bloodType = bloodType;
	}
}
