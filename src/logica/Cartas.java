/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author CristianPc
 */
public class Cartas {
    private String nombreCarta;
    private String estadoCarta;
    private int valorCarta;
    private Cartas[] mazo = new Cartas[52];
    private int sumaCartas = 0;

    public int getSumaCartas() {
        return sumaCartas;
    }

    public void setSumaCartas(int sumaCartas) {
        this.sumaCartas = sumaCartas;
    }
    
    public Cartas[] CrearMazo(Cartas[] pCartasOcu){
        String corazones = "Co";
        String diamantes = "Dm";
        String picas = "Pi";
        String treboles = "Tb";
        int aux = 0;
        for(int i = 0; i<= mazo.length; i++){
            String tipoCarta = "";
            if(aux == 0) tipoCarta = corazones;
            else if(aux == 1) tipoCarta = diamantes;
            else if(aux == 2) tipoCarta = picas;
            else if(aux == 3) tipoCarta = treboles;
            for(int j = 1; j <= 13; j++){
                mazo[i].setNombreCarta(tipoCarta +j);
                mazo[i].setEstadoCarta("0");
                for(int x= 0; x < pCartasOcu.length; x++){
                    if(pCartasOcu[x].nombreCarta == mazo[i].nombreCarta)
                        mazo[i].setEstadoCarta("1");                   
                }
                if(j > 10){
                    mazo[i].setValorCarta(10); 
                }else{
                    mazo[i].setValorCarta(j);               
                }
            }
            aux++;
        }
        return mazo;
    }
    
    public Cartas[] RepartirInicioJuego(Cartas[] clMazo){
        Cartas[] oCartas = new Cartas[2];
        Cartas vCarta = new Cartas();
        for(int i =0; i < oCartas.length; i++){
            vCarta = clMazo[(int)(Math.random()*52)];
            if(i==0) vCarta.setEstadoCarta("2");
            else vCarta.setEstadoCarta("1");
            oCartas[i] = vCarta;
        }
        return oCartas;
    }
    
     public Cartas RepartirCarta(Cartas[] clMazo){
        Cartas vCarta = new Cartas();
        vCarta = clMazo[(int)(Math.random()*52)];
        int aux = 0;
        while(aux == 0){
            if(vCarta.getEstadoCarta() != "0"){
                vCarta = clMazo[(int)(Math.random()*52)];
            }else{
                aux = 1;
            }           
            vCarta.setEstadoCarta("1");
        }
        return vCarta;
    }
     
    public int juegoBlackJack(Cartas[] oCartas){
        int sumaBJ = 0;
        int as = 0;
        int estadoJuego = 0;
        for(int i=0; i < oCartas.length; i++){
            
            if(oCartas[i].getValorCarta() == 1 && as == 0){
                oCartas[i].setValorCarta(11);
                as++;
            }
            sumaBJ = sumaBJ + oCartas[i].getValorCarta();
            if(sumaBJ == 21){
                 estadoJuego = 1;
            }else{
                if(sumaBJ > 21){
                    if(as > 0){
                        sumaBJ = sumaBJ - 10;
                        if(sumaBJ > 21){
                            estadoJuego = 2;
                        }
                        else{
                            estadoJuego = 0;
                        }
                    }
                     estadoJuego = 2;
                }else{
                    estadoJuego = 0;
                }
            }
            
        }
        this.setSumaCartas(sumaBJ);
        return estadoJuego;
    }

    public int getValorCarta() {
        return valorCarta;
    }

    public void setValorCarta(int valorCarta) {
        this.valorCarta = valorCarta;
    }
    
    public String getNombreCarta() {
        return nombreCarta;
    }

    public void setNombreCarta(String nombreCarta) {
        this.nombreCarta = nombreCarta;
    }

    public String getEstadoCarta() {
        return estadoCarta;
    }

    public void setEstadoCarta(String estadoCarta) {
        this.estadoCarta = estadoCarta;
    }

    
    
    
}
