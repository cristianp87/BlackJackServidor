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
    private DataOutputStream dos2;
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
                e.printStackTrace();
                bandera = false;
            }
        }
    }

    public void leeMensaje() throws IOException {
        Juego juego = getLogica().convierteMensaje(mensaje);
        switch (juego.getComando()) {
            case "REG":
                //añade a la lista de clientes
                SocksServer.listaCliente.add(getLogica().verificaJugadores(juego, clienteServer));
                if (SocksServer.listaCliente.size() == 1) {
                    SocksServer.juego1 = juego;
                }
                if (SocksServer.listaCliente.size() == 2) {
                    SocksServer.juego2 = juego;
                }
                if (SocksServer.mazo == null) {
                    getLogica().llenaMazo();
                } else {
                    getLogica().setMazo(SocksServer.mazo);
                }
                if (SocksServer.listaCliente.size() == 2) {
                    getLogica().setJugador1(SocksServer.juego1);
                    getLogica().setJugador2(SocksServer.juego2);
                    getLogica().iniciaJuego();
                    SocksServer.juego1 = getLogica().getJugador1();
                    SocksServer.juego2 = getLogica().getJugador2();
                    SocksServer.mazo = getLogica().getMazo();
                    enviaMensajeClientes();
                }
                break;
            case "PED":
                getLogica().setMazo(SocksServer.mazo);
                getLogica().setSuma(0);
                if (SocksServer.listaCliente.get(0).getIdusuario().equalsIgnoreCase(juego.getIdUsuario())) {
                    SocksServer.juego1 = juego;
                    getLogica().setJugador1(SocksServer.juego1);
                    getLogica().setJugador2(SocksServer.juego2);
                    getLogica().iniciaPideCarta();
                }
                if (SocksServer.listaCliente.get(1).getIdusuario().equalsIgnoreCase(juego.getIdUsuario())) {
                    SocksServer.juego2 = juego;
                    getLogica().setJugador2(SocksServer.juego2);
                    getLogica().setJugador1(SocksServer.juego1);
                    getLogica().iniciaPideCartaJug2();

                }
                if (getLogica().isTerminaJuego()) {
                    SocksServer.listaCliente.get(0).setEstado("I");
                    SocksServer.listaCliente.get(1).setEstado("I");
                }
                SocksServer.mazo = getLogica().getMazo();
                enviaMensajeClientes();
                break;

            case "PLA":
                if (SocksServer.listaCliente.get(0).getIdusuario().equalsIgnoreCase(juego.getIdUsuario())) {
                    SocksServer.juego1 = juego;
                    getLogica().setJugador1(SocksServer.juego1);
                    getLogica().setJugador2(SocksServer.juego2);
                    getLogica().plantarseJug1();
                }
                if (SocksServer.listaCliente.get(1).getIdusuario().equalsIgnoreCase(juego.getIdUsuario())) {
                    SocksServer.juego2 = juego;
                    getLogica().setJugador1(SocksServer.juego1);
                    getLogica().setJugador2(SocksServer.juego2);
                    getLogica().plantarseJug2();
                    SocksServer.mazo = null;
                    SocksServer.listaCliente.get(0).setEstado("I");
                    SocksServer.listaCliente.get(1).setEstado("I");

                }
                enviaMensajeClientes();
                break;

            case "NUE":
                if (juego.getIdUsuario().equalsIgnoreCase(SocksServer.listaCliente.get(0).getIdusuario())) {
                    SocksServer.listaCliente.get(0).setEstado("A");
                }
                if (juego.getIdUsuario().equalsIgnoreCase(SocksServer.listaCliente.get(1).getIdusuario())) {
                    SocksServer.listaCliente.get(1).setEstado("A");
                }

                if (SocksServer.listaCliente.get(0).getEstado().equalsIgnoreCase("A") && SocksServer.listaCliente.get(1).getEstado().equalsIgnoreCase("A")) {
                    if (SocksServer.mazo == null) {
                        getLogica().llenaMazo();
                    } else {
                        getLogica().setMazo(SocksServer.mazo);
                    }
                    getLogica().setJugador1(SocksServer.juego1);
                    getLogica().setJugador2(SocksServer.juego2);
                    getLogica().iniciaJuego();
                    SocksServer.juego1 = getLogica().getJugador1();
                    SocksServer.juego2 = getLogica().getJugador2();
                    SocksServer.mazo = getLogica().getMazo();
                    enviaMensajeClientes();
                }
                break;
        }
    }

    public void enviaMensajeClientes() throws IOException {
        dos = new DataOutputStream(SocksServer.listaCliente.get(0).getSocket().getOutputStream());
        dos.writeUTF(getLogica().convierteObjetoJuego(SocksServer.juego1));
        dos2 = new DataOutputStream(SocksServer.listaCliente.get(1).getSocket().getOutputStream());
        dos2.writeUTF(getLogica().convierteObjetoJuego(SocksServer.juego2));
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
