package Main;

import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import Connection.ConnectionSQL;
import Connection.SQLGenerator;
import Standard.BaseModel;
import Standard.PasswordCrypt;

public class User extends BaseModel {
	private String name;
	private String email;
	private String password;
	private LocalDateTime creationDate;
	private UserType userType;
	
	public User (String name, String email, String password, LocalDateTime creationDate, UserType userType)
	{
		super("usuario");
		this.name = name;
		this.email = email;
		this.password = password;
		this.creationDate = creationDate;
		this.userType = userType;
	}
	
	public User (String name, String password)
	{
		super("usuario");
		this.name = name;
		this.password = password;
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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> data = new HashMap<>();
		data.put("nome", this.name);
		data.put("email", this.email);
		data.put("senha", this.password);
		data.put("data_Criacao", this.creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		data.put("id_Tipo_Usuario", String.valueOf(this.userType.getId()));
		return data;
	}

	@Override
	public void populate(Map<String, String> data) {
		this.name = data.getOrDefault("nome", null);
		this.email = data.getOrDefault("email", null);
		this.password = data.getOrDefault("senha", null);
		
		String rawDate = data.getOrDefault("data_Criacao", null);
		if (rawDate != null) {
		    try {
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		        this.creationDate = LocalDateTime.parse(rawDate, formatter);
		    } catch (Exception e) {
		        this.creationDate = LocalDateTime.parse(rawDate);
		    }
		}
		
		this.userType = new UserType(data.get("id_Tipo_Usuario") != null ? Integer.parseInt(data.get("id_Tipo_Usuario")) : 0);
	}
	
	public boolean login() {
	    User userData = searchByName();

	    if (userData == null) {
	        return false;
	    }

	    return PasswordCrypt.verify(this.password, userData.getPassword());
	}

	public User searchByName() {
	    if (this.name == null || this.name.isEmpty()) {
	        return null;
	    }

	    Map<String, String> filters = new HashMap<>();
	    filters.put("nome", this.name);

	    String[][] result = SQLGenerator.selectSQL("usuario", null, filters);

	    if (result == null || result.length < 2) {
	        return null;
	    }

	    String[] columns = result[0];
	    String[] values = result[1];

	    Map<String, String> data = new HashMap<>();
	    for (int i = 0; i < columns.length; i++) {
	    	if (columns[i] == "id_Usuario") {
	    		data.put("id", values[i]);
	    	} else {	    		
	    		data.put(columns[i], values[i]);
	    	}
	    }

	    User user = new User(0);
	    user.populate(data);
	    return user;
	}
	
}
