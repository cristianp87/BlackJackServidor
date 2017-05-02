/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entidades.Carta;
import entidades.Juego;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 *
 * @author jomorenoro
 */
public class Logica {

    private HashMap listaUsuario;

    public Juego convierteMensaje(String mensaje) {
        System.out.println("Mensaje"+mensaje);
        Juego juego = new Juego();
        StringTokenizer st = new StringTokenizer(mensaje, "|");
        while (st.hasMoreElements()) {
            juego.setComando(st.nextToken());
            juego.setIdUsuario(st.nextToken());
            juego.setNombreJugador(st.nextToken());
            String listaCartas = st.nextToken();
            try {
                juego.setJuego(convierteListaCartas(listaCartas));
            } catch (Exception e) {
                System.out.println("Cartas vacias");
            }
            juego.setEstado(st.nextToken());
            juego.setIdUsuarioEnemigo(st.nextToken());
            juego.setNombreJugadorEnemigo(st.nextToken());
            String listaCartasEnemigo = st.nextToken();
            try {
                juego.setCartasEnemigo(convierteListaCartas(listaCartasEnemigo));
            } catch (Exception e) {
                System.out.println("Cartas vacias");
            }
           

        }
        return juego;
    }

    public String convierteObjetoJuego(Juego objeto) {
        String mensaje = "";
        mensaje += objeto.getComando().concat("|").concat(objeto.getIdUsuario().concat("|"))
                .concat(objeto.getNombreJugador().concat("|")).concat(recorreArray(objeto.getJuego())).concat("|")
                .concat(objeto.getEstado().concat("|")).concat(objeto.getIdUsuarioEnemigo()).concat("|").concat(objeto.getNombreJugadorEnemigo())
                .concat("|").concat(recorreArray(objeto.getCartasEnemigo()));
        return mensaje;
    }

    public ArrayList<Carta> convierteListaCartas(String mensajeCartas) {
        ArrayList<Carta> listaCartas = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(mensajeCartas, ",");
        while (st.hasMoreElements()) {
            String mensajeCarta = st.nextToken();
            listaCartas.add(convierteMensajeCarta(mensajeCarta));
        }
        return listaCartas;
    }

    public Carta convierteMensajeCarta(String mensajeCarta) {
        Carta c = new Carta();
        StringTokenizer st = new StringTokenizer(mensajeCarta, "%");
        while (st.hasMoreElements()) {
            c.setNombreCarta(st.nextToken());
            c.setValorCarta(st.nextToken());
            c.setEstadoCarta(st.nextToken());
        }
        return c;
    }

    public String recorreArray(ArrayList<Carta> listaCartas) {
        String mensaje = "";
        for (Carta item : listaCartas) {
            mensaje += mensaje.concat(item.getNombreCarta().concat("%")).concat(item.getValorCarta().concat("%")).concat(item.getEstadoCarta().concat(","));
        }
        return mensaje;
    }

    public HashMap getListaUsuario() {
        if (listaUsuario == null) {
            listaUsuario = new HashMap();
        }
        return listaUsuario;
    }

    public void setListaUsuario(HashMap listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

}
