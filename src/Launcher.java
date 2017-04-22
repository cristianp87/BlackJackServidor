
import java.io.IOException;
import javax.swing.text.BadLocationException;
import logica.SocketServidor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import logica.SocketServidor;
/**
 *
 * @author CristianPc
 */
public class Launcher {

    private SocketServidor socketServidor;
    
    public Launcher() throws BadLocationException, IOException{
        socketServidor = new SocketServidor();
        socketServidor.iniciar();        
    }
    
    public static void main(String[] args) throws BadLocationException, IOException {
        new Launcher();
    } 
    
}
