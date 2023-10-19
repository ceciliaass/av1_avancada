package demo.src.main.java.com.avancada;

//import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;

// import java.awt.Color;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.KeyEvent;
// import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
//import javax.swing.*;

import java.util.ArrayList;

import org.json.JSONObject;

public class Driver extends Thread{
    Car carro;
    private int conta;
    //private AlphaBank banco;
    private SumoTraciConnection sumo;

    private  ArrayList<Route>executar; 
    private  ArrayList<Route>executando; 
    private  ArrayList<Route>executadas; 


    private Socket socket;
    private OutputStream ou ;
    private Writer ouw;
    private BufferedWriter bfw;
    private String txtIP;
    private String txtPorta;
    //private String txtNome;
    Socket con;
    private boolean on_off;
    int inf;
    int sup;
    public Driver(Car carro, SumoTraciConnection sumo, String txtNome /*AlphaBank banco*/, int conta, int inf, int sup) throws IOException{
        
        this.txtIP = "127.0.0.2";
        this.txtPorta = "12347";
        //this.txtNome = txtNome;
        this.carro =  carro;
        this.conta = conta;
        //this.banco= banco;
        this.executar = new ArrayList<Route>();
        this.executando = new ArrayList<Route>();
        this.executadas = new ArrayList<Route>();
        this.sumo = sumo;
        this.on_off=true;
        this.inf=inf;
        this.sup = sup;
    }

    /***
     * Método usado para conectar no server socket, retorna IO Exception caso dê algum erro.
     * @throws IOException
     */
    public void conectar() throws IOException{
        socket = new Socket(this.txtIP,Integer.parseInt(this.txtPorta));
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        bfw.write(txtPorta+"\r\n");
        bfw.flush();
    }

    /***
     * Método usado para enviar mensagem para o server socket
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void enviarMensagem(String msg) throws Exception{
		Cryptography crpt = new Cryptography();
		msg= crpt.encrypt(msg, crpt.genKey(msg.length()));
        bfw.write(msg+"\r\n");    
        bfw.flush();
    }

    /**
     * Método usado para receber mensagem do servidor
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void escutar() throws IOException{

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while(!"Sair".equalsIgnoreCase(msg)){

                msg = bfr.readLine();
                JSONObject mensagem = new JSONObject(msg);
                if((int) mensagem.get("conta_recebendo")==this.conta){
                    System.out.println("Motorista recebendo");
                }
                System.out.println(msg);
            }
    }

    /***
     * Método usado quando o usu�rio clica em sair
     * @throws IOException retorna IO Exception caso d� algum erro.
     */
    public void sair() throws Exception{
		Json json = new Json();
		enviarMensagem(json.Json_mensagens("Sair"));
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }
   
    @Override
    public void run() {
        try {
           // System.out.println("solicita");
            //Thread.sleep(00);
            this.executar=this.carro.Solicita_rotas(this.inf,this.sup);
            //.out.println("solicitou");
            //System.out.println(this.executar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeRoutes();
        Thread thread = new Thread (carro);
        thread.start();
        //this.carro.run();
        while (this.on_off) {
            System.out.println(this.carro.getFuelTank());
            if(this.carro.getFuelTank()<=3.0){
                try {
                    Thread posto = new FuelStation(this);
                    posto.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.sumo.isClosed()) {
                carro.setOn_off(false);
                this.on_off = false;
                System.out.println("SUMO is closed...");
            }
        }
        //while (this.on_off) {
        //     this.carro.getRotaAtual();
        //     if(this.carro.getFuelTank()<=3.0){
        //         try {

        //             Thread posto = new FuelStation(this);
        //             posto.start();
        //         } catch (IOException e) {
        //             e.printStackTrace();
        //         }
        //     }
        //}
    }

    private void initializeRoutes() {

		SumoStringList edge = new SumoStringList();
		edge.clear();
        try{
		for(int i =0; i<this.executar.size();i++){
                for (String e : executar.get(i).getEdges().split(" ")) {
                    edge.add(e);
                }
                sumo.do_job_set(de.tudresden.sumo.cmd.Route.add(executar.get(i).getIdItinerary(), edge));
                //sumo.do_job_set(Vehicle.add(this.auto.getIdAuto(), "DEFAULT_VEHTYPE", this.itinerary.getIdItinerary(), 0,
                //		0.0, 0, (byte) 0));
                edge.clear();
                // while(carro.getRotaAtual().equals(null)){
                    
                // sumo.do_job_set(Vehicle.addFull(this.carro.getIdAuto(), 				//vehID
                //                                 executar.get(i).getIdItinerary(), 	//routeID 
                //                                 "DEFAULT_VEHTYPE", 					//typeID 
                //                                 "now", 								//depart  
                //                                 "0", 								//departLane 
                //                                 "0", 								//departPos 
                //                                 "0",								//departSpeed
                //                                 "current",							//arrivalLane 
                //                                 "max",								//arrivalPos 
                //                                 "current",							//arrivalSpeed 
                //                                 "",									//fromTaz 
                //                                 "",									//toTaz 
                //                                 "", 								//line 
                //                                 this.carro.getPersonCapacity(),		//personCapacity 
                //                                 this.carro.getPersonNumber())		//personNumber
                //         );
                // }
            }
                sumo.do_job_set(Vehicle.addFull(this.carro.getIdAuto(), 				//vehID
                                                executar.get(0).getIdItinerary(), 	//routeID 
                                                "DEFAULT_VEHTYPE", 					//typeID 
                                                "now", 								//depart  
                                                "0", 								//departLane 
                                                "0", 								//departPos 
                                                "0",								//departSpeed
                                                "current",							//arrivalLane 
                                                "max",								//arrivalPos 
                                                "current",							//arrivalSpeed 
                                                "",									//fromTaz 
                                                "",									//toTaz 
                                                "", 								//line 
                                                this.carro.getPersonCapacity(),		//personCapacity 
                                                this.carro.getPersonNumber())		//personNumber
                        );  
                //sumo.do_(sim.traci4j.src.java.it.polito.appeal.traci.Vehicle.getCurrentRoute());
                //sumo.do_job_set(Vehicle.setRoute(this.carro.getIdAuto(), ));
                //sumo.do_job_set(Vehicle.setColor(this.carro.getIdAuto(), this.carro.getColorAuto()));
                //this.sumo.do_timestep();
            //}
        } catch (Exception e) {
                e.printStackTrace();
            }
	}

    public void setfuelDivida(int contaFuelStation, double fuelDivida){
        BotPayment botPayment = new BotPayment(contaFuelStation, fuelDivida);
        Thread t1 = new Thread(botPayment);
        t1.start();
    }

    public int getConta(){
        return conta;
    }
     
    public Car getCar(){
        return carro;
    }

    public synchronized void addExecutando(Route executar){
        this.executando.add(executar);
    }

    public synchronized void addExecutadas(Route executando){
        this.executadas.add(executando);
    }

    public synchronized void delExecutar(Route executar){
        this.executar.remove(executar);
    }

    public synchronized void delExecutando(Route executar){
        this.executando.remove(executar);
    }

    public synchronized void delExecutadas(Route executando){
        this.executadas.remove(executando);
    }

    public synchronized ArrayList<Route> setExecutando(){
        return this.executando;
    }

    public synchronized ArrayList<Route> setExecutar(){
        return this.executar;
    }

    public synchronized ArrayList<Route> setExecutadas(){
        return this.executadas;
    }


    class BotPayment extends Thread{
        private int conta_recebendo;

        private Double valor;
        private boolean on_off;
        public BotPayment(int contaRecebendo,double valor){
            this.conta_recebendo = contaRecebendo;
            this.valor = valor;
            this.on_off = true;
        }

        @Override
        public void run() {

                Json json = new Json();

                try {
                    enviarMensagem(json.Json_pagamento(conta, conta_recebendo, valor));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // conta.setSaldo(conta.getSaldo(conta.getLogin(), conta.getSenha())-this.valor,conta.getLogin(), conta.getSenha());
                // conta_recebendo.setSaldo(conta_recebendo.getSaldo(conta_recebendo.getLogin(), conta_recebendo.getSenha())-this.valor,conta_recebendo.getLogin(), conta_recebendo.getSenha());
            
        }
        

        public void setOn_off(){
            this.on_off = !this.on_off;
        }

        public boolean getOn_off(){
            return this.on_off;
        }
    }
}