/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas.AsientoDetalle;

import Models.AsientoDetalle;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;




/**
 *
 * @author diego
 */
public class AsientoDetalleForm extends javax.swing.JFrame {

   private DefaultTableModel tableModel;
   private List<Integer> listaIdsAsientos;  // Lista para almacenar IDs de asientos
    private List<Integer> listaIdsCuentas;   // Lista para almacenar IDs de cuentas
    
    public AsientoDetalleForm() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         tableModel = (DefaultTableModel) tableDetalles.getModel();
       cargarDatos();
        listaIdsAsientos = new ArrayList<>();
        listaIdsCuentas = new ArrayList<>();
        cargarComboBoxAsientos();
        cargarComboBoxCuentas();
       
    }
   
    // Método para cargar el combobox de Asientos
    private void cargarComboBoxAsientos() {
        cmbAsiento.removeAllItems(); // Limpiar el combobox
        for (Entry<Integer, String> asiento : AsientoDetalle.obtenerAsientosConNombres()) {
            cmbAsiento.addItem(asiento.getValue()); // Agregar solo el nombre
            cmbAsiento.setSelectedIndex(-1); // Opcional: para dejar vacío por defecto
        }
    }

    // Método para cargar el combobox de Cuentas
    private void cargarComboBoxCuentas() {
        cmbCuenta.removeAllItems(); // Limpiar el combobox
        for (Entry<Integer, String> cuenta : AsientoDetalle.obtenerCuentasConNombres()) {
            cmbCuenta.addItem(cuenta.getValue()); // Agregar solo el nombre
            cmbCuenta.setSelectedIndex(-1); // Opcional: para dejar vacío por defecto
        }
    }


    // Método para obtener el ID del asiento seleccionado en el ComboBox
    public int obtenerIdAsientoSeleccionado() {
        int selectedIndex = cmbAsiento.getSelectedIndex();
        return selectedIndex >= 0 ? listaIdsAsientos.get(selectedIndex) : -1;
    }

    // Método para obtener el ID de la cuenta seleccionada en el ComboBox
    public int obtenerIdCuentaSeleccionada() {
        int selectedIndex = cmbCuenta.getSelectedIndex();
        return selectedIndex >= 0 ? listaIdsCuentas.get(selectedIndex) : -1;
    }


private void cargarDatos() {
    List<AsientoDetalle> detalles = AsientoDetalle.obtenerDetalles();

    // Obtener nombres de asientos y cuentas en Mapas
    Map<Integer, String> mapaAsientos = new HashMap<>();
    for (Map.Entry<Integer, String> asiento : AsientoDetalle.obtenerAsientosConNombres()) {
        mapaAsientos.put(asiento.getKey(), asiento.getValue());
    }

    Map<Integer, String> mapaCuentas = new HashMap<>();
    for (Map.Entry<Integer, String> cuenta : AsientoDetalle.obtenerCuentasConNombres()) {
        mapaCuentas.put(cuenta.getKey(), cuenta.getValue());
    }

    // Limpiar la tabla antes de cargar datos nuevos
    tableModel.setRowCount(0);

    // Agregar filas con datos visibles en todas las columnas
    for (AsientoDetalle detalle : detalles) {
        tableModel.addRow(new Object[]{
            detalle.getIdAsientoDetalle(),
            detalle.getIdAsiento(),                    
            mapaAsientos.get(detalle.getIdAsiento()),  
            detalle.getIdCuenta(),                     
            mapaCuentas.get(detalle.getIdCuenta()),    
            detalle.getMontoDebito(),
            detalle.getMontoCredito()
        });
    }
}


    
    // Método para guardar un nuevo detalle o editar uno existente
    private void guardar() {
        int idAsiento = cmbAsiento.getSelectedIndex() + 1; // Supongamos que los índices comienzan en 1
        int idCuenta = cmbCuenta.getSelectedIndex() + 1;   // Lo mismo para cuentas
        double montoDebito = Double.parseDouble(txtMontoDebito.getText());
        double montoCredito = Double.parseDouble(txtMontoCredito.getText());

        AsientoDetalle detalle = new AsientoDetalle(idAsiento, idCuenta, montoDebito, montoCredito);
        detalle.guardar(); // Guardar el detalle en la base de datos

        // Recargar los datos después de guardar
        cargarDatos();
        limpiarCampos();
    }

   public void editar() {
    try {
        
        int idAsiento = Integer.parseInt(cmbAsiento.getSelectedItem().toString());
        int idCuenta = Integer.parseInt(cmbCuenta.getSelectedItem().toString());
        double montoDebito = Double.parseDouble(txtMontoDebito.getText());
        double montoCredito = Double.parseDouble(txtMontoCredito.getText());

        AsientoDetalle detalle = new AsientoDetalle(idAsiento, idCuenta, montoDebito, montoCredito);
        detalle.actualizar();  // Método para realizar la edición en la base de datos

        JOptionPane.showMessageDialog(this, "Registro editado con éxito.");
        cargarDatos();  // Refresca la tabla después de editar
        limpiarCampos();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Error de formato en los datos: " + e.getMessage());
    }
}


private void eliminar() {
    int selectedRow = tableDetalles.getSelectedRow();
    if (selectedRow >= 0) {
        try {
            // Obtener los IDs desde las columnas ocultas
            int idAsiento = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            int idCuenta = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());

            // Crear el detalle a eliminar usando los IDs
            AsientoDetalle detalle = new AsientoDetalle(idAsiento, idCuenta, 0, 0);
            detalle.eliminar();

            // Eliminar la fila de la tabla sin necesidad de recargar todos los datos
            tableModel.removeRow(selectedRow);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los IDs de la fila seleccionada.");
            e.printStackTrace();
        }
    } else {
        JOptionPane.showMessageDialog(this, "Selecciona un registro para eliminar.");
    }
}


    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        cmbAsiento.setSelectedIndex(0);
        cmbCuenta.setSelectedIndex(0);
        txtMontoDebito.setText("");
        txtMontoCredito.setText("");
    }

private void seleccionarRegistroTabla() {
    int filaSeleccionada = tableDetalles.getSelectedRow();
    
    if (filaSeleccionada != -1) {
        // Obtener los datos de la fila seleccionada
        int idAsientoSeleccionado = (int) tableModel.getValueAt(filaSeleccionada, 0);
        String nombreAsientoSeleccionado = (String) tableModel.getValueAt(filaSeleccionada, 1);
        int idCuentaSeleccionada = (int) tableModel.getValueAt(filaSeleccionada, 2);
        String nombreCuentaSeleccionada = (String) tableModel.getValueAt(filaSeleccionada, 3);
        double montoDebito = (double) tableModel.getValueAt(filaSeleccionada, 4);
        double montoCredito = (double) tableModel.getValueAt(filaSeleccionada, 5);
        
        // Seleccionar el nombre del Asiento en el combobox `cmbAsiento` basado en el nombre
        for (int i = 0; i < cmbAsiento.getItemCount(); i++) {
            if (cmbAsiento.getItemAt(i).equals(nombreAsientoSeleccionado)) {
                cmbAsiento.setSelectedIndex(i);
                break;
            }
        }

        // Seleccionar el nombre de la Cuenta en el combobox `cmbCuenta` basado en el nombre
        for (int i = 0; i < cmbCuenta.getItemCount(); i++) {
            if (cmbCuenta.getItemAt(i).equals(nombreCuentaSeleccionada)) {
                cmbCuenta.setSelectedIndex(i);
                break;
            }
        }

        // Establecer los valores de monto en los campos de texto
        txtMontoDebito.setText(String.valueOf(montoDebito));
        txtMontoCredito.setText(String.valueOf(montoCredito));
    }
}





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDetalles = new javax.swing.JTable();
        cmbAsiento = new javax.swing.JComboBox<>();
        cmbCuenta = new javax.swing.JComboBox<>();
        txtMontoDebito = new javax.swing.JTextField();
        txtMontoCredito = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Detalle Asientos");

        tableDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id Asiento Detalle", "Id Asiento", "Asiento Contable", "Id Cuenta Contable", "Cuenta Contable", "Monto Debito", "Monto Credito"
            }
        ));
        tableDetalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDetallesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableDetalles);

        cmbAsiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAsientoActionPerformed(evt);
            }
        });

        cmbCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCuentaActionPerformed(evt);
            }
        });

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

        jLabel2.setText("Id Asiento:");

        jLabel3.setText("Id Cuenta:");

        jLabel4.setText("Monto Debito:");

        jLabel5.setText("Monto Credito:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cmbAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMontoDebito, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMontoCredito, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(102, 102, 102))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(314, 314, 314))
            .addGroup(layout.createSequentialGroup()
                .addGap(245, 245, 245)
                .addComponent(btnAgregar)
                .addGap(49, 49, 49)
                .addComponent(btnEditar)
                .addGap(58, 58, 58)
                .addComponent(btnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMontoDebito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMontoCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableDetallesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDetallesMouseClicked
             seleccionarRegistroTabla();
    }//GEN-LAST:event_tableDetallesMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
               guardar();
        
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
              editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
              eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cmbAsientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAsientoActionPerformed
        
    }//GEN-LAST:event_cmbAsientoActionPerformed

    private void cmbCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCuentaActionPerformed
        
    }//GEN-LAST:event_cmbCuentaActionPerformed

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
            java.util.logging.Logger.getLogger(AsientoDetalleForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsientoDetalleForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsientoDetalleForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsientoDetalleForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AsientoDetalleForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JComboBox<String> cmbAsiento;
    private javax.swing.JComboBox<String> cmbCuenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableDetalles;
    private javax.swing.JTextField txtMontoCredito;
    private javax.swing.JTextField txtMontoDebito;
    // End of variables declaration//GEN-END:variables

    private void cargarDetalleSeleccionado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
