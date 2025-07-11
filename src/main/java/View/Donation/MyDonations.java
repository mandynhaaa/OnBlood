package View.Donation;

import Controller.DonationController;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;

public class MyDonations extends JFrame {
    private JList<String> listDoacoes;
    private DefaultListModel<String> listModel;
    private DonationController controller;
    private ObjectId idUsuarioDoador;

    private JComboBox<String> cbStatus;
    private JButton btnFiltrar;

    public MyDonations(ObjectId idUsuarioDoador) {
        this.idUsuarioDoador = idUsuarioDoador;
        this.controller = new DonationController();

        setTitle("Minhas Doações");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Status:"));
        cbStatus = new JComboBox<>(new String[]{"Todos", "Pendente", "Realizada", "Cancelada"});
        panelFiltros.add(cbStatus);
        btnFiltrar = new JButton("Filtrar");
        panelFiltros.add(btnFiltrar);
        add(panelFiltros, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listDoacoes = new JList<>(listModel);
        listDoacoes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(listDoacoes), BorderLayout.CENTER);

        carregarDoacoes();

        btnFiltrar.addActionListener(e -> carregarDoacoes());
        cbStatus.addActionListener(e -> carregarDoacoes()); 
    }

    private void carregarDoacoes() {
        listModel.clear();
        String statusSelecionado = (String) cbStatus.getSelectedItem();
        
        controller.listarDoacoesPorDoador(idUsuarioDoador, statusSelecionado)
                  .forEach(listModel::addElement);
    }
}