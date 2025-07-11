package View.Donation;

import Controller.DonationController;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class ManagerDonation extends JFrame {
    private JList<String> listDoacoes;
    private DefaultListModel<String> listModel;
    private JButton btnEditar, btnExcluir, btnNova, btnAtualizar;
    private JComboBox<String> comboFiltroStatus;
    private DonationController controller;
    private ObjectId idUsuarioHemocentro;

    public ManagerDonation(ObjectId idUsuarioHemocentro) {
        this.idUsuarioHemocentro = idUsuarioHemocentro;
        this.controller = new DonationController(); 

        setTitle("Gerenciar Doações Recebidas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTopo.add(new JLabel("Filtrar por status:"));
        comboFiltroStatus = new JComboBox<>(new String[]{"Todos", "Pendente", "Realizada", "Cancelada"});
        panelTopo.add(comboFiltroStatus);
        add(panelTopo, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listDoacoes = new JList<>(listModel);
        listDoacoes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(listDoacoes), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnAtualizar = new JButton("Atualizar");
        btnNova = new JButton("Nova Doação");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnNova);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        add(panelBotoes, BorderLayout.SOUTH);

        btnNova.addActionListener(e -> new RegisterDonation(idUsuarioHemocentro, this).setVisible(true));
        btnAtualizar.addActionListener(e -> carregarDoacoes());
        comboFiltroStatus.addActionListener(e -> carregarDoacoes());
        btnEditar.addActionListener(e -> editarDoacao());
        btnExcluir.addActionListener(e -> excluirDoacao());

        carregarDoacoes();
    }

    public void carregarDoacoes() {
        listModel.clear();
        String statusSelecionado = (String) comboFiltroStatus.getSelectedItem();
        controller.listBloodCenterDonations(this.idUsuarioHemocentro, statusSelecionado)
                  .forEach(listModel::addElement);
    }

    private void editarDoacao() {
        String selecionado = listDoacoes.getSelectedValue();
        if (selecionado != null) {
            ObjectId idDoacao = extrairId(selecionado);
            if (idDoacao != null) {
                new EditDonation(idDoacao, this).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma doação para editar.");
        }
    }

    private void excluirDoacao() {
        String selecionado = listDoacoes.getSelectedValue();
        if (selecionado != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmar exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ObjectId idDoacao = extrairId(selecionado);
                if (idDoacao != null) {
                    controller.executeDelete(idDoacao);
                    carregarDoacoes();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma doação para excluir.");
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