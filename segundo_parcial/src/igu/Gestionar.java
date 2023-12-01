package igu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    private JTextField textFieldCapacidad;
    private JTextField textFieldCantidad;
    private JTable table;
    private Resto restauranteSeleccionado;
    private Menu menu;

    public Gestionar(Resto restauranteSeleccionado, Menu menu) {
        this.restauranteSeleccionado = restauranteSeleccionado;
        this.menu = menu;

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
        lblCapacidad.setBounds(10, 32, 86, 19);
        panel.add(lblCapacidad);

        textFieldCapacidad = new JTextField();
        textFieldCapacidad.setBounds(106, 32, 86, 20);
        panel.add(textFieldCapacidad);
        textFieldCapacidad.setColumns(10);

        JLabel lblCantidad = new JLabel("Cantidad de Mesas:");
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCantidad.setBounds(10, 62, 150, 19);
        panel.add(lblCantidad);

        textFieldCantidad = new JTextField();
        textFieldCantidad.setBounds(160, 62, 40, 20);
        panel.add(textFieldCantidad);
        textFieldCantidad.setColumns(10);

        JButton btnAlta = new JButton("Alta");
        btnAlta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                darDeAltaMesas();
            }
        });
        btnAlta.setBounds(10, 100, 89, 23);
        panel.add(btnAlta);

        JButton btnBaja = new JButton("Baja");
        btnBaja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                darDeBajaMesasSeleccionadas();
                actualizarTablaMesasLiberadas();
            }
        });
        btnBaja.setBounds(109, 100, 89, 23);
        panel.add(btnBaja);

        JButton btnSalir = new JButton("Volver");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                menu.setVisible(true);
            }
        });
        btnSalir.setBounds(208, 100, 89, 23);
        panel.add(btnSalir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 134, 524, 156);
        panel.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; 
            }
        };

        tableModel.addColumn("Seleccionar");
        tableModel.addColumn("Número de Mesa");
        tableModel.addColumn("Capacidad");
        tableModel.addColumn("Estado");

        table.setModel(tableModel);

        table.getColumnModel().getColumn(0).setCellEditor(new javax.swing.DefaultCellEditor(new javax.swing.JCheckBox()));
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Boolean.class));

        actualizarTablaMesasLiberadas();
    }

    private void darDeAltaMesas() {
        int cantidadMesas = Integer.parseInt(textFieldCantidad.getText());
        int capacidad = Integer.parseInt(textFieldCapacidad.getText());

        for (int i = 0; i < cantidadMesas; i++) {
            Mesa nuevaMesa = new Mesa();
            nuevaMesa.setCapacidad(capacidad);
            nuevaMesa.setConsumo(0);
            nuevaMesa.setResto(restauranteSeleccionado);
            nuevaMesa.setEstado(new EstadoServic().obtenerEstadoPorNombre("Liberada"));

            MesaServic mesaServic = new MesaServic();
            mesaServic.insertarMesa(nuevaMesa);
        }

        actualizarTablaMesasLiberadas();
    }

    private void darDeBajaMesasSeleccionadas() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean selected = (Boolean) table.getValueAt(i, 0);
            if (selected) {
                int numeroMesa = (Integer) table.getValueAt(i, 1);

                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de dar de baja la mesa " + numeroMesa + "?",
                        "Confirmar Baja de Mesa", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    darDeBajaMesa(numeroMesa);
                }
            }
        }
    }

    private void darDeBajaMesa(int numeroMesa) {
        MesaServic mesaServic = new MesaServic();
        mesaServic.darDeBajaMesa(numeroMesa);
    }

    private void actualizarTablaMesasLiberadas() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);
        int idRestaurante = restauranteSeleccionado.getIdResto();

        MesaServic mesaServic = new MesaServic();
        List<Mesa> mesasLiberadas = mesaServic.obtenerTodasLasMesasLiberadasPorRestaurante(idRestaurante);

        for (Mesa mesa : mesasLiberadas) {
            tableModel.addRow(new Object[]{false, mesa.getNro_mesa(), mesa.getCapacidad(), mesa.getEstado().nombreEstado()});
        }

        tableModel.fireTableDataChanged();
    }

   
}
