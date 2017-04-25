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
                String msjREG=protocolo.getEncabezado()+
                              protocolo.getIdentUserConect()
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
                    Cartas[] clMazo = clCartas.CrearMazo("");
                    Cartas[] clCartasRep = clCartas.RepartirInicioJuego(clMazo);
                   // clMazo = clCartas.CrearMazo("");
                   // clCartasRep = clCartas.RepartirInicioJuego(clMazo);
                    DataOutputStream dosur=new DataOutputStream(socketUserRecep.getOutputStream());
                    String mensajeRegreso=protocolo.getEncabezado()+
                                          protocolo.getIdentUserConect() +
                                          clCartasRep[0].getNombreCarta() +
                                          clCartasRep[0].getEstadoCarta() +
                                          clCartasRep[0].getValorCarta()  +
                                          clCartasRep[1].getNombreCarta() +
                                          clCartasRep[1].getEstadoCarta() +
                                          clCartasRep[1].getValorCarta()  +
                                          "INJR"+
                                          protocolo.getMensajeUser();
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
                    clMazoEm = clCartasEm.CrearMazo("");
                    clCartasRepEm = clCartasEm.RepartirInicioJuego(clMazoEm);
                    DataOutputStream dosuc=new DataOutputStream(socketUserEmisor.getOutputStream());
                    String mensajeRegreso=protocolo.getEncabezado()+
                                          clCartasRepEm[0].getNombreCarta() +
                                          clCartasRepEm[0].getEstadoCarta() +
                                          clCartasRepEm[0].getValorCarta()  +
                                          clCartasRepEm[1].getNombreCarta() +
                                          clCartasRepEm[1].getEstadoCarta() +
                                          clCartasRepEm[1].getValorCarta()  +
                                          protocolo.getIdentUserConect()
                                          +estadoMensajeInterno+
                                          protocolo.getMensajeUser();
                    dosuc.writeUTF(mensajeRegreso);
                
                break;
                
            case "PC":
               
                Clientes clConectpc =(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserConect());
                Socket socketUserEmisorpc = clConectpc.getSocket(); 
                
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) != null)
                {          
                    Clientes clRecep=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserRecep());
                    Socket socketUserRecep  = clRecep.getSocket();  
                    Cartas clCartas = new Cartas();
                    Cartas[] clMazo = clCartas.CrearMazo(protocolo.getMensajeUser());
                    Cartas clCartasRep = clCartas.RepartirCarta(clMazo);
                   // clMazo = clCartas.CrearMazo("");
                   // clCartasRep = clCartas.RepartirInicioJuego(clMazo);
                    DataOutputStream dosur=new DataOutputStream(socketUserRecep.getOutputStream());
                    String mensajeRegresoPC =protocolo.getEncabezado()+
                                          protocolo.getIdentUserConect() +
                                          clCartasRep.getNombreCarta() +
                                          clCartasRep.getEstadoCarta() +
                                          clCartasRep.getValorCarta()  +
                                          "PCR"+
                                          protocolo.getMensajeUser();
                    dosur.writeUTF(mensajeRegresoPC);
                }
                
                String estadoMensajeInternoPc ="";
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) == null)
                    estadoMensajeInternoPc ="PCE0";
                else
                    estadoMensajeInternoPc ="PCE1";
                    Cartas clCartas = new Cartas();
                    Cartas[] clMazopc = clCartas.CrearMazo(protocolo.getMensajeUser());
                    Cartas clCartasRepPc = clCartas.RepartirCarta(clMazopc);
                    DataOutputStream dosucpc=new DataOutputStream(socketUserEmisorpc.getOutputStream());
                    String mensajeRegresopc =protocolo.getEncabezado()+
                                          clCartasRepPc.getNombreCarta() +
                                          clCartasRepPc.getEstadoCarta() +
                                          clCartasRepPc.getValorCarta()  +
                                          protocolo.getIdentUserConect()
                                          +estadoMensajeInternoPc+
                                          protocolo.getMensajeUser();
                    dosucpc.writeUTF(mensajeRegresopc);
                
                break;    
            case "IEJ":
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserConect()) != null)
                {          
                    Clientes clRecepIEL=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserConect());
                    Socket scUserRecepIEL  = clRecepIEL.getSocket();
                    DataOutputStream dosur=new DataOutputStream(scUserRecepIEL.getOutputStream());
                    dosur.writeUTF(nuevoMensaje);
                }
                break;
            case "PLJ":
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) != null)
                {          
                    Clientes clRecepIEL=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserRecep());
                    Socket scUserRecepIEL  = clRecepIEL.getSocket();
                    DataOutputStream dosur=new DataOutputStream(scUserRecepIEL.getOutputStream());
                    String mensajeRegresoSj=protocolo.getEncabezado()+
                                          protocolo.getIdentUserRecep()+
                                          "INJ2"+
                                          protocolo.getMensajeUser();
                    dosur.writeUTF(nuevoMensaje + mensajeRegresoSj);
                }
                break;
            case "PUC":
                if(SocketServidor.listaClientes.get(protocolo.getIdentUserRecep()) != null)
                {
                    Clientes clRecepPUC=(Clientes)SocketServidor.listaClientes.get(protocolo.getIdentUserRecep());
                    dos.writeUTF(nuevoMensaje+
                                 clRecepPUC.getEstado());
                }else
                    dos.writeUTF(nuevoMensaje+"2");
                break;       
                
            case "IES":                
                Clientes clIES=new Clientes();
                clIES.setSocket(this.clienteServer);                
                clIES.setEstado("0");
                SocketServidor.listaClientes.put(protocolo.getIdentUserConect(),clIES);
                break;                
        }
    }
}
