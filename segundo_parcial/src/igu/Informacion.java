package igu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entidades.Estado;
import entidades.Mesa;
import entidades.Resto;
import servicio.MesaServic;

public class Informacion extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Resto restauranteSeleccionado;
    private Mesa mesaSeleccionada;
    private MesaServic mesaServic = new MesaServic();
    private Menu menu;

    public Informacion(Resto restauranteSeleccionado, Menu menu) {
        this.restauranteSeleccionado = restauranteSeleccionado;
        this.menu = menu;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 784, 461);
        contentPane.add(panel);
        panel.setLayout(null);

        JButton btnAcercaDe = new JButton("Acerca de");
        btnAcercaDe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarAcercaDe();
            }
        });
        btnAcercaDe.setBounds(646, 47, 104, 30);
        panel.add(btnAcercaDe);

        JButton btnVistaFecha = new JButton("Buscar");
        btnVistaFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarMesasDisponiblesParaFechaActual();
            }
        });
        btnVistaFecha.setBounds(144, 87, 150, 30);
        panel.add(btnVistaFecha);

        JLabel lblInformacion = new JLabel("Informacion Actual");
        lblInformacion.setFont(new Font("Arial", Font.BOLD, 24));
        lblInformacion.setBounds(10, 0, 283, 40);
        panel.add(lblInformacion);

        JLabel lblNombreResto = new JLabel("Nombre: " + restauranteSeleccionado.getNombre());
        lblNombreResto.setBounds(303, 14, 300, 20);
        panel.add(lblNombreResto);

        JLabel lblDomicilioResto = new JLabel("Domicilio: " + restauranteSeleccionado.getDomicilio());
        lblDomicilioResto.setBounds(303, 45, 300, 20);
        panel.add(lblDomicilioResto);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 154, 700, 200);
        panel.add(scrollPane);

        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nro. Mesa");
        tableModel.addColumn("Capacidad");
        tableModel.addColumn("Estado");
        table.setModel(tableModel);

        table.getColumnModel().getColumn(2).setCellRenderer(new EstadoMesaRenderer());

        JButton btnLiberar = new JButton("Liberar");
        btnLiberar.setBounds(435, 400, 150, 30);
        panel.add(btnLiberar);
        btnLiberar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                liberarMesa();
            }
        });

        JButton btnOcupar = new JButton("Ocupar");
        btnOcupar.setBounds(246, 400, 150, 30);
        panel.add(btnOcupar);
        btnOcupar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ocuparMesa();
            }
        });

        JButton btnReservar = new JButton("Reservar");
        btnReservar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reservarMesa();
            }
        });
        btnReservar.setBounds(69, 400, 150, 30);
        panel.add(btnReservar);

        JButton btnSalir = new JButton("Volver");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                menu.setVisible(true);
            }
        });
        btnSalir.setBounds(646, 400, 104, 30);
        panel.add(btnSalir);

        JLabel lblNewLabel = new JLabel("Vista Actual:");
        lblNewLabel.setBounds(50, 87, 84, 30);
        panel.add(lblNewLabel);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRowCount() > 0) {
                    int selectedRow = table.getSelectedRow();
                    mesaSeleccionada = obtenerMesaDesdeFila(selectedRow);
                }
            }
        });
    }

    private Mesa obtenerMesaDesdeFila(int fila) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        Object idMesaObject = tableModel.getValueAt(fila, 0);

        if (idMesaObject != null) {
            try {
                if (idMesaObject.toString().matches("\\d+")) {
                    int idMesa = Integer.parseInt(idMesaObject.toString());
                    int capacidad = (int) tableModel.getValueAt(fila, 1);

                    Mesa mesa = new Mesa();
                    mesa.setId_mesa(idMesa);
                    mesa.setNro_mesa(idMesa);
                    mesa.setCapacidad(capacidad);

                    return mesa;
                } else {
                    System.out.println("El valor no es un número.");
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    private void mostrarAcercaDe() {
        String mensaje = "\nNombre y Apellido: Tomas Fernandez\nFecha: 15/11/2023\nExamen: Parcial 2";
        JOptionPane.showMessageDialog(this, mensaje, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarTabla(List<Mesa> mesas) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        for (Mesa mesa : mesas) {
            Estado estado = mesa.getEstado();
            tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), estado.nombreEstado()});
        }
    }

    private void mostrarVistaActual() {
        int idResto = restauranteSeleccionado.getIdResto();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        List<Mesa> mesasRestaurante = mesaServic.obtenerTodasLasMesasPorRestaurante(idResto);

        actualizarTabla(mesasRestaurante);
    }


    private void mostrarMesasDisponiblesParaFechaActual() {
        int idResto = restauranteSeleccionado.getIdResto();

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        List<Mesa> todasLasMesas = mesaServic.obtenerTodasLasMesasPorRestaurante(idResto);

        for (Mesa mesa : todasLasMesas) {
            String estadoMesa = mesa.getEstado().nombreEstado();

            if ("ocupada".equals(estadoMesa)) {
                tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), "ocupada"});
            } else if ("liberada".equals(estadoMesa)) {
                tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), estadoMesa});
            } else {
                tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), estadoMesa});
            }
        }
    }


    private void reservarMesa() {
        if (mesaSeleccionada != null) {
            if (mesaServic.puedeReservarMesa(mesaSeleccionada.getId_mesa())) {
                mesaServic.reservarMesa(mesaSeleccionada.getId_mesa());
                cambiarEstadoYActualizarVista();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede reservar la mesa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una mesa antes de reservar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void liberarMesa() {
        if (mesaSeleccionada != null) {
            if (mesaServic.puedeLiberarMesa(mesaSeleccionada.getId_mesa())) {
                mesaServic.liberarMesa(mesaSeleccionada.getId_mesa());
                cambiarEstadoYActualizarVista();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede liberar la mesa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una mesa antes de liberar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ocuparMesa() {
        if (mesaSeleccionada != null) {
            if (mesaServic.puedeOcuparMesa(mesaSeleccionada.getId_mesa())) {
                mesaServic.ocuparMesa(mesaSeleccionada.getId_mesa());
                cambiarEstadoYActualizarVista();
            } else {
                JOptionPane.showMessageDialog(this, "No se puede ocupar la mesa.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una mesa antes de ocupar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoYActualizarVista() {
        int idMesaSeleccionada = (mesaSeleccionada != null) ? mesaSeleccionada.getId_mesa() : -1;

        mostrarVistaActual();

        seleccionarFilaPorIdMesa(idMesaSeleccionada);
    }

    private void seleccionarFilaPorIdMesa(int idMesa) {
        for (int i = 0; i < table.getRowCount(); i++) {
            int nroMesa = (int) table.getValueAt(i, 0);
            if (nroMesa == idMesa) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }
    }

    private class EstadoMesaRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String estado = ((String) table.getValueAt(row, 2)).toLowerCase();

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
