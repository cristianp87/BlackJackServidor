/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logica;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
/**
 *
 * @author CristianPc
 */
public class SocketServidor {
    public static HashMap listaClientes;
    
    public void iniciar() throws IOException{
        Socket cl;
        ServerSocket ss;
        ss=new ServerSocket(3333);
        ClienteServidor nuevoCliente;
        listaClientes = new HashMap(); 
        
        while (true)
        {
            System.out.println("Esperando cliente");
            cl=ss.accept();
            nuevoCliente=new ClienteServidor(cl);
            nuevoCliente.start();
        }        
    }
}
