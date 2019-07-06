package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */

/*Clase Conexion la cual contendra dos metodos que permitiran crear y cerrar una conexion con la
base de datos*/
public class Conexion {
        
    /*Creamos las respectivas variables con los datos requeridos para la conexion
      variable DB, esta le asignaremos el nombre de la base de datos en este caso seria 'estudiante'
      variable URL, en esta ira la direccion a la cual accederemos para establecer 
                    la conexion en este caso sera de manera local por lo tanto utilizamos localhost
                    seguido del puerto que utilizaremos, el puerto por defecto es el 3306, igualmente
                    debemos acceder a la base de datos por lo tanto utilizamos la variable DB
                    y cargamos otras configuraciones para evitar errores en los caracteres entre otras cosas
      variable USER, asignamos el nombre de usuario en este caso utilizaremos el usuario root que es el que viene por defecto
      variable PASS, asignamos la contraseña para acceder a la base de datos, para este caso no requiere contraseña
    */
    private final String DB="estudiante";
    private final String URL="jdbc:mysql://localhost:3306/"+DB+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String USER="root";
    private final String PASS="";    

    /*Creamos una funcion de tipo Connection la cual nos permitira establecer una conexion con la base de datos*/
    public Connection conectar(){
    
        /*Creamos un objeto de tipo Connection y los inicializamos en null*/
        Connection connect = null;
        
        /*Creamos un bloque de tipo try/catch en el cual cargamos el driver a utilizar
        y obtenemos la conexion haciendo uso de las variables anteriormente creadas las cuales son
        URL, USER, PASS*/
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(URL, USER, PASS);
            
        }catch(SQLException ex ){
            System.out.println(ex.getMessage());        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connect;
        
    }    


    /* metodo que nos permitira cerrar la conexion para esto pasamos como parametro el objeto de 
    tipo Connection al cual deseamos terminar la conexion, para cerrar la misma debemos hacer uso 
    del metodo close() */
    public void cerrar(Connection con){
        
        try {  
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    
    }
    
    
    
}
