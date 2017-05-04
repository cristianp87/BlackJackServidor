/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import entidades.Juego;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import logica.Logica;

/**
 *
 * @author jomorenoro
 */
public class ClienteServidor extends Thread {

    private Socket clienteServer;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String mensaje;
    private Logica logica;

    public ClienteServidor(Socket clienteServer) throws IOException {
        this.clienteServer = clienteServer;
        this.dis = new DataInputStream(clienteServer.getInputStream());
        this.dos = new DataOutputStream(clienteServer.getOutputStream());
    }

    @Override
    public void run() {
        boolean bandera = true;
        while (bandera) {
            try {
                mensaje = "";
                mensaje = dis.readUTF();
                leeMensaje();
            } catch (Exception e) {
                bandera = false;
            }
        }
    }

    public void leeMensaje() throws IOException {

        Juego juego = getLogica().convierteMensaje(mensaje);
        switch (juego.getComando()) {
            case "REG":
                SocksServer.listaCliente.add(getLogica().verificaJugadores(juego, clienteServer));
                if (SocksServer.listaCliente.size() == 1) {
                    SocksServer.juego1 = juego;
                }
                if (SocksServer.listaCliente.size() == 2) {
                    SocksServer.juego2 = juego;
                }

                if (SocksServer.listaCliente.size() == 2) {
                    SocksServer.listaCliente.get(0).setListaCarta(getLogica().reparteCartasInicio());
                    SocksServer.listaCliente.get(1).setListaCarta(getLogica().reparteCartasInicio());
                    SocksServer.juego1.setCartasEnemigo(SocksServer.listaCliente.get(1).getListaCarta());
                    SocksServer.juego1.setComando("JUG");
                    SocksServer.juego1.setEstado("A");
                    SocksServer.juego1.setJuego(SocksServer.listaCliente.get(0).getListaCarta());
                    SocksServer.juego1.setNombreJugadorEnemigo(SocksServer.juego2.getNombreJugador());
                    SocksServer.juego2.setCartasEnemigo(SocksServer.listaCliente.get(0).getListaCarta());
                    SocksServer.juego2.setComando("REG");
                    SocksServer.juego2.setEstado("A");
                    SocksServer.juego2.setJuego(SocksServer.listaCliente.get(0).getListaCarta());
                    SocksServer.juego2.setNombreJugadorEnemigo(SocksServer.juego1.getNombreJugador());
                    dos = new DataOutputStream(SocksServer.listaCliente.get(0).getSocket().getOutputStream());
                    dos.writeUTF(getLogica().convierteObjetoJuego(SocksServer.juego1));
                    dos = new DataOutputStream(SocksServer.listaCliente.get(1).getSocket().getOutputStream());
                    dos.writeUTF(getLogica().convierteObjetoJuego(SocksServer.juego2));
                }
                break;
            case "PED":
                if (SocksServer.listaCliente.get(0).getIdusuario().equalsIgnoreCase(juego.getIdJugador())) {
                    SocksServer.juego1 = juego;
                    SocksServer.juego1.setJuego(getLogica().pideCarta(SocksServer.juego1.getJuego()));
                    SocksServer.juego2.setCartasEnemigo(SocksServer.juego1.getJuego());
                }
                if (SocksServer.listaCliente.get(1).getIdusuario().equalsIgnoreCase(juego.getIdJugador())) {
                    SocksServer.juego2 = juego;
                    SocksServer.juego2.setJuego(getLogica().pideCarta(SocksServer.juego2.getJuego()));
                    SocksServer.juego1.setCartasEnemigo(SocksServer.juego2.getJuego());
                }
                dos = new DataOutputStream(SocksServer.listaCliente.get(0).getSocket().getOutputStream());
                dos.writeUTF(getLogica().convierteObjetoJuego(SocksServer.juego1));
                dos = new DataOutputStream(SocksServer.listaCliente.get(1).getSocket().getOutputStream());
                dos.writeUTF(getLogica().convierteObjetoJuego(SocksServer.juego2));
                break;
        }
    }

    public Logica getLogica() {
        if (logica == null) {
            logica = new Logica();
        }
        return logica;
    }

    public void setLogica(Logica logica) {
        this.logica = logica;
    }

}
