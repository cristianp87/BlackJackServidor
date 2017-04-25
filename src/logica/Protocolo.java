/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

/**
 *
 * @author CristianPc
 */

 /*
    ***Lista de comandos***
    REG = Pregunta si ya existe un usuario registrado para ese identUser 
    PUC = Pregunta si el Usuario esta Conectado
          PARAMETROS:
          XXX -> Codigo del usuario al que se le va a averiguar el estado
          RESPUESTAS:
          0 ->  No conectado.
                PARAMETROS:
                YYYYMMDDHHMMSS -> Ultima fecha de conexion 
          1 ->  En linea
          2 ->  No registrado
    INJ = Inicia Juego
          PARAMETROS:
          XXX -> Codigo del usuario al que se le va a averiguar el estado
          RESPUESTAS:
          0 ->  No conectado.
                PARAMETROS:
                YYYYMMDDHHMMSS -> Ultima fecha de conexion 
          1 ->  En linea
    PC = Pedir Carta
          PARAMETROS:
          XXX -> Codigo del usuario al que se debe enviar el mensaje
          XXX.... -> Mensaje
        RESPUESTAS:
          0 ->  No conectado.
                PARAMETROS:
                YYYYMMDDHHMMSS -> Ultima fecha de conexion 
          1 ->  En linea
    PLJ = Plantar Jugador
        PARAMETROS:
          XXX -> Codigo del usuario al que se debe enviar el mensaje
          XXX.... -> Mensaje

    NPC = No Pedir Carta
          PARAMETROS:
          X -> Estado entrega del mensaje
               Posibles valores:
                0 ->Receptor no conectado
                1 ->Receptor conectado sin leer mensaje
                2 ->Receptor conectado con mensaje leido
          XXX.... -> Mensaje
    IEJ = Informar estado Jugador
          PARAMETROS:
          X -> Estado
               Posibles valores:
               0 ->  No conectado.
                     PARAMETROS:
                     YYYYMMDDHHMMSS -> Ultima fecha de conexion 
               1 ->  En linea
    */

public class Protocolo {
    private String identUserConect; 
    private String mensajeUser;
    private String encabezado;
    private String comando;
    private String identUserRecep;
    private String estadoRecepMen;
    private String estado;
    
    public String leerMensaje(String mensaje)
    {
        setEncabezado(mensaje.substring(0,4));       
        setIdentUserConect(mensaje.substring(18,21));        
        setComando(mensaje.substring(21,24));
        
        if(getComando().equals("INJ")) 
        {
            this.setIdentUserRecep(mensaje.substring(24,27));
            mensajeUser=mensaje.substring(27,mensaje.length());             
        }else
        if(getComando().equals("PC")) 
        {
            this.setIdentUserRecep(mensaje.substring(24,27));
            mensajeUser=mensaje.substring(27,mensaje.length());             
        }else
        if(getComando().equals("NPC")) {
            this.setEstadoRecepMen(mensaje.substring(24,25));
            mensajeUser=mensaje.substring(25,mensaje.length());  
            
        }else
        if(getComando().equals("IEL")) {
            this.setIdentUserRecep(mensaje.substring(24,27));        
        }else 
        if(getComando().equals("IEJ")) {
            this.setIdentUserRecep(mensaje.substring(24,27));        
        }    
        
        return getComando();
    }

    /**
     * Set's y Get's
     */

    public String getMensajeUser() {
        return mensajeUser;
    }
    public void setMensajeUser(String mensajeUser) {
        this.mensajeUser = mensajeUser;
    }
    public String getEncabezado() {
        return encabezado;
    }
    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }
    
    public String getComando() {
        return comando;
    }
    public void setComando(String comando) {
        this.comando = comando;
    }
    public String getIdentUserConect() {
        return identUserConect;
    }
    public void setIdentUserConect(String identUserConect) {
        this.identUserConect = identUserConect;
    }
    public String getIdentUserRecep() {
        return identUserRecep;
    }
    public void setIdentUserRecep(String identUserRecep) {
        this.identUserRecep = identUserRecep;
    }
    public String getEstadoRecepMen() {
        return estadoRecepMen;
    }
    public void setEstadoRecepMen(String estadoRecepMen) {
        this.estadoRecepMen = estadoRecepMen;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
