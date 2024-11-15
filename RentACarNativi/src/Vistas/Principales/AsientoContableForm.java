/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas.Principales;

import Models.AsientoContable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author diego
 */
public class AsientoContableForm extends javax.swing.JFrame {

    
    private DefaultTableModel tableModel;
    private int idAsientoSeleccionado = -1;
    
    public AsientoContableForm() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cargarAsientosEnTabla();
    }
    
    
    
    // Método para agregar un nuevo asiento contable
private void guardarAsiento() {
    // Obteniendo los datos del formulario
    String fechaAsientoStr = txtFechaAsiento.getText(); // Supongo que txtFechaAsiento es un JTextField con la fecha como String
    String descripcionAsiento = txtDescripcionAsiento.getText();

    // Convertir la fecha a un formato adecuado si es necesario
    java.util.Date fechaAsiento = null;
    try {
        fechaAsiento = new SimpleDateFormat("dd/MM/yyyy").parse(fechaAsientoStr); // Parseamos la fecha del formato String a Date
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Error en el formato de la fecha: " + e.getMessage());
        return;  // Salimos si la fecha es incorrecta
    }

    // Crear objeto AsientoContable con los datos del formulario
    AsientoContable asiento = new AsientoContable(fechaAsiento, descripcionAsiento);

    try {
        if (idAsientoSeleccionado == 0) {
            // Es un nuevo asiento contable, lo guardamos
            asiento.guardar();
        } else {
            // Si hay un ID, estamos actualizando un asiento existente
            asiento.setIdAsiento(idAsientoSeleccionado);  // Asignamos el ID para la actualización
            asiento.actualizar();
        }
        JOptionPane.showMessageDialog(this, "Asiento guardado exitosamente.");
        limpiarCampos();  // Limpiar los campos del formulario
        actualizarTabla();  // Recargar la tabla con los datos actualizados
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el asiento: " + e.getMessage());
    }
}




    
    private void eliminarAsiento() {
    int filaSeleccionada = tablaAsientos.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un asiento en la tabla para eliminarlo.");
        return;
    }

    int idAsiento = (int) tablaAsientos.getValueAt(filaSeleccionada, 0);

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este asiento?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        AsientoContable.eliminarAsiento(idAsiento);
        cargarAsientosEnTabla();  // Actualizar la tabla después de eliminar
        limpiarCampos();  // Limpiar los campos después de eliminar
        idAsientoSeleccionado = -1;  // Resetear el ID
    }
}
    
   private void editarAsiento() {
    // Obtener la fila seleccionada en la tabla
    int filaSeleccionada = tablaAsientos.getSelectedRow();
    
    if (filaSeleccionada != -1) {
        // Obtener el ID del asiento seleccionado
        int idAsiento = (int) tablaAsientos.getValueAt(filaSeleccionada, 0);
        
        // Obtener el asiento contable correspondiente a partir del ID
        AsientoContable asientoSeleccionado = AsientoContable.obtenerAsientoPorId(idAsiento);

        // Llenar los campos del formulario con los datos del asiento seleccionado
        cargarDatosAsiento(asientoSeleccionado);

        // Asignar el ID del asiento seleccionado a la variable idAsientoSeleccionado
        idAsientoSeleccionado = asientoSeleccionado.getIdAsiento();
    }
}

private void cargarDatosAsiento(AsientoContable asientoSeleccionado) {
    // Supongo que AsientoContable tiene los métodos getFecha() y getDescripcion()
    txtFechaAsiento.setText(asientoSeleccionado.getFechaAsiento());  // Fecha del asiento
    txtDescripcionAsiento.setText(asientoSeleccionado.getDescripcionAsiento());  // Descripción del asiento
}
    
    
    // Método para cargar los datos de un asiento seleccionado en los campos del formulario
private void cargarAsientoSeleccionado() {
    int filaSeleccionada = tablaAsientos.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un asiento en la tabla para editarlo.");
        return;
    }

    // Obtener el ID del asiento seleccionado y cargar los datos
    idAsientoSeleccionado = (int) tablaAsientos.getValueAt(filaSeleccionada, 0);
    AsientoContable asiento = AsientoContable.obtenerAsientoPorId(idAsientoSeleccionado);

    if (asiento != null) {
        txtFechaAsiento.setText(asiento.getFechaAsiento());
        txtDescripcionAsiento.setText(asiento.getDescripcionAsiento());
    }
}
    
    private void cargarAsientosEnTabla() {
    List<AsientoContable> asientos = AsientoContable.obtenerAsientos();
    DefaultTableModel modelo = (DefaultTableModel) tablaAsientos.getModel();
    modelo.setRowCount(0); // Limpiar la tabla antes de cargar datos

    for (AsientoContable asiento : asientos) {
        modelo.addRow(new Object[]{
            asiento.getIdAsiento(),
            asiento.getFechaAsiento(),
            asiento.getDescripcionAsiento()
        });
    }
}
    
    // Validación de la fecha con el formato "dd/MM/yyyy"
    private boolean validarFecha(String fecha) {
        return fecha.matches("^\\d{2}/\\d{2}/\\d{4}$");
    }
    
    
    private void limpiarCampos() {
    txtFechaAsiento.setText("");
    txtDescripcionAsiento.setText("");
}
    
    
    private void actualizarTabla() {
    List<AsientoContable> cuentas = AsientoContable.obtenerAsientos();  // Obtener todas las cuentas
    DefaultTableModel modelo = (DefaultTableModel) tablaAsientos.getModel();
    modelo.setRowCount(0);  // Limpiar la tabla

    for (AsientoContable cuenta : cuentas) {
        modelo.addRow(new Object[]{
            cuenta.getIdAsiento(),
            cuenta.getFechaAsiento(),
            cuenta.getDescripcionAsiento()
            
        });
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAsientos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtFechaAsiento = new javax.swing.JTextField();
        txtDescripcionAsiento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tablaAsientos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Fecha Asiento", "Descripcion Asiento"
            }
        ));
        tablaAsientos.getTableHeader().setReorderingAllowed(false);
        tablaAsientos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAsientosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaAsientos);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Asientos Contables");

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

        jLabel2.setText("Fecha:");

        jLabel3.setText("Descripcion:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAgregar)
                                .addGap(76, 76, 76)
                                .addComponent(btnEditar)
                                .addGap(73, 73, 73)
                                .addComponent(btnEliminar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFechaAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(75, 75, 75)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDescripcionAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescripcionAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaAsientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAsientosMouseClicked
        editarAsiento();
    }//GEN-LAST:event_tablaAsientosMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        guardarAsiento();
        limpiarCampos();
        actualizarTabla();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        guardarAsiento();
        limpiarCampos();
        actualizarTabla();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarAsiento();
        actualizarTabla();
        limpiarCampos();
    }//GEN-LAST:event_btnEliminarActionPerformed

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
            java.util.logging.Logger.getLogger(AsientoContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsientoContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsientoContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsientoContableForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AsientoContableForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaAsientos;
    private javax.swing.JTextField txtDescripcionAsiento;
    private javax.swing.JTextField txtFechaAsiento;
    // End of variables declaration//GEN-END:variables
}
