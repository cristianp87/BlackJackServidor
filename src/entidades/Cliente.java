/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author jomorenoro
 */
public class Cliente {
    private Socket socket;
    private String idusuario;
    private ArrayList<Carta> listaCarta;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }



    public ArrayList<Carta> getListaCarta() {
        return listaCarta;
    }

    public void setListaCarta(ArrayList<Carta> listaCarta) {
        this.listaCarta = listaCarta;
    }
    
}
