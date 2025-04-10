package Main;

public class Address {
	private int id;
	private String description;
	private String cep;
	private String country;
	private String state;
	private String city;
	private String neighborhood;
	private String street;
	private int number;
	private String complement;
	
	public Address (String description, String cep, String country, String state, String city, String neighborhood, String street, int number, String complement)
	{
		this.description = description;
		this.cep = cep;
		this.country = country;
		this.state = state;
		this.city = city;
		this.neighborhood = neighborhood;
		this.street = street;
		this.number = number;
		this.complement = complement;
	}
}
