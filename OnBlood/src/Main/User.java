package Main;

import java.time.LocalDate;

public class User {
	private int id;
	private String name;
	private String email;
	private String password;
	private LocalDate creationDate;
	private UserType userType;
	private Telephone telephone;
	private Address address;
	
	public User (String name, String email, String password, LocalDate creationDate, UserType userType, Telephone telephone, Address address)
	{
		this.name = name;
		this.email = email;
		this.password = password;
		this.creationDate = creationDate;
		this.userType = userType;
		this.telephone = telephone;
		this.address = address;
	}
}
