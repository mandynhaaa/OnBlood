package Main;

import javax.swing.JOptionPane;
import Main.ReportDonation;
import Main.ReportBloodStock;
import Main.ReportDonor;

public class Main {
	
	public static void main(String[] args) {
		
		Address address = new Address(
	            "Minha Casa", 
	            12345678, 
	            "Brasil", 
	            "SC", 
	            "Florianópolis", 
	            "Centro", 
	            "Rua das Flores", 
	            100, 
	            "Apartamento 202"
	        );
		
		address.create();
		
		boolean executando = true;

        while (executando) {
            String[] opcoes = {
                "1 - Doações por Doador em Período",
                "2 - Estoque por Hemocentro",
                "3 - Maior Doador em Período",
                "0 - Sair"
            };

            String escolha = (String) JOptionPane.showInputDialog(
                null,
                "Escolha um relatório para gerar:",
                "Menu de Relatórios",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );

            if (escolha == null || escolha.startsWith("0")) {
                executando = false;
            } else if (escolha.startsWith("1")) {
            	ReportDonation.create();
            } else if (escolha.startsWith("2")) {
            	ReportBloodStock.create();
            } else if (escolha.startsWith("3")) {
            	ReportDonor.create();
            }
        }

        JOptionPane.showMessageDialog(null, "Sistema encerrado.");

	}
}