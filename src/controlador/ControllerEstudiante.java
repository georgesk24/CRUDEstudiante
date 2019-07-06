package controlador;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Estudiante;
import modelo.EstudianteDAO;
import vista.Principal;

/**
 *
 * @author root
 */
public class ControllerEstudiante implements ActionListener, KeyListener{

    private Principal view;
    private EstudianteDAO modelo = new EstudianteDAO();
    
    private int id=-1;
    
    
    
    /*metodo para asignar los respectivos eventos a los componentes de la interfaz grafica, 
    utilizaremos eventos tipo Action y eventos de teclado.
    los eventos tipo action(ActionListener) seran utilizados especificamente cuando se oprima algun boton en la interfaz grafica
    los eventos de teclado(KeyListener) los utilizaremos para validar los datos ingresados por teclado, por ejemplo:
    evaluar si un campo de texto solo permiten valores numericos */
    public final void events(){
        
        /*agregamos eventos tipo actionListener pertencientes al formulario de registro
        para acceder a los componentes de la interfaz debemos llamarlo de la siguiente manera :
        nombreDelObjetoDeLaClase.componente 
        para este caso seria: view.componente seguido del metodo addActionListener*/
        
        /*Componentes pertenecientes al formulario de registro */
        this.view.btnRegistrarEstudiante.addActionListener(this);
        this.view.btnActualizarEstudiante.addActionListener(this);
        this.view.btnLimpiarEstudiante.addActionListener(this);
        this.view.btnActualizarTablaFormularioRegistro.addActionListener(this);
        
        /*Componentes pertenecientes al formulario de consulta*/
        this.view.btnBuscarConsulta.addActionListener(this);
        this.view.btnRegistrarView.addActionListener(this);
        this.view.btnActualizarView.addActionListener(this);
        this.view.btnEliminar.addActionListener(this);
        
        /*Componente eventos de teclado*/
        this.view.txtCodigo.addKeyListener(this);
        this.view.txtNumDoc.addKeyListener(this);
        
    }
    
    /*
    Creamos el metodo constructor de la clase, este metodo se ejecutara automaticamente cuando 
    creamos un objeto de la clase como por ejemplo: 
    ControllerEstudiante objeto = new ControllerEstudiante()
    Al hacer esta llamada automaticamente se ejecutara el metodo constructor y con este las lineas de codigo 
    que esten dentro del metodo constructor.
    
    Para este caso creamos el metodo constructor inicialmente para obtener los componentes de la vista principal
    y de esta manera poder acceder a ellos, igualmente lo utilizaremos para asignar los eventos que estaremos utilizando
    */
    public ControllerEstudiante(Principal view){

        this.view=view;
        
        /*agregamos los eventos, para esto llamamos al respectivo metodo donde se encuentran estas lineas de codigo
        este es el metodo events()*/
        events();
        
        mostrarDatosTabla(view.tableDatos);
        mostrarDatosTabla(view.tableBusqueda);

        
    }
        
    /*Creamos un metodo llamado listarDatos el cual llevara un parametro
    de tipo JTable y de esta manera listar los datos obtenidos de la base de datos
    en le JTable*/
    private DefaultTableModel listarDatos(JTable table){
        
        DefaultTableModel modelTable = null;
        /*Realizamos la consulta a la base de datos y asignamos en objeto de tipo ArrayList
        el resultado obtenido*/
        ArrayList<Estudiante> list = modelo.consultarEstudiante("Todo", "");

        /*validamos si la longitud del arraylist es mayor que cero*/
        if(list.size()>0){
            
            /*Creamos un objeto de tipo DefaultTableModel para poder 
            realizar las operaciones y poder agregar las respectivas filas 
            al JTable*/
            modelTable = (DefaultTableModel) table.getModel();
            
            /*recorremos el arraylist y agregamos las respectivas filas 
            al JTable*/
            for(int i=0; i<list.size(); i++){
                
                /*Creamos un vector de dimension 5 sera de tipo Object
                y de esta manera nos permitira utilizar multiples tipos de datos*/
                Object datos[] = new Object[5];
                
                /*obtenemos los valores a mostrar en el JTable*/
                datos[0]=list.get(i).getId();
                datos[1]=list.get(i).getNombres();
                datos[2]=list.get(i).getApellidos();
                datos[3]=list.get(i).getGenero();
                datos[4]=list.get(i).getTelefono();
                
                /*agregamos una nueva fila haciendo uso del metodo addRow
                le pasamos como parametro el vector de tipo Object*/
                modelTable.addRow(datos);
                
            }
            
        
        }
        
        return modelTable;
        
    }
    
    /*mostramos datos en un JTable*/
    private void mostrarDatosTabla(JTable table){
        /*creamos un objeto de tipo DefaultTableModel, 
        si este es diferente de null asignamos el modelo*/
        DefaultTableModel modelTable = listarDatos(table);
        if(modelTable != null){
            table.setModel(modelTable);        
        }        
    }
    
    /*Eliminamos todas las filas que contenga un JTable*/
    private void eliminarTodoFilas(JTable table){
        DefaultTableModel modeloTabla = (DefaultTableModel) table.getModel();
        modeloTabla.getDataVector().removeAllElements();
        modeloTabla.fireTableDataChanged();
    }
    
    /*validamos si el valori ingresado en un numero*/
    private boolean isNumericValue(String val){
        
        boolean condicion;
        
        /*creamo un bloque de tipo try/catch y realizamos la conversion
        a numero entero, si todo sale bien devolvera true
        de lo contrario devolvera false*/
        try{
            int value = Integer.parseInt(val);
            condicion=true;
        }catch(NumberFormatException ex){
            condicion=false;
        }
        
        return condicion;
    }
    
    /*metodo para limpiar los campos de texto*/
    private void limpiarCampos() {

        this.view.txtCodigo.setText("");
        this.view.comboBoxTipoDocumento.setSelectedIndex(0);
        this.view.txtNumDoc.setText("");
        this.view.txtNombre.setText("");
        this.view.txtApellido.setText("");
        this.view.txtDireccion.setText("");
        this.view.txtTelefono.setText("");
        this.view.comboBoxGenero.setSelectedIndex(0);

        this.view.txtCodigo.setEditable(true);
        this.view.btnActualizarEstudiante.setEnabled(false);
        this.view.btnRegistrarEstudiante.setEnabled(true);
        
        this.id=-1;

    }
    
    
    
    /*metodos referente a los eventos inicialmente tenemos un metodo de tipo ActionPerformed
    seguido de los eventos de teclado KeyTyped, keypressed y keyreleased
    */    
    @Override
    public void actionPerformed(ActionEvent e) {

        /*Creamos un objeto de tipo Object que nos permitira verificar el componente al cual estamos accediendo*/
        
        Object evt = e.getSource();
        
        /*validamos el componente activo*/
        if(evt.equals(this.view.btnRegistrarEstudiante)){
            
            try{

                /*verificamos que los campos de texto obligatorios no esten vacios
                de lo contrario imprimimos un mensaje de error*/
                if(!this.view.txtCodigo.getText().isEmpty() && !this.view.txtNombre.getText().isEmpty() 
                   && !this.view.txtApellido.getText().isEmpty()){

                    /*validamos si el codigo ingresado existe en la base de datos
                    si el resultado es falso se procedera con la inserccion de los datos
                    de lo contrario se mostrara un mensaje de error*/

                    if(isNumericValue(view.txtCodigo.getText()) && isNumericValue(view.txtNumDoc.getText())){
    
                        if(modelo.validarCodigo(Integer.parseInt(view.txtCodigo.getText()))){
                        
                            /*creamos un objeto de tipo Estudiante, este lo utilizaremos para almacenar los datos obtenidos de los respectivos campos de texto*/
                            Estudiante estudiante = new Estudiante();

                            /*Realizamos la asignación teniendo en cuenta los respectivos tipos de datos*/
                            estudiante.setCodigo(Integer.parseInt(this.view.txtCodigo.getText()));
                            estudiante.setTipoDocumento(this.view.comboBoxTipoDocumento.getSelectedItem().toString());
                            estudiante.setNumeroDocumento(Integer.parseInt(this.view.txtNumDoc.getText()));
                            estudiante.setNombres(this.view.txtNombre.getText());
                            estudiante.setApellidos(this.view.txtApellido.getText());
                            estudiante.setDireccion(this.view.txtDireccion.getText());
                            estudiante.setTelefono(this.view.txtTelefono.getText());
                            estudiante.setGenero(this.view.comboBoxGenero.getSelectedItem().toString());

                            /*llamamos al metodo crearEstudiante le pasamos como parametro
                            el objeto tipo estudiante y validamos si este es igual a true
                            de los contrario se imprime un mensaje de error*/
                            if(modelo.crearEstudiante(estudiante)){
                                JOptionPane.showMessageDialog(null, "Los datos han sido almacenados exitosamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                                limpiarCampos();
                            }else{
                                JOptionPane.showMessageDialog(null, "Hubo un error en el proceso, por favor intente mas tarde", "ERROR", JOptionPane.ERROR_MESSAGE);                
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "EL codigo ingresado ya existe en la base de datos", "ERROR", JOptionPane.ERROR_MESSAGE);                
                        }
                    
                    }else{
                        JOptionPane.showMessageDialog(null, "Algunos valores ingresados no son permitidos.\n"
                                                            +"Por favor verifique que los valores del codigo y numero de documento sean valores numericos", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "¡Operación fallida!, Debe llenar todos los campos obligatorios", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                
                
            }catch(HeadlessException | NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Error en el proceso "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        
        
        }else if(evt.equals(this.view.btnLimpiarEstudiante)){
            
            limpiarCampos();
        
        }else if(evt.equals(view.btnActualizarEstudiante)){
            
            try{

                /*verificamos que los campos de texto obligatorios no esten vacios
                de lo contrario imprimimos un mensaje de error*/
                if(!view.txtCodigo.getText().isEmpty() && !view.txtNombre.getText().isEmpty() 
                   && !view.txtApellido.getText().isEmpty()){

                    /*creamos un objeto de tipo Estudiante, este lo utilizaremos para almacenar los datos obtenidos de los respectivos campos de texto*/
                    Estudiante estudiante = new Estudiante();

                    /*Realizamos la asignación teniendo en cuenta los respectivos tipos de datos*/
                    estudiante.setCodigo(Integer.parseInt(view.txtCodigo.getText()));
                    estudiante.setTipoDocumento(view.comboBoxTipoDocumento.getSelectedItem().toString());
                    estudiante.setNumeroDocumento(Integer.parseInt(view.txtNumDoc.getText()));
                    estudiante.setNombres(view.txtNombre.getText());
                    estudiante.setApellidos(view.txtApellido.getText());
                    estudiante.setDireccion(view.txtDireccion.getText());
                    estudiante.setTelefono(view.txtTelefono.getText());
                    estudiante.setGenero(view.comboBoxGenero.getSelectedItem().toString());
                    estudiante.setId(id);
                    /*llamamos al metodo crearEstudiante le pasamos como parametro
                    el objeto tipo estudiante y validamos si este es igual a true
                    de los contrario se imprime un mensaje de error*/
                    if(modelo.modificarEstudiante(estudiante)){
                        JOptionPane.showMessageDialog(null, "Los datos han sido actualizados exitosamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                    }else{
                        JOptionPane.showMessageDialog(null, "Hubo un error en el proceso, por favor intente mas tarde", "ERROR", JOptionPane.ERROR_MESSAGE);                
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "¡Operación fallida!, Debe llenar todos los campos obligatorios", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                
                
            }catch(HeadlessException | NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Error en el proceso "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        
        }else if(evt.equals(this.view.btnActualizarTablaFormularioRegistro)){
            
            eliminarTodoFilas(view.tableDatos);
            view.tableDatos.setModel(listarDatos(view.tableDatos));
            
        }else if(evt.equals(this.view.btnRegistrarView)){
        
            this.view.panelMenu.setSelectedIndex(1);
        
        }else if(evt.equals(this.view.btnActualizarView)){
        
            int row = view.tableBusqueda.getSelectedRow();
            
            if(row>-1){

                String data = view.tableBusqueda.getValueAt(row, 0).toString();
                                
                ArrayList<Estudiante> list = modelo.consultarEstudiante("Id", data);
                
                if(list.size()>0){

                    this.view.panelMenu.setSelectedIndex(1);
                    this.view.btnRegistrarEstudiante.setEnabled(false);
                    this.view.btnActualizarEstudiante.setEnabled(true);
                    this.view.txtCodigo.setEditable(false);
                
                    id = list.get(0).getId();
                    view.txtCodigo.setText(String.valueOf(list.get(0).getCodigo()));
                    view.comboBoxTipoDocumento.setSelectedItem(list.get(0).getTipoDocumento());
                    view.txtNumDoc.setText(String.valueOf(list.get(0).getNumeroDocumento()));
                    view.txtNombre.setText(list.get(0).getNombres());
                    view.txtApellido.setText(list.get(0).getApellidos());
                    view.txtTelefono.setText(list.get(0).getTelefono());
                    view.comboBoxGenero.setSelectedItem(list.get(0).getGenero());
                    view.txtDireccion.setText(list.get(0).getDireccion());
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Hubo un error en el proceso", "Error", JOptionPane.ERROR_MESSAGE);
                }
            
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una celda valida", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
                    
        }else if(evt.equals(this.view.btnEliminar)){

            int row = view.tableBusqueda.getSelectedRow();
            
            if(row>-1){
                
                int confirmar = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar el registro?");

                if(confirmar==JOptionPane.YES_OPTION){

                    id = Integer.parseInt(view.tableBusqueda.getValueAt(row, 0).toString());

                    if(modelo.eliminarEstudiante(id)){
                        JOptionPane.showMessageDialog(null, "El registro ha sido eliminado de manera exitosa", "Información", JOptionPane.INFORMATION_MESSAGE);

                        id=-1;
                        eliminarTodoFilas(view.tableBusqueda);
                        view.tableBusqueda.setModel(listarDatos(view.tableBusqueda));


                    }else{
                        JOptionPane.showMessageDialog(null, "Hubo un error en el proceso", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "Seleccione una celda valida", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            
        
        }else if(evt.equals(view.btnBuscarConsulta)){
            
            String filtro = view.comboBoxTipoBusqueda.getSelectedItem().toString();
            String busqueda = view.textBusqueda.getText();
            
            ArrayList<Estudiante> list = modelo.consultarEstudiante(filtro, busqueda);
            if(list.size()>0){
                eliminarTodoFilas(view.tableBusqueda);
                view.tableBusqueda.setModel(listarDatos(view.tableBusqueda));
            }else{
                JOptionPane.showMessageDialog(null, "No se obtuvo resultado de la consulta", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
            }

                
            
        }



    }

    @Override
    public void keyTyped(KeyEvent e) {
        
        /*Creamos un objeto de tipo Object que nos permitira verificar el componente al cual estamos accediendo*/
        
        Object evt = e.getSource();
        
        /*validamos el componente activo*/
        if(evt.equals(this.view.txtCodigo)){
            
            /*Este campo solo permitira el ingreso de valores numericos por lo tantos debemos
            verificar si el caracter ingresado es un valor numerico, para esto creamos una variable
            de tipo char y le vamos asignar el valor ingresado por teclado haciendo uso del metodo
            getKeyChar()*/
            
            char caracter = e.getKeyChar();
            
            /*validamos si el caracter es un valor numerico haciendo uso del metodo isDigit
            en caso que no sea un valor numerico se detendra el proceso haciendo uso del metodo
            consume*/
            if(!Character.isDigit(caracter)){
                e.consume();
            }
            
        }else if(evt.equals(this.view.txtNumDoc)){
            
            char caracter = e.getKeyChar();
            if(!Character.isDigit(caracter)){
                e.consume();
            }
        
        }
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }





    
}
