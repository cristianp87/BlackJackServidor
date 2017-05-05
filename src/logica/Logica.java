/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import entidades.Carta;
import entidades.Cliente;
import entidades.Juego;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author jomorenoro
 */
public class Logica {

    private ArrayList<Cliente> jugadores;
    private ArrayList<Carta> mazo;
    private int suma;

    public Juego convierteMensaje(String mensaje) {
        System.out.println("Mensaje" + mensaje);
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

    public String convierteObjetoJuego(Juego objeto) {
        String mensaje = "";
        mensaje += objeto.getComando().concat("|").concat(objeto.getIdUsuario().concat("|"))
                .concat(objeto.getNombreJugador().concat("|")).concat(recorreArray(objeto.getJuego())).concat("|")
                .concat(objeto.getEstado().concat("|")).concat(objeto.getIdUsuarioEnemigo()).concat("|").concat(objeto.getNombreJugadorEnemigo())
                .concat("|").concat(recorreArray(objeto.getCartasEnemigo()));
        System.out.println("mensaje" + mensaje);
        return mensaje;
    }

    public String recorreArray(ArrayList<Carta> listaCartas) {
        String mensaje = "";
        for (Carta item : listaCartas) {
            mensaje = mensaje.concat(item.getNombreCarta().concat("%")).concat(item.getValorCarta().concat("%")).concat(item.getEstadoCarta().concat(","));
        }
        if ("".equalsIgnoreCase(mensaje)) {
            mensaje = "null";
        }
        return mensaje;
    }

    public Cliente verificaJugadores(Juego juego, Socket sk) {
        boolean existeJugador = false;
        Cliente clien = new Cliente();
        if (!getJugadores().isEmpty()) {
            for (Cliente cl : jugadores) {
                if (juego.getIdJugador().equalsIgnoreCase(cl.getIdusuario())) {
                    System.out.println("Ya existe el jugador");
                    existeJugador = true;
                    break;
                }
            }
        }
        if (!existeJugador) {
            clien.setIdusuario(juego.getIdUsuario());
            clien.setSocket(sk);
            getJugadores().add(clien);
        }
        return clien;
    }

    public ArrayList<Carta> reparteCartasInicio() {
        ArrayList<Carta> carta = new ArrayList<>();
        Random random = new Random();
        int index = random.nextInt(mazo.size());
        carta.add(mazo.get(index));
        mazo.remove(index);
        index = random.nextInt(mazo.size());
        mazo.get(index).setEstadoCarta("D");
        carta.add(mazo.get(index));
        mazo.remove(index);
        return carta;
    }

    public ArrayList<Carta> pideCarta(ArrayList<Carta> cartas) {
        Random random = new Random();
        int index = random.nextInt(mazo.size());
        mazo.get(index).setEstadoCarta("D");
        cartas.add(mazo.get(index));
        mazo.remove(index);
        return cartas;
    }

    public void llenaMazo() {
        if (mazo == null) {
            mazo = new ArrayList<>();
        }
        llenaPinta("corazones/");
        llenaPinta("diamantes/");
        llenaPinta("picas/");
        llenaPinta("trebol/");

    }

    public void llenaPinta(String pinta) {
        for (int i = 2; i <= 11; i++) {
            Carta c = new Carta();
            if (i == 10) {
                c.setNombreCarta(pinta + i);
                c.setValorCarta("" + i);
                c.setEstadoCarta("T");
                mazo.add(c);
                c.setNombreCarta(pinta + "J");
                c.setValorCarta("" + i);
                c.setEstadoCarta("T");
                mazo.add(c);
                c.setNombreCarta(pinta + "Q");
                c.setValorCarta("" + i);
                c.setEstadoCarta("T");
                mazo.add(c);
                c.setNombreCarta(pinta + "K");
                c.setValorCarta("" + i);
                c.setEstadoCarta("T");
                mazo.add(c);
            } else if (i == 11) {
                c.setNombreCarta(pinta + "A");
                c.setValorCarta("" + i);
                c.setEstadoCarta("T");
                mazo.add(c);
            } else {
                c.setNombreCarta(pinta + i);
                c.setValorCarta("" + i);
                c.setEstadoCarta("T");
                mazo.add(c);
            }

        }
    }

    public ArrayList<Carta> verificaPuntaje(ArrayList<Carta> cartas) {
        for (Carta item : cartas) {
            suma += Integer.parseInt(item.getValorCarta());
        }
        if (suma > 21) {
            cartas = verificaAS(cartas);
        }
        return cartas;
    }

    public ArrayList<Carta> verificaAS(ArrayList<Carta> cartas) {
        for (Carta item : cartas) {
            if (item.getNombreCarta().contains("A")) {
                item.setValorCarta("1");
                cartas = verificaPuntaje(cartas);
                break;
            }
        }
        return cartas;
    }



    public ArrayList<Cliente> getJugadores() {
        if (jugadores == null) {
            jugadores = new ArrayList<>();
        }
        return jugadores;
    }

    public void setJugadores(ArrayList<Cliente> jugadores) {
        this.jugadores = jugadores;
    }

    public ArrayList<Carta> getMazo() {
        return mazo;
    }

    public void setMazo(ArrayList<Carta> mazo) {
        this.mazo = mazo;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
    }

}
