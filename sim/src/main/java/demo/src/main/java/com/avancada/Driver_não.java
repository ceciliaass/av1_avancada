// package demo.src.main.java.com.avancada;

// //import de.tudresden.sumo.cmd.Route;
// import de.tudresden.sumo.cmd.Vehicle;
// import de.tudresden.sumo.objects.SumoStringList;
// import it.polito.appeal.traci.SumoTraciConnection;

// // import java.awt.Color;
// // import java.awt.event.ActionEvent;
// // import java.awt.event.ActionListener;
// // import java.awt.event.KeyEvent;
// // import java.awt.event.KeyListener;
// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.io.OutputStream;
// import java.io.OutputStreamWriter;
// import java.io.Writer;
// import java.net.Socket;
// //import javax.swing.*;

// import java.util.ArrayList;

// import org.json.JSONObject;

// public class Driver_não extends Thread{
//     Car carro;
//     private int conta;
//     //private AlphaBank banco;
//     private SumoTraciConnection sumo;

//     private  ArrayList<Route>executar; 
//     private  ArrayList<Route>executando; 
//     private  ArrayList<Route>executadas; 


//     private Socket socket;
//     private OutputStream ou ;
//     private Writer ouw;
//     private BufferedWriter bfw;
//     private String txtIP;
//     private String txtPorta;
//     //private String txtNome;
//     Socket con;
//     private boolean on_off;
//     private int inf;
//     private int sup;
//     private Boolean abastecendo;

//     public Driver_não(Car carro, SumoTraciConnection sumo, String txtNome /*AlphaBank banco*/, int conta, int inf, int sup) throws IOException{
        
//         this.txtIP = "127.0.0.2";
//         this.txtPorta = "12347";
//         //this.txtNome = txtNome;
//         this.carro =  carro;
//         this.conta = conta;
//         //this.banco= banco;
//         this.executar = new ArrayList<Route>();
//         this.executando = new ArrayList<Route>();
//         this.executadas = new ArrayList<Route>();

//         this.sumo = sumo;
//         this.on_off=true;
//         this.inf=inf;
//         this.sup = sup;
//         this.abastecendo= false;
//     }

//     /***
//      * Método usado para conectar no server socket, retorna IO Exception caso dê algum erro.
//      * @throws IOException
//      */
//     public void conectar() throws IOException{
//         socket = new Socket(this.txtIP,Integer.parseInt(this.txtPorta));
//         ou = socket.getOutputStream();
//         ouw = new OutputStreamWriter(ou);
//         bfw = new BufferedWriter(ouw);
//         bfw.write(txtPorta+"\r\n");
//         bfw.flush();
//     }

//     /***
//      * Método usado para enviar mensagem para o server socket
//      * @param msg do tipo String
//      * @throws IOException retorna IO Exception caso dê algum erro.
//      */
//     public void enviarMensagem(String msg) throws Exception{
// 		Cryptography crpt = new Cryptography();
// 		msg= crpt.encrypt(msg, crpt.genKey(msg.length()));
//         bfw.write(msg+"\r\n");    
//         bfw.flush();
//         // System.out.println("o no envirar do driveeeeer");
//     }

//     /**
//      * Método usado para receber mensagem do servidor
//      * @throws IOException retorna IO Exception caso dê algum erro.
//      */
//     public void escutar() throws IOException{

//         InputStream in = socket.getInputStream();
//         InputStreamReader inr = new InputStreamReader(in);
//         BufferedReader bfr = new BufferedReader(inr);
//         String msg = "";

//         while(!"Sair".equalsIgnoreCase(msg)){

//                 msg = bfr.readLine();
//                 JSONObject mensagem = new JSONObject(msg);
//                 if((int) mensagem.get("conta_recebendo")==this.conta){
//                     // System.out.println("Motorista recebendo");
//                 }
//                 // System.out.println(msg);
//             }
//     }

//     /***
//      * Método usado quando o usu�rio clica em sair
//      * @throws IOException retorna IO Exception caso d� algum erro.
//      */
//     public void sair() throws Exception{
// 		Json json = new Json();
// 		enviarMensagem(json.Json_mensagens("Sair"));
//         bfw.close();
//         ouw.close();
//         ou.close();
//         socket.close();
//     }
   
//     @Override
//     public void run() {
//         try {
//            // System.out.println("solicita");
//             //Thread.sleep(00);
//             this.executar=this.carro.Solicita_rotas(this.inf,this.sup);
//             //.out.println("solicitou");
//             //System.out.println(this.executar);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         initializeRoutes();

//         try {
//             Thread.sleep(200);
//         } catch (Exception e) {
//             System.out.println("Falhou no  sleep.\nException: " + e);
//         }

//         Thread thread = new Thread (carro);
//         thread.start();
//         while (!this.sumo.isClosed()) {
//             //System.out.println(this.carro.getFuelTank());

//             //System.out.println(this.carro.getFuelTank());
//             // if(this.carro.getFuelTank()<=9.9 & this.abastecendo==false){
//             //     try {
//             //         Thread posto = new FuelStation(this);
//             //         if(FuelStation.getbomba1()){
//             //             FuelStation.setbomba1();
//             //             posto.start();
//             //             System.out.println(this.carro.getFuelTank());
//             //             Thread.sleep(2000);
//             //         }else if(FuelStation.getbomba2()){
//             //             posto.start();
//             //             System.out.println(this.carro.getFuelTank());
//             //             FuelStation.setbomba2();
//             //             Thread.sleep(2000);
//             //         }else{
//             //             FuelStation.setespera(true);
//             //             while(!FuelStation.getbomba2() || !FuelStation.getbomba1()){
//             //                 posto.wait();
//             //             }
//             //             FuelStation.setespera(false);
//             //             System.out.println("");
//             //         }

//             //     } catch (IOException e) {
//             //         e.printStackTrace();
//             //     } catch (InterruptedException e) {
//             //        e.printStackTrace();
//             //      }
//             // }
            
//                     //System.out.println(this.carro.getFuelTank());
            
//                 try {
                    
//                     SumoStringList carros = (SumoStringList) sumo.do_job_get(Vehicle.getIDList());
//                     // System.out.println(" listaa" + carros);

//                     boolean teste = true;
//                     while (teste) {
//                         if (carros.size() > 0) {
//                             if ((carros).contains(this.carro.getIdAuto())) {
//                                 teste = false;
//                                 // System.out.println("oiiii");
//                             }
//                         }
//                         carros = (SumoStringList) sumo.do_job_get(Vehicle.getIDList());
//                     }

//                     boolean rota_executando = true;

//                     while (rota_executando) {
//                         if (!(carros).contains(this.carro.getIdAuto())){
//                              rota_executando = false;
//                              thread.interrupt();
//                         } 
//                         //Thread.sleep(200);
//                         carros = (SumoStringList) sumo.do_job_get(Vehicle.getIDList());
//                     }

                    
//                     addNovaRota(executar.get(0).getIdItinerary());
//                     //thread.start();
                    
//                     // if(carros.size()>0 && executar.size()>0){
//                     //     if(!(carros).contains(this.carro.getIdAuto())){
//                             //Thread.sleep(100);	
//                             // addNovaRota(executar.get(0).getIdItinerary());
//                             // thread.start();
//                             // System.out.println(" não teeeeem" );
//                             // System.out.println("executaaar" +this.executar.size());


//                             //Thread.sleep(200);
//                             //addNovaRota(executar.get(0).getIdItinerary());
                            
//                             addExecutando(this.executar.get(0));
//                             addExecutadas(this.executando.get(0));
//                             delExecutar(0);
//                             delExecutando(0);
//                             // System.out.println("executaaar" +this.executando.size());
//                             // System.out.println("executaaar" +this.executar.size());

//                     //     }
//                     // }
                
//                         // sumo.do_job_set(Vehicle.setRouteID(this.carro.getIdAuto(),this.executando.get(0).getIdItinerary()));
//                         // i=9;
                    
//                 } catch (Exception e) {
//                     // TODO Auto-generated catch block
//                     e.printStackTrace();
//                 }
//         }
//     }

//     public void setAbastecendo(Boolean situacao){
//         this.abastecendo = situacao;
//     }
//     private void initializeRoutes() {

//                 SumoStringList edge = new SumoStringList();
                
//                 int cont=0;
//                 try{
//                 for(int i =0; i<this.executar.size();i++){
//                         for (String e : executar.get(i).getEdges().split(" ")) {
//                             edge.add(e);
//                             cont++;
//                         }
//                         sumo.do_job_set(de.tudresden.sumo.cmd.Route.add(executar.get(i).getIdItinerary(), edge));
//                         //sumo.do_job_set(Vehicle.add(this.auto.getIdAuto(), "DEFAULT_VEHTYPE", this.itinerary.getIdItinerary(), 0,
//                         //		0.0, 0, (byte) 0));
//                         edge.clear();
//                 }
//                         sumo.do_job_set(Vehicle.addFull(this.carro.getIdAuto(), 				//vehID
//                                                         executar.get(0).getIdItinerary(), 	//routeID 
//                                                         "DEFAULT_VEHTYPE", 					//typeID 
//                                                         "now", 								//depart  
//                                                         "0", 								//departLane 
//                                                         "0", 								//departPos 
//                                                         "0",								//departSpeed
//                                                         "current",							//arrivalLane 
//                                                         "max",								//arrivalPos 
//                                                         "current",							//arrivalSpeed 
//                                                         "",									//fromTaz 
//                                                         "",									//toTaz 
//                                                         "", 								//line 
//                                                         this.carro.getPersonCapacity(),		//personCapacity 
//                                                         this.carro.getPersonNumber())		//personNumber
//                                 );  
//                         //sumo.do_(sim.traci4j.src.java.it.polito.appeal.traci.Vehicle.getCurrentRoute());
//                         //sumo.do_job_set(Vehicle.setRoute(this.carro.getIdAuto(),edges));
//                         //sumo.do_job_set(Vehicle.setColor(this.carro.getIdAuto(), this.carro.getColorAuto()));
//                         //this.sumo.do_timestep();
//                         addExecutando(this.executar.get(0));
//                         delExecutar(0);

//             }catch (Exception e) {
//                         e.printStackTrace();
//                     }
//         }
//     public void addNovaRota(String rota) throws Exception{
//         ///sumo.do_job_set(Vehicle.setRouteID(this.carro.getIdAuto(),rota));
//         //Thread.sleep(17000);
       
//         sumo.do_job_set(Vehicle.addFull(this.carro.getIdAuto(), 				//vehID
//                 rota, 	                            //routeID 
//                 "DEFAULT_VEHTYPE", 					//typeID 
//                 "now", 								//depart  
//                 "0", 								//departLane 
//                 "0", 								//departPos 
//                 "0",								//departSpeed
//                 "current",							//arrivalLane 
//                 "max",								//arrivalPos 
//                 "current",							//arrivalSpeed 
//                 "",									//fromTaz 
//                 "",									//toTaz 
//                 "", 								//line 
//                 this.carro.getPersonCapacity(),		//personCapacity 
//                 this.carro.getPersonNumber())		//personNumber
//             ); 
        
    
//     }

//     public void setfuelDivida(int contaFuelStation, double fuelDivida){
//         BotPayment botPayment = new BotPayment(contaFuelStation, fuelDivida);
//         Thread t1 = new Thread(botPayment);
//         t1.start();
//     }

//     public int getConta(){
//         return conta;
//     }
     
//     public Car getCar(){
//         return carro;
//     }

//     public synchronized void addExecutando(Route executar){
//         this.executando.add(executar);
//     }

//     public synchronized void addExecutadas(Route executando){
//         this.executadas.add(executando);
//     }

//     public synchronized void delExecutar(int executar){
//         this.executar.remove(executar);
//     }

//     public synchronized void delExecutando(int executar){
//         this.executando.remove(executar);
//     }

//     public synchronized void delExecutadas(Route executando){
//         this.executadas.remove(executando);
//     }

//     public synchronized ArrayList<Route> setExecutando(){
//         return this.executando;
//     }

//     public synchronized ArrayList<Route> setExecutar(){
//         return this.executar;
//     }

//     public synchronized ArrayList<Route> setExecutadas(){
//         return this.executadas;
//     }


//     class BotPayment extends Thread{
//         private int conta_recebendo;

//         private Double valor;
//         private boolean on_off;
//         public BotPayment(int contaRecebendo,double valor){
//             this.conta_recebendo = contaRecebendo;
//             this.valor = valor;
//             this.on_off = true;
//         }

//         @Override
//         public void run() {

//                 Json json = new Json();

//                 try {
//                     conectar();
//                     enviarMensagem(json.Json_pagamento(conta, conta_recebendo, valor));
//                     // System.out.println("aquuuuuuuuuuuuui");
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//                 // conta.setSaldo(conta.getSaldo(conta.getLogin(), conta.getSenha())-this.valor,conta.getLogin(), conta.getSenha());
//                 // conta_recebendo.setSaldo(conta_recebendo.getSaldo(conta_recebendo.getLogin(), conta_recebendo.getSenha())-this.valor,conta_recebendo.getLogin(), conta_recebendo.getSenha());
            
//         }
        

//         public void setOn_off(){
//             this.on_off = !this.on_off;
//         }

//         public boolean getOn_off(){
//             return this.on_off;
//         }
//     }
// }