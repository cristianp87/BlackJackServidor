
import entidades.Carta;
import entidades.Cliente;
import entidades.Juego;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SocksServer {

    private ServerSocket server;
    private Socket cliente;
    private int puerto;
    public static ArrayList<Cliente> listaCliente;
    public static Juego juego1;
    public static Juego juego2;
    public static ArrayList<Carta> mazo;

    public SocksServer() throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("recursos/archivos/configuracion/configuracion");
        this.puerto = Integer.parseInt(rb.getString("puerto"));
        server = new ServerSocket(puerto);
        listaCliente = new ArrayList<>();
        juego1 = new Juego();
        juego2 = new Juego();
    }

    public void iniciar() throws IOException {
        while (true) {
            System.out.println("Esperando cliente");
            this.cliente = server.accept();
            ClienteServidor cs = new ClienteServidor(cliente);
            cs.start();
        }
    }

    public static void main(String[] args) throws IOException {
        SocksServer ss = new SocksServer();
        ss.iniciar();

    }

}
