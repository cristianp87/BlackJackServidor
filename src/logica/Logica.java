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
import recursos.enumeraciones.EnumComando;

/**
 *
 * @author jomorenoro
 */
public class Logica {

    private ArrayList<Cliente> jugadores;
    private ArrayList<Carta> mazo;
    private int suma;
    private Juego jugador1;
    private Juego jugador2;
    private boolean terminaJuego;

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
            c.setValorCarta(Integer.parseInt(st.nextToken()));
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
            mensaje = mensaje.concat(item.getNombreCarta().concat("%")).concat(String.valueOf(item.getValorCarta()).concat("%")).concat(item.getEstadoCarta().concat(","));
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
        mazo.get(index).setEstadoCarta("T");
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
            c.setEstadoCarta("T");
            switch (i) {
                case 10:
                    c.setNombreCarta(pinta + i);
                    c.setValorCarta(i);
                    mazo.add(c);
                    c.setNombreCarta(pinta + "J");
                    c.setValorCarta(i);
                    mazo.add(c);
                    c.setNombreCarta(pinta + "Q");
                    c.setValorCarta(i);
                    mazo.add(c);
                    c.setNombreCarta(pinta + "K");
                    c.setValorCarta(i);
                    mazo.add(c);
                    break;
                case 11:
                    c.setNombreCarta(pinta + "A");
                    c.setValorCarta(i);
                    mazo.add(c);
                    break;
                default:
                    c.setNombreCarta(pinta + i);
                    c.setValorCarta(i);
                    mazo.add(c);
                    break;
            }

        }
    }

    public void verificaPuntaje(ArrayList<Carta> cartas) {
        for (Carta item : cartas) {
            suma += item.getValorCarta();
        }
        if (suma > 21) {
            verificaAS(cartas);
        }
    }

    public void verificaAS(ArrayList<Carta> cartas) {
        boolean asSi = false;
        for (Carta item : cartas) {
            if (asSi == false) {
                if (item.getNombreCarta().contains("A")) {
                    if(item.getValorCarta() != 1){
                        item.setValorCarta(1);
                        asSi = true;
                        break;
                    }
                }
            }
        }
        if (asSi) {
            suma = 0;
            verificaPuntaje(cartas);
            System.out.println("entro" + suma);
        }
    }

    public void iniciaJuego() {
        jugador1.setJuego(reparteCartasInicio());
        jugador2.setJuego(reparteCartasInicio());
        jugador1.setComando(EnumComando.JUG.name());
        jugador1.setEstado("A");
        jugador2.setComando(EnumComando.REG.name());
        jugador2.setEstado("A");
        llenaDatosJugadores();
    }

    public void iniciaPideCarta() {
        jugador1.setJuego(pideCarta(jugador1.getJuego()));
        verificaPuntaje(jugador1.getJuego());
        if (getSuma() > 21) {

            jugador1.setComando(EnumComando.PER.name());
            jugador1.setEstado("I");
            jugador2.setComando(EnumComando.GAN.name());
            jugador2.setEstado("I");
            terminaJuego = true;
        } else {
            jugador1.setComando(EnumComando.JUG.name());
            jugador1.setEstado("A");
            jugador2.setComando(EnumComando.REG.name());
            jugador2.setEstado("A");
        }
        llenaDatosJugadores();
    }

    public void iniciaPideCartaJug2() {
        jugador2.setJuego(pideCarta(jugador2.getJuego()));
        verificaPuntaje(jugador2.getJuego());
        if (getSuma() > 21) {
            jugador2.setComando(EnumComando.PER.name());
            jugador2.setEstado("I");
            jugador1.setComando(EnumComando.GAN.name());
            jugador1.setEstado("I");
            terminaJuego = true;
        } else {
            jugador2.setComando(EnumComando.JUG.name());
            jugador2.setEstado("A");
            jugador1.setComando(EnumComando.REG.name());
            jugador1.setEstado("A");
        }
        llenaDatosJugadores();
    }

    public void plantarseJug1() {
        jugador1.setComando(EnumComando.REG.name());
        jugador1.setEstado("A");
        jugador2.setComando(EnumComando.JUG.name());
        jugador2.setEstado("A");
        llenaDatosJugadores();
    }

    public void plantarseJug2() {
        jugador1.setEstado("A");
        jugador2.setEstado("A");
        llenaDatosJugadores();
        if (jugador2.getSumaCartas() > jugador1.getSumaCartas()) {
            jugador2.setComando(EnumComando.GAN.name());
            jugador1.setComando(EnumComando.PER.name());
        } else if (jugador2.getSumaCartas() < jugador1.getSumaCartas()) {
            jugador2.setComando(EnumComando.PER.name());
            jugador1.setComando(EnumComando.GAN.name());
        } else if (jugador2.getSumaCartas() == jugador1.getSumaCartas()) {
            jugador2.setComando(EnumComando.EMP.name());
            jugador1.setComando(EnumComando.EMP.name());
        }
    }

    public void llenaDatosJugadores() {
        jugador1.setCartasEnemigo(jugador2.getJuego());
        jugador1.setNombreJugadorEnemigo(jugador2.getNombreJugador());
        jugador1.setIdUsuarioEnemigo(jugador2.getIdUsuario());
        jugador2.setCartasEnemigo(jugador1.getJuego());
        jugador2.setNombreJugadorEnemigo(jugador1.getNombreJugador());
        jugador2.setIdUsuarioEnemigo(jugador1.getIdUsuario());
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

    public Juego getJugador1() {
        return jugador1;
    }

    public void setJugador1(Juego jugador1) {
        this.jugador1 = jugador1;
    }

    public Juego getJugador2() {
        return jugador2;
    }

    public void setJugador2(Juego jugador2) {
        this.jugador2 = jugador2;
    }

    public boolean isTerminaJuego() {
        return terminaJuego;
    }

    public void setTerminaJuego(boolean terminaJuego) {
        this.terminaJuego = terminaJuego;
    }

}
