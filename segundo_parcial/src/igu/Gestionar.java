package igu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import entidades.Mesa;
import entidades.Resto;
import servicio.EstadoServic;
import servicio.MesaServic;

public class Gestionar extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTable table;
    private Resto restauranteSeleccionado;

    public Gestionar(Resto restauranteSeleccionado) {
        this.restauranteSeleccionado = restauranteSeleccionado;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 572, 346);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Gestión de Mesas");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblNewLabel.setBounds(10, 0, 467, 22);
        panel.add(lblNewLabel);

        JLabel lblCapacidad = new JLabel("Capacidad:");
        lblCapacidad.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCapacidad.setBounds(151, 62, 86, 19);
        panel.add(lblCapacidad);

        textField = new JTextField();
        textField.setBounds(247, 62, 86, 20);
        panel.add(textField);
        textField.setColumns(10);

        JButton btnAlta = new JButton("Alta");
        btnAlta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int capacidad = Integer.parseInt(textField.getText());

                Mesa nuevaMesa = new Mesa();
                nuevaMesa.setCapacidad(capacidad);
                nuevaMesa.setConsumo(0);
                nuevaMesa.setResto(restauranteSeleccionado);
                nuevaMesa.setEstado(new EstadoServic().obtenerEstadoPorNombre("Liberada"));

                MesaServic mesaServic = new MesaServic();
                mesaServic.insertarMesa(nuevaMesa);

                actualizarTablaMesasLiberadas();
            }
        });
        btnAlta.setBounds(151, 100, 89, 23);
        panel.add(btnAlta);

        JButton btnBaja = new JButton("Baja");
        btnBaja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    int numeroMesa = (int) table.getValueAt(selectedRow, 0);

                    darDeBajaMesa(numeroMesa);

                    // Actualizar tabla después de dar de baja una mesa
                    actualizarTablaMesasLiberadas();
                }
            }
        });
        btnBaja.setBounds(250, 100, 89, 23);
        panel.add(btnBaja);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnSalir.setBounds(449, 100, 89, 23);
        panel.add(btnSalir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 134, 524, 156);
        panel.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Número de Mesa");
        tableModel.addColumn("Capacidad");
        tableModel.addColumn("Estado");
        table.setModel(tableModel);

        actualizarTablaMesasLiberadas();
    }

    private void actualizarTablaMesasLiberadas() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);
        int idRestaurante = restauranteSeleccionado.getIdResto();

        MesaServic mesaServic = new MesaServic();
        List<Mesa> mesasLiberadas = mesaServic.obtenerTodasLasMesasLiberadasPorRestaurante(idRestaurante);

        for (Mesa mesa : mesasLiberadas) {
            tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), mesa.getEstado().nombreEstado()});
        }

        tableModel.fireTableDataChanged();
    }


    private void darDeBajaMesa(int numeroMesa) {
        MesaServic mesaServic = new MesaServic();
        mesaServic.darDeBajaMesa(numeroMesa);

        // Línea añadida para actualizar la tabla después de dar de baja una mesa
        actualizarTablaMesasLiberadas();
    }
}
