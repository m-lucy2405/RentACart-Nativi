/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas.Gerente;

import Vistas.Admin.*;
import Models.CuentaContable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author diego
 */
public class CuentaContablePanelGerente extends javax.swing.JPanel {

    private ArrayList<CuentaContable> cuentas;  // Lista para almacenar las cuentas
    private CuentaContable cuentaSeleccionada = null;
    private int idCuentaSeleccionada;
    
    public CuentaContablePanelGerente() {
        initComponents();
        
       cargarCuentasEnTabla();
        llenarComboBoxes();
        cuentas = new ArrayList<>();
        btnAgregar.setVisible(false);
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
    }
    
        // Método para llenar los ComboBox
private void llenarComboBoxes() {
    // Llenar TipoCuenta con valores predeterminados
    cmbTipoCuenta.addItem("Activo");
    cmbTipoCuenta.addItem("Pasivo");
    cmbTipoCuenta.addItem("Patrimonio");
    
    // Llenar Estado con valores predeterminados
    cmbEstado.addItem("Activo");
    cmbEstado.addItem("Inactivo");
}

    
    private void cargarCuentasEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCuentas.getModel();
        modelo.setRowCount(0);
        
        List<CuentaContable> cuentas = CuentaContable.obtenerCuentas();
        for (CuentaContable cuenta : cuentas) {
            modelo.addRow(new Object[]{
                cuenta.getIdCuenta(),
                cuenta.getNombreCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getCodigoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
    
    // Método para guardar o actualizar la cuenta
private void guardarCuenta() {
    // Obteniendo datos del formulario
    String nombreCuenta = txtNombreCuenta.getText();
    String tipoCuenta = (String) cmbTipoCuenta.getSelectedItem();
    String codigoCuenta = txtCodigoCuenta.getText();
    double saldoInicial = Double.parseDouble(txtSaldoInicial.getText());
    boolean estado = cmbEstado.getSelectedItem().equals("Activo");

    // Crear objeto CuentaContable con los datos del formulario
    CuentaContable cuenta = new CuentaContable(nombreCuenta, tipoCuenta, codigoCuenta, saldoInicial, estado);
    cuenta.setNombreCuenta(nombreCuenta);
    cuenta.setTipoCuenta(tipoCuenta);
    cuenta.setCodigoCuenta(codigoCuenta);
    cuenta.setSaldoInicial(saldoInicial);
    cuenta.setEstado(estado);

    try {
        if (idCuentaSeleccionada == 0) {
            // Creación de nuevo registro
            cuenta.guardar();
        } else {
            // Edición de registro existente
            cuenta.setIdCuenta(idCuentaSeleccionada);  // Asigna el ID para actualizar
            cuenta.actualizar();  // Llama a un método de actualización en el modelo
        }
        JOptionPane.showMessageDialog(this, "Cuenta guardada exitosamente.");
        limpiarCampos();
        cargarDatosEnTabla();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al guardar la cuenta: " + e.getMessage());
    }
}

private void editarCuenta() {
       int filaSeleccionada = tablaCuentas.getSelectedRow();
    if (filaSeleccionada != -1) {
        // Obtener la cuenta seleccionada directamente de la tabla
        int idCuenta = (int) tablaCuentas.getValueAt(filaSeleccionada, 0);
        CuentaContable cuentaSeleccionada = CuentaContable.obtenerCuentaPorId(idCuenta);

        // Llenar los campos del formulario con los datos de la cuenta seleccionada
        cargarDatosCuenta(cuentaSeleccionada);

        // Asignar el ID de la cuenta seleccionada a la variable idCuentaSeleccionada
        idCuentaSeleccionada = cuentaSeleccionada.getIdCuenta();
    }
}

 private void eliminarCuenta(){
       // Obtener el índice de la fila seleccionada en la tabla
    int filaSeleccionada = tablaCuentas.getSelectedRow();

    if (filaSeleccionada != -1) {  // Si hay una fila seleccionada
        // Obtener el ID de la cuenta seleccionada
        int idCuenta = (int) tablaCuentas.getValueAt(filaSeleccionada, 0);  // Suponiendo que el ID está en la primera columna

        // Llamar al método para eliminar la cuenta
        boolean eliminado = CuentaContable.eliminarCuenta(idCuenta);

        if (eliminado) {
            JOptionPane.showMessageDialog(null, "Cuenta eliminada exitosamente.");
            // Actualizar la tabla después de eliminar la cuenta
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar la cuenta.");
        }
    } else {
        JOptionPane.showMessageDialog(null, "Por favor, selecciona una cuenta para eliminar.");
    }
 }

// Método de cargar datos en la tabla después de cualquier cambio
private void cargarDatosEnTabla() {
    DefaultTableModel modelo = (DefaultTableModel) tablaCuentas.getModel();
    modelo.setRowCount(0);

    cuentas = (ArrayList<CuentaContable>) CuentaContable.obtenerCuentas();  // Actualizar la lista
    for (CuentaContable cuenta : cuentas) {
        modelo.addRow(new Object[]{
            cuenta.getIdCuenta(),
            cuenta.getNombreCuenta(),
            cuenta.getTipoCuenta(),
            cuenta.getCodigoCuenta(),
            cuenta.getSaldoInicial(),
            cuenta.isEstado() ? "Activo" : "Inactivo"
        });
    }
}


private void actualizarTabla() {
    List<CuentaContable> cuentas = CuentaContable.obtenerCuentas();  // Obtener todas las cuentas
    DefaultTableModel modelo = (DefaultTableModel) tablaCuentas.getModel();
    modelo.setRowCount(0);  // Limpiar la tabla

    for (CuentaContable cuenta : cuentas) {
        modelo.addRow(new Object[]{
            cuenta.getIdCuenta(),
            cuenta.getNombreCuenta(),
            cuenta.getTipoCuenta(),
            cuenta.getCodigoCuenta(),
            cuenta.getSaldoInicial(),
            cuenta.isEstado() ? "Activo" : "Inactivo"
        });
    }
}


// Método para cargar los datos de la cuenta seleccionada en el formulario
private void cargarDatosCuenta(CuentaContable cuenta) {
    txtNombreCuenta.setText(cuenta.getNombreCuenta());
    txtCodigoCuenta.setText(cuenta.getCodigoCuenta());
    txtSaldoInicial.setText(String.valueOf(cuenta.getSaldoInicial()));
    
    // Seleccionar el tipo de cuenta en el ComboBox
    cmbTipoCuenta.setSelectedItem(cuenta.getTipoCuenta());

    // Seleccionar el estado en el ComboBox
    cmbEstado.setSelectedItem(cuenta.isEstado() ? "Activo" : "Inactivo");
}


private void limpiarCampos() {
    txtNombreCuenta.setText("");
    txtCodigoCuenta.setText("");
    txtSaldoInicial.setText("");
    cmbTipoCuenta.setSelectedIndex(0);  // Establecer el primer valor
    cmbEstado.setSelectedIndex(0);  // Establecer el primer valor
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        txtNombreCuenta = new javax.swing.JTextField();
        cmbTipoCuenta = new javax.swing.JComboBox<>();
        txtCodigoCuenta = new javax.swing.JTextField();
        txtSaldoInicial = new javax.swing.JTextField();
        cmbEstado = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCuentas = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jLabel5.setText("Saldo Inicial:");

        btnEditar.setBackground(new java.awt.Color(0, 0, 0));
        btnEditar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        jLabel6.setText("Estado:");

        btnEliminar.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        tablaCuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Nombre Cuenta", "Tipo Cuenta", "Codigo Cuenta", "Saldo Inicial", "Estado"
            }
        ));
        tablaCuentas.getTableHeader().setReorderingAllowed(false);
        tablaCuentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaCuentasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaCuentas);

        jLabel2.setText("Tipo Cuenta:");

        jLabel3.setText("Nombre Cuenta:");

        jLabel4.setText("Codigo Cuenta:");

        btnAgregar.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregar.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cuentas Contables");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombreCuenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoCuenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSaldoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(43, 43, 43))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombreCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2)
                                .addGap(2, 2, 2)
                                .addComponent(cmbTipoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(2, 2, 2)
                                        .addComponent(txtCodigoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(1, 1, 1)
                                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addGap(5, 5, 5)
                                .addComponent(txtSaldoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        guardarCuenta();
        limpiarCampos();
        actualizarTabla();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarCuenta();
        actualizarTabla();
        limpiarCampos();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tablaCuentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaCuentasMouseClicked
        editarCuenta();
    }//GEN-LAST:event_tablaCuentasMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        guardarCuenta();
        limpiarCampos();
        actualizarTabla();
    }//GEN-LAST:event_btnAgregarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbTipoCuenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaCuentas;
    private javax.swing.JTextField txtCodigoCuenta;
    private javax.swing.JTextField txtNombreCuenta;
    private javax.swing.JTextField txtSaldoInicial;
    // End of variables declaration//GEN-END:variables
}
