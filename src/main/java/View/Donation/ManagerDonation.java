package View.Donation;

import javax.swing.*;
import Controller.DonationController;
import java.awt.*;
import java.util.List;

public class ManagerDonation extends JFrame {
    private JList<String> listDoacoes;
    private DefaultListModel<String> listModel;
    private JButton btnEditar, btnExcluir, btnNova, btnAtualizar;
    private JComboBox<String> comboFiltroStatus;
    private DonationController controller;
    private int idUsuario;

    public ManagerDonation(int idUser) {
        this.idUsuario = idUser;
        this.controller = new DonationController(0, idUsuario, 0, null, null, null, null);

        setTitle("Gerenciar Doações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listDoacoes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listDoacoes);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTopo.add(new JLabel("Filtrar por status:"));
        comboFiltroStatus = new JComboBox<>(new String[] {"Todos", "Pendente", "Realizada", "Cancelada"});
        panelTopo.add(comboFiltroStatus);
        add(panelTopo, BorderLayout.NORTH);

        JPanel panelBotoes = new JPanel();
        btnAtualizar = new JButton("Atualizar");
        btnNova = new JButton("Nova Doação");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

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
            RegisterDonation telaCadastro = new RegisterDonation(this.idUsuario);
            telaCadastro.setVisible(true);
        });

        btnAtualizar.addActionListener(e -> carregarDoacoes());

        comboFiltroStatus.addActionListener(e -> carregarDoacoes());
    }

    void carregarDoacoes() {
        listModel.clear();
        String statusSelecionado = (String) comboFiltroStatus.getSelectedItem();
        List<String> doacoes = controller.listarDoacoesHemocentro(this.idUsuario, statusSelecionado);
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
            if (confirm == JOptionPane.YES_OPTION) {
                controller = new DonationController(id, 0, 0, null, null, null, null);
                controller.executeDelete();
                carregarDoacoes();
            }
        }
    }
}
