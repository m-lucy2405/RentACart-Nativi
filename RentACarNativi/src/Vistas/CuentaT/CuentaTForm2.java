/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vistas.CuentaT;

import Models.CuentaContable;
import Models.CuentaT;
import static Models.CuentaT.cargarCuentasTemporales;
import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author diego
 */
public class CuentaTForm2 extends javax.swing.JFrame {

    private int idCuenta;

    
    
    
    
    public CuentaTForm2() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Llamar a los métodos para cargar datos
        cargarIdCuentasEnComboBox();  // Si estás utilizando un ComboBox para filtrar por cuenta
        
        
        
        
        // Inicializar los componentes visuales
        JDateChooser selectfechaInicio = new JDateChooser(); 
        JDateChooser selectfechaFin = new JDateChooser();
        
        
        selectfechaInicio.setDateFormatString("dd/MM/yyyy");
        selectfechaFin.setDateFormatString("dd/MM/yyyy");
    }
    
    public java.sql.Date[] obtenerFechas() {
    // Obtener las fechas seleccionadas desde los JDateChooser
    Date fechaInicioUtil = selectfechaInicio.getDate();
    Date fechaFinUtil = selectfechaFin.getDate();

    // Verificar si alguna de las fechas es nula
    if (fechaInicioUtil == null || fechaFinUtil == null) {
        // Mostrar mensaje de error si alguna fecha es nula
        JOptionPane.showMessageDialog(null, "Por favor, seleccione ambas fechas.", "Error", JOptionPane.ERROR_MESSAGE);
        return null; // Si alguna fecha es nula, retornamos null
    } else {
        // Convertir java.util.Date a java.sql.Date
        java.sql.Date fechaInicioSql = new java.sql.Date(fechaInicioUtil.getTime());
        java.sql.Date fechaFinSql = new java.sql.Date(fechaFinUtil.getTime());

        // Mostrar las fechas convertidas para verificar
        System.out.println("Fecha de Inicio (java.sql.Date): " + fechaInicioSql);
        System.out.println("Fecha de Fin (java.sql.Date): " + fechaFinSql);

        return new java.sql.Date[] { fechaInicioSql, fechaFinSql };
    }
}

 
    
    
   public void botonGenerarCuentasT() {
    // Obtener las fechas usando el método de conversión
    java.sql.Date[] fechas = obtenerFechas();

    // Verificar que las fechas no son nulas
    if (fechas == null) {
        System.out.println("Alguna fecha es nula, no se puede continuar.");
        return; // Si alguna fecha es nula, salimos del método
    }

    // Si las fechas son válidas, pasamos a la lógica de generación de cuentas T
    java.sql.Date sqlFechaInicio = fechas[0];
    java.sql.Date sqlFechaFin = fechas[1];

    // Obtener el idCuenta desde el campo o componente que ya tienes (ajusta esto según tu implementación)
    int idCuenta = Integer.parseInt(cmbCuenta.getSelectedItem().toString());  // Suponiendo que es un ComboBox con el ID de cuenta

    // Lógica para generar las cuentas T temporales
    CuentaT.borrarCuentasTemporales(idCuenta);
    CuentaT.generarCuentasTemporales(sqlFechaInicio, sqlFechaFin, idCuenta);  // Genera las nuevas cuentas temporales
     
    // Cargar los datos en la tabla
     cargarDatosEnTabla();
}
   
   // Método para cargar los datos en la tabla
public void cargarDatosEnTabla() {
    // Obtener los datos de la base de datos
    ArrayList<CuentaT> cuentasT = cargarCuentasTemporales();

    // Crear las columnas para el JTable
    String[] columnNames = {"ID Temporal", "ID Cuenta", "Nombre Cuenta",  "Fecha Asiento", "Descripción Asiento", "Débito", "Crédito", "Saldo Final",  "Fecha Generación"};
    
    // Crear el modelo de la tabla con las columnas y las filas
    Object[][] rowData = new Object[cuentasT.size()][columnNames.length];

    // Llenar los datos en el modelo
    for (int i = 0; i < cuentasT.size(); i++) {
        CuentaT cuenta = cuentasT.get(i);
        rowData[i][0] = cuenta.getIdTemporal();
        rowData[i][1] = cuenta.getIdCuenta();
        rowData[i][2] = cuenta.getNombreCuenta();
        rowData[i][3] = cuenta.getFechaAsiento();
        rowData[i][4] = cuenta.getDescripcionAsiento();
        rowData[i][5] = cuenta.getDebito();
        rowData[i][6] = cuenta.getCredito();
        rowData[i][7] = cuenta.getSaldoFinal();
        rowData[i][8] = cuenta.getFechaGeneracion();
        
    }

    // Crear un DefaultTableModel con los datos y columnas
    DefaultTableModel model = new DefaultTableModel(rowData, columnNames);

    // Asignar el modelo a la tabla
    tableCuentasT.setModel(model);
    
    // Configurar el ancho de cada columna
    tableCuentasT.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID Temporal
    tableCuentasT.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID Cuenta
    tableCuentasT.getColumnModel().getColumn(2).setPreferredWidth(150); // Nombre Cuenta
    tableCuentasT.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha Asiento
    tableCuentasT.getColumnModel().getColumn(4).setPreferredWidth(200); // Descripción Asiento
    tableCuentasT.getColumnModel().getColumn(5).setPreferredWidth(80);  // Débito
    tableCuentasT.getColumnModel().getColumn(6).setPreferredWidth(80);  // Crédito
    tableCuentasT.getColumnModel().getColumn(7).setPreferredWidth(100); // Saldo Final
    tableCuentasT.getColumnModel().getColumn(8).setPreferredWidth(150); // Fecha Generación
}

  
    public void cargarIdCuentasEnComboBox() {
    // Obtener la lista de cuentas desde la base de datos
    ArrayList<CuentaContable> cuentas = (ArrayList<CuentaContable>) CuentaContable.obtenerCuentas();  // Método que obtiene todas las cuentas

    // Limpiar el JComboBox antes de llenarlo
    cmbCuenta.removeAllItems();

    // Agregar una opción por defecto (si lo deseas)
    cmbCuenta.addItem("Seleccione una cuenta");

    // Recorrer la lista de cuentas y agregar cada idCuenta al JComboBox
    for (CuentaContable cuenta : cuentas) {
     cmbCuenta.addItem(String.valueOf(cuenta.getIdCuenta()));  // Convierte el ID de la cuenta a String
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
        tableCuentasT = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnGenerar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbCuenta = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        selectfechaInicio = new com.toedter.calendar.JDateChooser();
        selectfechaFin = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableCuentasT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id Cuenta T", "Id Cuenta Contable", "Nombre Cuenta ", "Mes", "Descripcion Asiento", "Total Debitos", "Total Creditos", "Saldo Final", "Fecha de Generacion"
            }
        ));
        jScrollPane1.setViewportView(tableCuentasT);
        if (tableCuentasT.getColumnModel().getColumnCount() > 0) {
            tableCuentasT.getColumnModel().getColumn(0).setPreferredWidth(25);
            tableCuentasT.getColumnModel().getColumn(1).setPreferredWidth(25);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Cuentas T");

        btnGenerar.setText("Generar Cuenta");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
            }
        });

        jLabel2.setText("Fecha Inicio:");

        jLabel3.setText("Fecha Fin:");

        jLabel4.setText("Id Cuenta:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(382, 382, 382)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(94, 94, 94)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(selectfechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(73, 73, 73)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectfechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(371, 371, 371)
                        .addComponent(btnGenerar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectfechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectfechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(btnGenerar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        botonGenerarCuentasT();
    }//GEN-LAST:event_btnGenerarActionPerformed

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
            java.util.logging.Logger.getLogger(CuentaTForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CuentaTForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CuentaTForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CuentaTForm2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CuentaTForm2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerar;
    private javax.swing.JComboBox<String> cmbCuenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser selectfechaFin;
    private com.toedter.calendar.JDateChooser selectfechaInicio;
    private javax.swing.JTable tableCuentasT;
    // End of variables declaration//GEN-END:variables

}
