package View.Telephone;

import javax.swing.*;
import Controller.TelephoneController;

import java.awt.*;
import java.util.List;

public class ManagerTelephone extends JFrame {
    private JList<String> listTelefones;
    private DefaultListModel<String> listModel;
    private JButton btnExcluir, btnNovo, btnAtualizar, btnEditar;
    private TelephoneController controller;
    private int idUsuario;

    public ManagerTelephone(int idUsuario) {
    	this.idUsuario = idUsuario;
    	
        setTitle("Gerenciar Telefones");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listTelefones = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listTelefones);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        btnAtualizar = new JButton("Atualizar");
        btnNovo = new JButton("Novo Telefone");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNovo);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> carregarTelefones());
        btnNovo.addActionListener(e -> new RegisterTelephone(idUsuario).setVisible(true));
        btnExcluir.addActionListener(e -> excluirEndereco());
        btnEditar.addActionListener(e -> {
            String selecionado = listTelefones.getSelectedValue();
            if (selecionado != null) {
                int id = extrairId(selecionado);
                EditTelephone editor = new EditTelephone(id);
                editor.setVisible(true);
            }
        });

        carregarTelefones();
    }

    public void carregarTelefones() {
        listModel.clear();
        controller = new TelephoneController(0, idUsuario, null, null, null);
        List<String> telefones = controller.listTelephones();
        for (String e : telefones) {
            listModel.addElement(e);
        }
    }

    private void excluirEndereco() {
        String selecionado = listTelefones.getSelectedValue();
        if (selecionado != null) {
            int id = Integer.parseInt(selecionado.substring(1, selecionado.indexOf("]")));
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_NO_OPTION) {
                controller = new TelephoneController(id, idUsuario, null, null, null);
                controller.executeDelete();
            }
        }
        carregarTelefones();
    }
    
    private int extrairId(String texto) {
        return Integer.parseInt(texto.substring(1, texto.indexOf("]")));
    }

}
