/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas.Principales;

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
public class CuentaContableForm extends javax.swing.JFrame {

    
    
    private ArrayList<CuentaContable> cuentas;  // Lista para almacenar las cuentas
    private CuentaContable cuentaSeleccionada = null;
    private int idCuentaSeleccionada;
    /**
     * Creates new form CuentaContableForm
     */
    public CuentaContableForm() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       cargarCuentasEnTabla();
        llenarComboBoxes();
        cuentas = new ArrayList<>(); 
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCuentas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtNombreCuenta = new javax.swing.JTextField();
        cmbTipoCuenta = new javax.swing.JComboBox<>();
        txtCodigoCuenta = new javax.swing.JTextField();
        txtSaldoInicial = new javax.swing.JTextField();
        cmbEstado = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Cuentas Contables");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(180, 180, 180)))
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAgregar)
                        .addGap(39, 39, 39)
                        .addComponent(btnEditar)
                        .addGap(39, 39, 39)
                        .addComponent(btnEliminar)
                        .addGap(174, 174, 174)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombreCuenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoCuenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCodigoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSaldoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregar)
                            .addComponent(btnEditar)
                            .addComponent(btnEliminar))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtNombreCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(cmbTipoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(txtCodigoCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(txtSaldoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
       guardarCuenta();
       limpiarCampos();
       actualizarTabla();
    }//GEN-LAST:event_btnAgregarActionPerformed

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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CuentaContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CuentaContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CuentaContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CuentaContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CuentaContableForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbTipoCuenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaCuentas;
    private javax.swing.JTextField txtCodigoCuenta;
    private javax.swing.JTextField txtNombreCuenta;
    private javax.swing.JTextField txtSaldoInicial;
    // End of variables declaration//GEN-END:variables
}
