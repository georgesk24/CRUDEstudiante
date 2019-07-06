package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class EstudianteDAO {
    
    /*Creamos los objetos requeridos para poder administrar las operaciones
    con la base de datos
    
    inicialmente creamos un objeto de tipo Conexion(conexion), este nos permitira crear una conexion con la base de datos 
    creamos un objeto de tipo Connection(conectar), este nos permitirá administrar la conexion que se obtenga con la base de datos
    creamos un objeto de tipo PreparedStatement(pst), lo utilizaremos para preparar la sentencia sql y agregar los parametros requeridos
    creamos un objeto de tipo ResultSet(rs), lo utilizaremos para obtener los respectivos registros alojados en la base de datos 
    */   
    private final Conexion conexion;
    private Connection conectar;
    private PreparedStatement pst;
    private ResultSet rs;
    
    /*Creamos el metodo constructor de la clase y dentro de esto inicializamos el objeto de tipo Conexion(conexion)*/
    public EstudianteDAO(){
        conexion=new Conexion();
    }
    
    /*Creamos un funcion de tipo boolean este nos permitira guardar la información obtenida en la base de datos*/
    /*La función recibira como parametro un objeto de tipo Estudiante*/
    public boolean crearEstudiante(Estudiante estudiante){
        
        /*variable tipo boolean, este cambiará dependiendo el resultado del proceso a realizar*/
        boolean condicion = false;
        
        /*Creamos un bloque de tipo try/catch/finally
        la sección del try agregamos las lineas de codigo a ejecutar
        la seccion del catch capturaremos los posibles errores que surgan en la seccion del try
        la seccion del finally cerraremos la conexion con la base de datos
        */
        try{
            
            /*Asignamos al objeto tipo Connection la conexion que obtengamos con la base de datos
            */
            this.conectar = conexion.conectar();
            
            /*validamos si el objeto de tipo Conecction es diferente de null*/
            if(this.conectar!=null){
                
                /*Creamos una variable tipo String(sql), esta contendra la sentencia sql*/
                String sql="INSERT INTO "
                            + "estudiante(codigo, tipo_documento, numero_documento, nombres, apellidos, direccion, telefono, genero) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";;
                
                /*Preparamos la sentencia haciendo uso del objeto tipo PreparedStatement*/
                pst = conectar.prepareStatement(sql);
                
                /*agregamos los parametros requeridos
                y pasamos los valores que estan contenidos en el objeto tipo estudiante*/

                pst.setInt(1, estudiante.getCodigo());
                pst.setString(2, estudiante.getTipoDocumento());
                pst.setInt(3, estudiante.getNumeroDocumento());
                pst.setString(4, estudiante.getNombres());
                pst.setString(5, estudiante.getApellidos());
                pst.setString(6, estudiante.getDireccion());
                pst.setString(7, estudiante.getTelefono());
                pst.setString(8, estudiante.getGenero());
                
                /*agregamos el resultado final a la variable tipo boolean
                ejecutamos el metodo executeUpdate si este es mayor que 0
                devuelve true de lo contrario false*/
                condicion= (pst.executeUpdate()>0);
                
            }else{
                System.out.println("Error al conectar con la base de datos");
            }
            
        }catch(SQLException ex){
        
            System.out.println(ex.getMessage());
        
        }finally{
            
            try{
                if(this.conectar!=null){
                    this.conexion.cerrar(conectar);
                }
            }catch(Exception ex){
            }
        
        }
        
        
        
        return condicion;
    
    }


    public boolean modificarEstudiante(Estudiante estudiante){
        
        boolean condicion = false;

        /*Creamos un bloque de tipo try/catch/finally
        la sección del try agregamos las lineas de codigo a ejecutar
        la seccion del catch capturaremos los posibles errores que surgan en la seccion del try
        la seccion del finally cerraremos la conexion con la base de datos
        */
        try{
            
            /*Asignamos al objeto tipo Connection la conexion que obtengamos con la base de datos
            */
            this.conectar = conexion.conectar();
            
            /*validamos si el objeto de tipo Conecction es diferente de null*/
            if(this.conectar!=null){
                
                /*Creamos una variable tipo String(sql), esta contendra la sentencia sql*/
                String sql="UPDATE estudiante "
                            + "SET tipo_documento=?, numero_documento=?, nombres=?, apellidos=?, direccion=?, telefono=?, genero=? "
                            + " WHERE id=?";
                
                /*Preparamos la sentencia haciendo uso del objeto tipo PreparedStatement*/
                pst = conectar.prepareStatement(sql);
                
                
                /*agregamos los parametros requeridos
                y pasamos los valores que estan contenidos en el objeto tipo estudiante*/

                pst.setString(1, estudiante.getTipoDocumento());
                pst.setInt(2, estudiante.getNumeroDocumento());
                pst.setString(3, estudiante.getNombres());
                pst.setString(4, estudiante.getApellidos());
                pst.setString(5, estudiante.getDireccion());
                pst.setString(6, estudiante.getTelefono());
                pst.setString(7, estudiante.getGenero());
                pst.setInt(8, estudiante.getId());
                
                /*agregamos el resultado final a la variable tipo boolean
                ejecutamos el metodo executeUpdate si este es mayor que 0
                devuelve true de lo contrario false*/
                condicion= (pst.executeUpdate()>0);
                
            }else{
                System.out.println("Error al conectar con la base de datos");
            }
            
        }catch(SQLException ex){
        
            System.out.println(ex.getMessage());
        
        }finally{
            
            try{
                if(this.conectar!=null){
                    this.conexion.cerrar(conectar);
                }
            }catch(Exception ex){
            }
        
        }
        
        
        
        return condicion;
    
    }    
    
    public ArrayList<Estudiante> consultarEstudiante(String filtro, String busqueda){
        
        /*creamos un objeto de tipo ArrayList al cual le asignaremos los 
        registros que obtengamos de la consulta a la base de datos.
        igualmente creamos un objeto de tipo estudiante el cual 
        recogera los datos registro por registro*/
        ArrayList list = new ArrayList();
        Estudiante estudiante;
        
        try{
            
            /*obtenemos la conexion con la base de datos*/
            conectar = conexion.conectar();
            
            /*validamos si la conexion es valida*/
            if(conectar != null){
                
                /*creamos una variable tipo String la cual contendra la sentencia sql*/
                String sql;
            
                /*utilizaremos la estructura de control de tipo switch
                la cual tendra una consulta sql distinta dependiendo el filtro
                o tipo de busqueda, para esto haremos uso del parametro tipo String
                filtro.*/
                switch(filtro){
                
                    case "Todo":
                        sql = "SELECT * FROM estudiante";
                        pst = conectar.prepareStatement(sql);
                    break;
                    
                    case "Número Documento":
                        sql = "SELECT * FROM estudiante WHERE numero_documento=?";                        
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, busqueda);
                    break;
                    
                    case "Nombre/Apellido":
                        sql = "SELECT * FROM estudiante WHERE nombres LIKE ? OR apellidos LIKE ?";                        
                        pst = conectar.prepareStatement(sql);
                        pst.setString(1, "%"+busqueda+"%"); 
                        pst.setString(2, "%"+busqueda+"%");                                                
                    break;
                    
                    case "Genero":
                        sql = "SELECT * FROM estudiante WHERE genero=?";
                        pst = conectar.prepareStatement(sql);                        
                        pst.setString(1, busqueda);                         
                    break;

                    case "Id":
                        sql = "SELECT * FROM estudiante WHERE id=?";
                        pst = conectar.prepareStatement(sql);                        
                        pst.setString(1, busqueda);                         
                    break;
                    
                    default:
                        sql = "SELECT * FROM estudiante";    
                        pst = conectar.prepareStatement(sql);                        
                    break;
                
                }
                
                /*asignamos al objeto tipo resulset el resultado obtenido 
                de la consulta realizada a la base de datos
                para esto accedemos al metodo executeQuery*/
                rs = pst.executeQuery();
                
                /*Creamos un ciclo para obtener los respectivos registros*/
                while(rs.next()){
                    
                    /*inicializamos el objeto tipo Estudiante*/
                    estudiante = new Estudiante();
                
                    /*asignamos los respectivos valores obtenidos al objeto*/
                    estudiante.setId(rs.getInt("id"));
                    estudiante.setCodigo(rs.getInt("codigo"));
                    estudiante.setTipoDocumento(rs.getString("tipo_documento"));
                    estudiante.setNumeroDocumento(rs.getInt("numero_documento"));
                    estudiante.setNombres(rs.getString("nombres"));
                    estudiante.setApellidos(rs.getString("apellidos"));
                    estudiante.setDireccion(rs.getString("direccion"));
                    estudiante.setTelefono(rs.getString("telefono"));
                    estudiante.setGenero(rs.getString("genero"));
                    
                    /*asignamos el objeto tipo Estudiante al ArrayList*/
                    list.add(estudiante);
                    
                }
                
            }else{}
            
        }catch(SQLException ex){
        
            System.out.println(ex.getMessage());
        
        }finally{
            try{
                if(this.conectar!=null){
                    this.conexion.cerrar(conectar);
                }
            }catch(Exception ex){}
        }
        
        
        return list;
        
    }
    
    
    public boolean eliminarEstudiante(int id){
        
        boolean condicion = false;

        /*Creamos un bloque de tipo try/catch/finally
        la sección del try agregamos las lineas de codigo a ejecutar
        la seccion del catch capturaremos los posibles errores que surgan en la seccion del try
        la seccion del finally cerraremos la conexion con la base de datos
        */
        try{
            
            /*Asignamos al objeto tipo Connection la conexion que obtengamos con la base de datos
            */
            this.conectar = conexion.conectar();
            
            /*validamos si el objeto de tipo Conecction es diferente de null*/
            if(this.conectar!=null){
                
                /*Creamos una variable tipo String(sql), esta contendra la sentencia sql*/
                String sql="DELETE FROM estudiante WHERE id=?";
                
                /*Preparamos la sentencia haciendo uso del objeto tipo PreparedStatement*/
                pst = conectar.prepareStatement(sql);

                /*agregamos los parametros requeridos
                y pasamos los valores que estan contenidos en el objeto tipo estudiante*/
                pst.setInt(1, id);
                
                /*agregamos el resultado final a la variable tipo boolean
                ejecutamos el metodo executeUpdate si este es mayor que 0
                devuelve true de lo contrario false*/
                condicion= (pst.executeUpdate()>0);
                
            }else{
                System.out.println("Error al conectar con la base de datos");
            }
            
        }catch(SQLException ex){
        
            System.out.println(ex.getMessage());
        
        }finally{
            
            try{
                if(this.conectar!=null){
                    this.conexion.cerrar(conectar);
                }
            }catch(Exception ex){
            }
        
        }
        
        return condicion;
    }
    
    
    public boolean validarCodigo(int codigo){
    
        boolean condicion = false;
        
        try{
            
            this.conectar = conexion.conectar();
            
            if(this.conectar!=null){
                
                String sql="SELECT * FROM estudiante WHERE codigo = ? ";
                pst = conectar.prepareStatement(sql);
                pst.setInt(1, codigo);
                
                rs = pst.executeQuery();
                
                condicion = rs.next();
                
            }else{
                System.out.println("Error al conectar con la base de datos");
            }
            
        }catch(SQLException ex){
        
            System.out.println(ex.getMessage());
        
        }finally{
            
            try{
                if(this.conectar!=null){
                    this.conexion.cerrar(conectar);
                }
            }catch(Exception ex){
            }
        
        }
        
        return condicion;
    
    }
    
    
}
