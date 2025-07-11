package View.Address;

import Controller.AddressController;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class ManagerAddress extends JFrame {
    private JList<String> listEnderecos;
    private DefaultListModel<String> listModel;
    private JButton btnExcluir, btnNovo, btnAtualizar, btnEditar;
    private AddressController controller;
    private ObjectId idUsuario;

    public ManagerAddress(ObjectId idUsuario) {
        this.idUsuario = idUsuario;
        
        this.controller = new AddressController(this.idUsuario);

        setTitle("Gerenciar Endereços");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listModel = new DefaultListModel<>();
        listEnderecos = new JList<>(listModel);
        listEnderecos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(listEnderecos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnAtualizar = new JButton("Atualizar");
        btnNovo = new JButton("Novo Endereço");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNovo);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> carregarEnderecos());
        btnNovo.addActionListener(e -> new RegisterAddress(this.idUsuario, this).setVisible(true));
        btnExcluir.addActionListener(e -> excluirEndereco());
        btnEditar.addActionListener(e -> {
            String selecionado = listEnderecos.getSelectedValue();
            if (selecionado != null) {
                ObjectId idEndereco = extrairId(selecionado);
                new EditAddress(this.idUsuario, idEndereco, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um endereço para editar.");
            }
        });

        carregarEnderecos();
    }

    public void carregarEnderecos() {
        listModel.clear();
        controller.listAddresses(this.idUsuario).forEach(listModel::addElement);
    }

    private void excluirEndereco() {
        String selecionado = listEnderecos.getSelectedValue();
        if (selecionado != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ObjectId idEndereco = extrairId(selecionado);
                if(idEndereco != null) {
                    controller.executeDelete(idEndereco);
                    carregarEnderecos();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um endereço para excluir.");
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