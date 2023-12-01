package igu;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import entidades.Mesa;
import entidades.Resto;
import servicio.MesaServic;

public class Estadistica extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Resto restauranteSeleccionado;
    private MesaServic mesaServic;
    private DefaultTableModel model;
    private JComboBox<String> filtroComboBox;

    /**
     * Create the frame.
     */
    public Estadistica(Resto restauranteSeleccionado, Menu menuAnterior) {
        this.restauranteSeleccionado = restauranteSeleccionado;
        mesaServic = new MesaServic();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 560, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 544, 350);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblEstadistica = new JLabel("Estadística");
        lblEstadistica.setBounds(10, 0, 414, 30);
        lblEstadistica.setFont(lblEstadistica.getFont().deriveFont(18.0f));
        panel.add(lblEstadistica);

        model = new DefaultTableModel(new Object[]{"Nro de Mesa", "Capacidad", "Consumo", "Estado"}, 0);
        table = new JTable(model);

        filtroComboBox = new JComboBox<>();
        filtroComboBox.addItem("Consumo");
        filtroComboBox.addItem("Liberadas");
        filtroComboBox.addItem("Reservadas");
        filtroComboBox.addItem("Ocupadas");
        filtroComboBox.setBounds(10, 45, 120, 30);
        panel.add(filtroComboBox);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBounds(140, 45, 80, 30);
        btnFiltrar.addActionListener(this::filtrarTabla);
        panel.add(btnFiltrar);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 90, 524, 200);
        panel.add(scrollPane);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(414, 300, 120, 32);
        btnVolver.addActionListener(e -> {
            Menu menuFrame = new Menu(restauranteSeleccionado);
            menuFrame.setVisible(true);
            dispose();
        });
        panel.add(btnVolver);

        table.getColumnModel().getColumn(3).setCellRenderer(new EstadoMesaRenderer());
    }

    private void filtrarTabla(ActionEvent e) {
        String seleccion = filtroComboBox.getSelectedItem().toString();
        switch (seleccion) {
            case "Consumo":
                List<Mesa> mesasOrdenadas = mesaServic.obtenerMesasConMayorConsumoLambda(restauranteSeleccionado.getIdResto(), 3);
                actualizarTabla(mesasOrdenadas);
                break;
            case "Liberadas":
                List<Mesa> mesasLiberadas = mesaServic.obtenerMesasPorEstadolambda("liberada", restauranteSeleccionado.getIdResto());
                actualizarTabla(mesasLiberadas);
                break;
            case "Reservadas":
                List<Mesa> mesasReservadas = mesaServic.obtenerMesasPorEstadolambda("reservada", restauranteSeleccionado.getIdResto());
                actualizarTabla(mesasReservadas);
                break;
            case "Ocupadas":
                List<Mesa> mesasOcupadas = mesaServic.obtenerMesasPorEstadolambda("ocupada", restauranteSeleccionado.getIdResto());
                actualizarTabla(mesasOcupadas);
                break;
        }
    }

    private void actualizarTabla(List<Mesa>... listas) {
        model.setRowCount(0);
        for (List<Mesa> lista : listas) {
            for (Mesa mesa : lista) {
                Object[] row = {mesa.getNro_mesa(), mesa.getCapacidad(), mesa.getConsumo(), mesa.getEstado().nombreEstado()};
                model.addRow(row);
            }
        }
        table.setModel(model);
    }

    private class EstadoMesaRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String estado = ((String) table.getValueAt(row, 3)).toLowerCase();

            switch (estado) {
                case "liberada":
                    cellComponent.setBackground(Color.GREEN);
                    break;
                case "reservada":
                    cellComponent.setBackground(Color.YELLOW);
                    break;
                case "ocupada":
                    cellComponent.setBackground(Color.RED);
                    break;
                default:
                    cellComponent.setBackground(table.getBackground());
            }

            return cellComponent;
        }
    }
}
