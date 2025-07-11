package View.Telephone;

import Controller.TelephoneController;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class ManagerTelephone extends JFrame {
    private JList<String> listTelefones;
    private DefaultListModel<String> listModel;
    private JButton btnExcluir, btnNovo, btnAtualizar, btnEditar;
    private TelephoneController controller;
    private ObjectId idUsuario;

    public ManagerTelephone(ObjectId idUsuario) {
        this.idUsuario = idUsuario;
        this.controller = new TelephoneController(idUsuario);

        setTitle("Gerenciar Telefones");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listModel = new DefaultListModel<>();
        listTelefones = new JList<>(listModel);
        listTelefones.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(listTelefones), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
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
        btnNovo.addActionListener(e -> new RegisterTelephone(idUsuario, this).setVisible(true));
        btnEditar.addActionListener(e -> editarTelefone());
        btnExcluir.addActionListener(e -> excluirTelefone());

        carregarTelefones();
    }

    public void carregarTelefones() {
        listModel.clear();
        controller.listTelephones(this.idUsuario).forEach(listModel::addElement);
    }

    private void editarTelefone() {
        String selecionado = listTelefones.getSelectedValue();
        if (selecionado != null) {
            ObjectId idTelefone = extrairId(selecionado);
            if (idTelefone != null) {
                new EditTelephone(idUsuario, idTelefone, this).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um telefone para editar.");
        }
    }

    private void excluirTelefone() {
        String selecionado = listTelefones.getSelectedValue();
        if (selecionado != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ObjectId idTelefone = extrairId(selecionado);
                if (idTelefone != null) {
                    controller.executeDelete(idTelefone);
                    carregarTelefones();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um telefone para excluir.");
        }
    }

    private ObjectId extrairId(String texto) {
        try {
            String hexId = texto.substring(texto.indexOf('[') + 1, texto.indexOf(']'));
            return new ObjectId(hexId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de ID inválido na lista.");
            return null;
        }
    }
}