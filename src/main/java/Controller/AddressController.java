package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Main.Address;

public class AddressController implements ActionListener {
	private JTextField tf_description;
	private JTextField tf_country;
	private JTextField tf_state;
	private JTextField tf_city;
	private JTextField tf_neighborhood;
	private JTextField tf_street;
	private JTextField tf_number;
	private JTextField tf_complement;
	private JTextField tf_cep;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Register")) {
			executeRegister();
		}
	}
	
	public void executeRegister() {
		Address address = new Address(tf_description.getText(), tf_cep.getText(), tf_country.getText(), tf_state.getText(), tf_city.getText(), tf_neighborhood.getText(), tf_street.getText(), Integer.parseInt(tf_number.getText()), tf_complement.getText());
		if (address.create() > 0) {
			JOptionPane.showMessageDialog(null, "Endereço inserido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro na inserção do endereço.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

}
