package View.Donation;

import javax.swing.*;

import Controller.DonationController;
import java.awt.*;
import java.util.List;

public class ManagerDonation extends JFrame {
	private JList<String> listDoacoes;
    private DefaultListModel<String> listModel;
    private JButton btnEditar, btnExcluir, btnNova, btnAtualizar;
    private DonationController controller;
    private int idUsuario;

    public ManagerDonation(int idUser) {
    	this.idUsuario = idUser;
        this.controller = new DonationController(0, idUsuario, 0, null, null, null, null);
        
        setTitle("Gerenciar Doações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listDoacoes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listDoacoes);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnNova = new JButton("Nova Doação");
        btnAtualizar = new JButton("Atualizar");
        
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNova);
        panelBotoes.add(btnEditar);      
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        carregarDoacoes();

        btnEditar.addActionListener(e -> {
            String selecionado = listDoacoes.getSelectedValue();
            if (selecionado != null) {
                int id = extrairId(selecionado);
                EditDonation editor = new EditDonation(id);
                editor.setVisible(true);
            }
        });

        btnExcluir.addActionListener(e -> excluirDoacao());
        
        btnNova.addActionListener(e -> {
            RegisterDonation telaCadastro = new RegisterDonation(idUsuario);
            telaCadastro.setVisible(true);
        });
        
        btnAtualizar.addActionListener(e -> carregarDoacoes());
    }

    void carregarDoacoes() {
        listModel.clear();
        List<String> doacoes = controller.listarDoacoesHemocentro(idUsuario);
        for (String d : doacoes) {
            listModel.addElement(d);
        }
    }

    private int extrairId(String texto) {
        return Integer.parseInt(texto.substring(1, texto.indexOf("]")));
    }
    
    private void excluirDoacao() {
        String selecionado = listDoacoes.getSelectedValue();
        if (selecionado != null) {
            int id = extrairId(selecionado);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?");
            if (confirm == JOptionPane.YES_NO_OPTION) {
            	controller = new DonationController(id, 0, 0, null, null, null, null);
                controller.executeDelete();
            }
        }
    }

}
