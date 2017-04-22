/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.net.Socket;
/**
 *
 * @author CristianPc
 */
public class Clientes {
    private Socket socket;
    private String estado;
    private Cartas[] strCartas;

    public Cartas[] getStrCartas() {
        return strCartas;
    }

    public void setStrCartas(Cartas[] strCartas) {
        this.strCartas = strCartas;
    }
    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
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
