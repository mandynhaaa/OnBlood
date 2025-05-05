package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Main.Telephone;

public class TelephoneController implements ActionListener {
	private JTextField tf_telephoneDescription;
	private JTextField tf_ddd;
	private JTextField tf_number;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Register")) {
			executeRegister();
		}
	}
	
	public void executeRegister() {
		Telephone telephone = new Telephone(tf_telephoneDescription.getText(), tf_ddd.getText(), tf_number.getText());
		if (telephone.create() > 0) {
			JOptionPane.showMessageDialog(null, "Telefone inserido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao inserir o telefone", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}	
	
}
