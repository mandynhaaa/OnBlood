package View.Donation;

import Controller.DonationController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyDonations extends JFrame {
    private JList<String> listDoacoes;
    private DefaultListModel<String> listModel;
    private DonationController controller;
    private int idUsuario;

    private JComboBox<String> cbStatus;
    private JComboBox<String> cbHemocentro;
    private JButton btnAtualizar;

    public MyDonations(int idUsuario) {
        this.idUsuario = idUsuario;
        this.controller = new DonationController(0, 0, idUsuario, null, null, null, null);

        setTitle("Minhas Doações");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        listDoacoes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listDoacoes);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelFiltros = new JPanel(new FlowLayout());

        cbStatus = new JComboBox<>(new String[]{"Todos", "Pendente", "Realizada", "Cancelada"});
        panelFiltros.add(new JLabel("Status:"));
        panelFiltros.add(cbStatus);

        cbHemocentro = new JComboBox<>();
        panelFiltros.add(new JLabel("Hemocentro:"));
        panelFiltros.add(cbHemocentro);

        btnAtualizar = new JButton("Filtrar");
        panelFiltros.add(btnAtualizar);

        add(panelFiltros, BorderLayout.NORTH);

        carregarHemocentros();
        carregarDoacoes();

        btnAtualizar.addActionListener(e -> carregarDoacoes());
    }

    private void carregarDoacoes() {
        listModel.clear();
        String statusSelecionado = (String) cbStatus.getSelectedItem();
        String hemocentroSelecionado = (String) cbHemocentro.getSelectedItem();
        List<String> doacoes = controller.listarDoacoesPorUsuario(idUsuario, statusSelecionado, hemocentroSelecionado);
        for (String d : doacoes) {
            listModel.addElement(d);
        }
    }

    private void carregarHemocentros() {
        cbHemocentro.removeAllItems();
        List<String> hemocentros = controller.listarHemocentros();
        for (String h : hemocentros) {
            cbHemocentro.addItem(h);
        }
    }
}
