package Main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Connection.SQLGenerator;
import Standard.BaseModel;
import Standard.PasswordCrypt;

public class User extends BaseModel {
	private String name;
	private String email;
	private String password;
	private LocalDate creationDate;
	private UserType userType;
	private Telephone telephone;
	private Address address;
	
	public User (String name, String email, String password, LocalDate creationDate, UserType userType, Telephone telephone, Address address)
	{
		super("usuario");
		this.name = name;
		this.email = email;
		this.password = password;
		this.creationDate = creationDate;
		this.userType = userType;
		this.telephone = telephone;
		this.address = address;
	}
	
	public User (String name, String password)
	{
		super("usuario");
		this.name = name;
		this.password = PasswordCrypt.hash(password);
	}
	
	public User (int id)
	{
		super("usuario", id);
		this.read();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = PasswordCrypt.hash(password);
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Telephone getTelephone() {
		return telephone;
	}

	public void setTelephone(Telephone telephone) {
		this.telephone = telephone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> data = new HashMap<>();
		data.put("nome", this.name);
		data.put("email", this.email);
		data.put("senha", this.password);
		data.put("id_Tipo_Usuario", String.valueOf(this.userType.getId()));
		data.put("id_Telefone", String.valueOf(this.telephone.getId()));
		data.put("id_Endereco", String.valueOf(this.address.getId()));
		return data;
	}

	@Override
	public void populate(Map<String, String> data) {
		this.name = data.getOrDefault("nome", null);
		this.email = data.getOrDefault("email", null);
		this.password = data.getOrDefault("senha", null);
		this.creationDate = LocalDate.parse(data.getOrDefault("data_Criacao", null));
		this.userType = new UserType(data.get("id_Tipo_Usuario") != null ? Integer.parseInt(data.get("id_Tipo_Usuario")) : 0);
		this.telephone = new Telephone(data.get("id_Telefone") != null ? Integer.parseInt(data.get("id_Telefone")) : 0);
		this.address = new Address(data.get("id_Endereco") != null ? Integer.parseInt(data.get("id_Endereco")) : 0);
	}
	
	public boolean login() {
	    User userData = searchByName(this.name);

	    if (userData == null) {
	        return false;
	    }

	    return PasswordCrypt.verify(this.password, userData.getPassword());
	}

	private User searchByName(String name) {
	    if (name == null || name.isEmpty()) {
	        return null;
	    }

	    Map<String, String> filters = new HashMap<>();
	    filters.put("nome", name);

	    String[][] result = SQLGenerator.selectSQL("usuario", null, filters);

	    if (result == null || result.length < 2) {
	        return null;
	    }

	    String[] columns = result[0];
	    String[] values = result[1];

	    Map<String, String> data = new HashMap<>();
	    for (int i = 0; i < columns.length; i++) {
	        data.put(columns[i], values[i]);
	    }

	    User user = new User(0);
	    user.populate(data);
	    return user;
	}

}
