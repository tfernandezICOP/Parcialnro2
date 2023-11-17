package igu;

import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import entidades.Estado;
import entidades.Mesa;
import entidades.Reserva;
import entidades.Resto;
import servicio.EstadoServic;
import servicio.MesaServic;
import servicio.ReservaServic;

public class ReservaIgu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTable table;
    private JFormattedTextField formattedTextField;
    private DefaultTableModel tableModel;
    private Resto restauranteSeleccionado;
    private Mesa mesaSeleccionada;
    EstadoServic estadoServic = new EstadoServic();
    Reserva reserva = new Reserva();
    MesaServic mesaServic = new MesaServic();
    public ReservaIgu(Resto restauranteSeleccionado) {
        this.restauranteSeleccionado = restauranteSeleccionado;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        contentPane = new JPanel();
        contentPane.setBorder(null);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 684, 461);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Registrar Reserva");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 24));
        lblNewLabel.setBounds(240, 20, 260, 30);
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Nombre y Apellido");
        lblNewLabel_1.setBounds(20, 80, 120, 20);
        panel.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Comensales");
        lblNewLabel_1_1.setBounds(20, 130, 120, 20);
        panel.add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_2 = new JLabel("Fecha");
        lblNewLabel_1_2.setBounds(400, 80, 100, 20);
        panel.add(lblNewLabel_1_2);

        textField = new JTextField();
        textField.setBounds(160, 80, 180, 25);
        panel.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(160, 130, 180, 25);
        panel.add(textField_1);

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            formattedTextField = new JFormattedTextField(dateMask);
            formattedTextField.setBounds(500, 80, 150, 25);
            panel.add(formattedTextField);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JButton btnNewButton = new JButton("Buscar Mesas Disponibles");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarMesasDisponibles();
            }
        });
        btnNewButton.setBounds(400, 130, 200, 25);
        panel.add(btnNewButton);

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "N° de mesa", "Capacidad", "Estado" }));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 180, 650, 150);
        panel.add(scrollPane);

        tableModel = (DefaultTableModel) table.getModel();

        JButton btnNewButton_1 = new JButton("Guardar Reserva");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarReserva();
            }
        });
        btnNewButton_1.setBounds(360, 350, 150, 25);
        panel.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Salir");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnNewButton_2.setBounds(520, 350, 150, 25);
        panel.add(btnNewButton_2);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() || table.getSelectedRowCount() == 0) {
                    return;
                }
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Object idMesaObject = tableModel.getValueAt(selectedRow, 0);
                    if (idMesaObject != null) {
                        try {
                            int idMesa = Integer.parseInt(idMesaObject.toString());
                            int capacidad = (int) tableModel.getValueAt(selectedRow, 1);

                            mesaSeleccionada = new Mesa();
                            mesaSeleccionada.setId_mesa(idMesa);
                            mesaSeleccionada.setNro_mesa(idMesa); 
                            mesaSeleccionada.setCapacidad(capacidad);
                            mesaSeleccionada.setEstado(estadoServic.obtenerEstadoPorNombre("Reservada"));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });



    }

    private void buscarMesasDisponibles() {
        String nombreApellido = textField.getText();
        int comensales = Integer.parseInt(textField_1.getText());

        String fechaStr = formattedTextField.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = new Date(sdf.parse(fechaStr).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int idRestaurante = restauranteSeleccionado.getIdResto();

        List<Mesa> mesasDisponibles = mesaServic.obtenerMesasLiberadasConCapacidadPorRestaurante(comensales, idRestaurante);

        mesaSeleccionada = null;

        if (!mesasDisponibles.isEmpty()) {
            for (Mesa mesa : mesasDisponibles) {
                if (mesaSeleccionada == null && !mesaEstaReservada(mesa, fecha)) {
                    mesaSeleccionada = mesa;
                    break;
                }
            }
        }

        if (mesaSeleccionada != null) {
            mesaSeleccionada.setId_mesa(mesaServic.obtenerMesaPorId(mesaSeleccionada.getNro_mesa()).getId_mesa());
        }

        actualizarTabla(mesasDisponibles);
    }
    private boolean mesaEstaReservada(Mesa mesa, Date fecha) {
        ReservaServic reservaServic = new ReservaServic();
        List<Reserva> reservas = reservaServic.buscarReservasPorMesaYFecha(mesa.getId_mesa(), fecha);
        return !reservas.isEmpty();
    }

    private void actualizarTabla(List<Mesa> mesas) {
        tableModel.setRowCount(0);

        for (Mesa mesa : mesas) {
            Estado estado = mesa.getEstado();
            tableModel.addRow(new Object[] {mesa.getNro_mesa(), mesa.getCapacidad(), estado.nombreEstado() });
        }
    }

    private void guardarReserva() {
        if (mesaSeleccionada != null && mesaSeleccionada.getId_mesa() != 0) {
            int selectedRow = table.getSelectedRow();
            tableModel.setValueAt(mesaSeleccionada.getEstado().nombreEstado(), selectedRow, 2);

            reserva.setNombreApellido(textField.getText());
            reserva.setComensales(Integer.parseInt(textField_1.getText()));

            String fechaStr = formattedTextField.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date fecha = new Date(sdf.parse(fechaStr).getTime());
                reserva.setFecha(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            reserva.setMesa(mesaSeleccionada);

            ReservaServic reservaServic = new ReservaServic();
            System.out.println("ID de la mesa seleccionada: " + mesaSeleccionada.getId_mesa());
            reservaServic.insertarReserva(reserva, mesaSeleccionada.getId_mesa());
        } else {
            System.out.println("La mesa seleccionada no es válida. Asegúrate de que la mesa tenga un ID válido.");
        }
    }
}
