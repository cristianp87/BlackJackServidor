/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

/**
 *
 * @author CristianPc
 */
public class ClienteServidor extends Thread {
    private Socket clienteServer;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Protocolo protocolo=new Protocolo();
    private String nuevoMensaje;
    
    public ClienteServidor(Socket socket) throws IOException
    {
      this.clienteServer=socket;
      this.dis=new DataInputStream(socket.getInputStream());
      this.dos=new DataOutputStream(socket.getOutputStream());
    }
    
    @Override
    public void run(){  
        boolean leer=true;
        while(leer)
        {
            try {
                nuevoMensaje="";
                nuevoMensaje=dis.readUTF();      
                protocolo.leerMensaje(nuevoMensaje);
                ejecutarActividad();
            } catch (IOException ex) {
                leer=false;
            }
        }
    }
    
    public void ejecutarActividad() throws IOException 
    {
        switch (protocolo.getComando()) {
            case "REG":
                String estadoReg;
                if (SocketServidor.listaClientes.containsKey(protocolo.getIdentUserConect()))
                {
                    Clientes clREG=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserConect());
                    if(clREG.getEstado().equals("1"))
                    {
                        System.out.println("Usuario ya está conectado");
                        estadoReg="NK";                        
                    }
                    else
                    {
                        estadoReg="OK";
                        clREG.setSocket(this.clienteServer);
                        clREG.setEstado("1");
                        SocketServidor.listaClientes.put(protocolo.getIdentUserConect(),clREG);
                    }
                }else {
                    if(SocketServidor.listaClientes.isEmpty()){
                        estadoReg="OK";
                        Clientes cl=new Clientes();
                        cl.setSocket(this.clienteServer);               
                        cl.setEstado("1");
                        SocketServidor.listaClientes.put(protocolo.getIdentUserConect(),cl);
                        System.out.println("cliente registrado:"+protocolo.getIdentUserConect());
                    }else{
                        estadoReg="OK2";
                        Clientes cl=new Clientes();
                        cl.setSocket(this.clienteServer);               
                        cl.setEstado("2");
                        SocketServidor.listaClientes.put(protocolo.getIdentUserConect(),cl);
                        System.out.println("cliente registrado:"+protocolo.getIdentUserConect());
                    }
                }          
                DataOutputStream doScClSolREG=new DataOutputStream(clienteServer.getOutputStream());
                String msjREG=protocolo.getIdentUserConect()
                              +"REG"+estadoReg;
                doScClSolREG.writeUTF(msjREG);
                
                break;
            case "INJ":
                Clientes clConect=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserConect());
                Socket socketUserEmisor = clConect.getSocket();
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) != null)
                {          
                    Clientes clRecep=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserRecep());
                    Socket socketUserRecep  = clRecep.getSocket();  
                    Cartas clCartas = new Cartas();
                    Cartas[] clMazo = clCartas.CrearMazo(clRecep.getStrCartasOcupadas());
                    Cartas[] clCartasRep = clCartas.RepartirInicioJuego(clMazo);
                    this.AgregarCartasPedidasReceptor(clRecep, clCartasRep[0]);
                    this.AgregarCartasPedidasReceptor(clRecep, clCartasRep[1]);
                    DataOutputStream dosur=new DataOutputStream(socketUserRecep.getOutputStream());               
                    String mensajeRegreso= protocolo.getIdentUserConect() +
                                          "INJR"+
                                          clCartasRep[0].getNombreCarta() +
                                          clCartasRep[0].getEstadoCarta() +
                                          clCartasRep[0].getValorCarta()  +
                                          clCartasRep[1].getNombreCarta() +
                                          clCartasRep[1].getEstadoCarta() +
                                          clCartasRep[1].getValorCarta();
                    dosur.writeUTF(mensajeRegreso);
                }
                
                 String estadoMensajeInterno="";
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) == null)
                    estadoMensajeInterno="INJ0";
                else
                    estadoMensajeInterno="INJ1";
                
                    Cartas clCartasEm = new Cartas();
                    Cartas[] clMazoEm = new Cartas[52];
                    Cartas[] clCartasRepEm = new Cartas[2];
                    clMazoEm = clCartasEm.CrearMazo(clConect.getStrCartasOcupadas());
                    clCartasRepEm = clCartasEm.RepartirInicioJuego(clMazoEm);
                    this.AgregarCartasPedidasEmisor(clConect, clCartasRepEm[0]);
                    this.AgregarCartasPedidasEmisor(clConect, clCartasRepEm[1]);
                    DataOutputStream dosuc=new DataOutputStream(socketUserEmisor.getOutputStream());
                    String mensajeRegreso=protocolo.getIdentUserConect() +
                                          clCartasRepEm[0].getNombreCarta() +
                                          clCartasRepEm[0].getEstadoCarta() +
                                          clCartasRepEm[0].getValorCarta()  +
                                          clCartasRepEm[1].getNombreCarta() +
                                          clCartasRepEm[1].getEstadoCarta() +
                                          clCartasRepEm[1].getValorCarta()  +
                                          estadoMensajeInterno;
                    dosuc.writeUTF(mensajeRegreso);               
                break;
                
            case "PCT":
               
                Clientes clConectpc =(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserConect());
                Socket socketUserEmisorpc = clConectpc.getSocket(); 
                
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) != null)
                {          
                    Clientes clRecep=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserRecep());
                    Socket socketUserRecep  = clRecep.getSocket();  
                    Cartas clCartas = new Cartas();
                    Cartas[] clMazo = clCartas.CrearMazo(clRecep.getStrCartasOcupadas());
                    Cartas clCartasRep = clCartas.RepartirCarta(clMazo);
                    this.AgregarCartasPedidasReceptor(clRecep, clCartasRep);
                    DataOutputStream dosur=new DataOutputStream(socketUserRecep.getOutputStream());
                    String mensajeRegresoPC =protocolo.getIdentUserConect() +
                                          "CRR"+
                                          clCartasRep.getNombreCarta() +
                                          clCartasRep.getEstadoCarta() +
                                          clCartasRep.getValorCarta();
                    dosur.writeUTF(mensajeRegresoPC);
                }
                
                String estadoMensajeInternoPc ="";
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) == null)
                    estadoMensajeInternoPc ="CE0";
                else
                    estadoMensajeInternoPc ="CE1";
                
                    Cartas clCartasPCT = new Cartas();
                    Cartas[] clMazopc = clCartasPCT.CrearMazo(clConectpc.getStrCartasOcupadas());
                    Cartas clCartasRepPc = clCartasPCT.RepartirCarta(clMazopc);
                    this.AgregarCartasPedidasEmisor(clConectpc, clCartasRepPc);
                    DataOutputStream dosucpc=new DataOutputStream(socketUserEmisorpc.getOutputStream());
                    String mensajeRegresopc = protocolo.getIdentUserConect() +
                                          estadoMensajeInternoPc +
                                          clCartasRepPc.getNombreCarta() +
                                          clCartasRepPc.getEstadoCarta() +
                                          clCartasRepPc.getValorCarta();
                    dosucpc.writeUTF(mensajeRegresopc);
                
                break;    
            case "PLJ":
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) != null)
                {          
                    Clientes clRecepIEL=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserRecep());
                    Socket scUserRecepIEL  = clRecepIEL.getSocket();
                    DataOutputStream dosur=new DataOutputStream(scUserRecepIEL.getOutputStream());
                    String mensajeRegresoSj= protocolo.getIdentUserRecep()+
                                          "INJ2";
                    dosur.writeUTF(mensajeRegresoSj);
                }
                break;        
        }
    }
    
    public void AgregarCartasPedidasEmisor(Clientes oCliente, Cartas oCarta){
        if (SocketServidor.listaClientes.containsKey(protocolo.getIdentUserConect())){
            Cartas[] lCartas = oCliente.getStrCartasOcupadas();
            int lLongCartas = lCartas.length;
            lCartas[lLongCartas].setNombreCarta(oCarta.getNombreCarta());
            lCartas[lLongCartas].setEstadoCarta(oCarta.getEstadoCarta());
            lCartas[lLongCartas].setValorCarta(oCarta.getValorCarta());
            Clientes cl=new Clientes();
            cl.setSocket(this.clienteServer);               
            cl.setEstado("1");  
            cl.setStrCartasOcupadas(lCartas);
            SocketServidor.listaClientes.put(protocolo.getIdentUserConect(),cl);
            System.out.println("cliente "+protocolo.getIdentUserConect() + ": " + cl.getStrCartasOcupadas());
        }
    }
    
    public void AgregarCartasPedidasReceptor(Clientes oCliente, Cartas oCarta){
        if (SocketServidor.listaClientes.containsKey(protocolo.getIdentUserRecep())){
            Cartas[] lCartas = oCliente.getStrCartasOcupadas();
            int lLongCartas = lCartas.length;
            lCartas[lLongCartas].setNombreCarta(oCarta.getNombreCarta());
            lCartas[lLongCartas].setEstadoCarta(oCarta.getEstadoCarta());
            lCartas[lLongCartas].setValorCarta(oCarta.getValorCarta());
            Clientes cl=new Clientes();
            cl.setSocket(this.clienteServer);               
            cl.setEstado("2");  
            cl.setStrCartasOcupadas(lCartas);
            SocketServidor.listaClientes.put(protocolo.getIdentUserRecep(),cl);
            System.out.println("cliente "+protocolo.getIdentUserRecep() + ": " + cl.getStrCartasOcupadas());
        }
    }
}
