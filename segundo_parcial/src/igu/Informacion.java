package igu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
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
import javax.swing.text.MaskFormatter;

import entidades.Estado;
import entidades.Mesa;
import entidades.Resto;
import servicio.MesaServic;

public class Informacion extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Resto restauranteSeleccionado;
    private JFormattedTextField formattedTextField;
    private Mesa mesaSeleccionada;
    MesaServic mesaServic = new MesaServic();

    public Informacion(Resto restauranteSeleccionado) {
        this.restauranteSeleccionado = restauranteSeleccionado;

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

        JButton btnVistaActual = new JButton("Vista actual");
        btnVistaActual.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarVistaActual();
            }
        });
        btnVistaActual.setBounds(50, 47, 150, 30);
        panel.add(btnVistaActual);

        JButton btnAcercaDe = new JButton("Acerca de");
        btnAcercaDe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarAcercaDe();
            }
        });
        btnAcercaDe.setBounds(646, 47, 104, 30);
        panel.add(btnAcercaDe);

        JButton btnVistaFecha = new JButton("Vista x fecha");
        btnVistaFecha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarMesasDisponiblesParaFecha();
            }
        });
        btnVistaFecha.setBounds(50, 113, 150, 30);
        panel.add(btnVistaFecha);

        JLabel lblInformacion = new JLabel("Informacion");
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

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            formattedTextField = new JFormattedTextField(dateMask);
            formattedTextField.setBounds(210, 114, 150, 30);
            panel.add(formattedTextField);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Nro. Mesa");
        tableModel.addColumn("Capacidad");
        tableModel.addColumn("Estado");
        table.setModel(tableModel);

        // Configurar el renderizador para la columna de Estado
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
        
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        	}
        });
        btnSalir.setBounds(646, 400, 104, 30);
        panel.add(btnSalir);

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

    private void mostrarMesasDisponiblesParaFecha() {
        try {
            String fechaStr = formattedTextField.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = new Date(sdf.parse(fechaStr).getTime());
            int idResto = restauranteSeleccionado.getIdResto();

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setRowCount(0);

            List<Mesa> todasLasMesas = mesaServic.obtenerTodasLasMesasPorRestaurante(idResto);

            for (Mesa mesa : todasLasMesas) {
                String estadoMesa = mesa.getEstado().nombreEstado();

                boolean tieneReserva = mesaServic.tieneReservaEnFecha(mesa.getId_mesa(), fecha);
                boolean esOcupada = "ocupada".equals(estadoMesa);

                if (tieneReserva && esOcupada) {
                    tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), "ocupada"});
                } else if (tieneReserva) {
                    tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), "reservada"});
                } else if ("liberada".equals(estadoMesa)) {
                    tableModel.addRow(new Object[]{mesa.getNro_mesa(), mesa.getCapacidad(), estadoMesa});
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
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
        mostrarVistaActual();
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
