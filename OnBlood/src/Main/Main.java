package Main;

public class Main {
	
	public static void main(String[] args) {
		Address address = new Address(
				"casa",
				"89208375",
				"Brasil",
				"Santa Catarina",
				"Joinville",
				"Itaum",
				"Rua dos Astronautas",
				235,
				"complemento"
				);
		
		address.create();
		
		address.setComplement("teste2");
		address.update();
		
		address.read();
	}
}
