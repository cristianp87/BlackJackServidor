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
    
    public Cartas[] CrearMazo(String pCartasOcu){
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
                mazo[i].setNombreCarta(j + tipoCarta);
                if(pCartasOcu.contains(j + tipoCarta)){
                    mazo[i].setEstadoCarta("1");
                }else{
                    mazo[i].setEstadoCarta("0");
                }
                mazo[i].setValorCarta(j);               
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
            int aux = 0;
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
