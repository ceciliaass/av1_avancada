//Criar deposito na conta do driver
//Criar o Json para o AlphaBank

package demo.src.main.java.com.avancada;
// import de.tudresden.sumo.objects.SumoColor;

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
import java.net.ServerSocket;
import java.net.Socket;
//import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import de.tudresden.sumo.objects.SumoStringList;
//import io.sim.Auto;
import it.polito.appeal.traci.SumoTraciConnection;
//import sim.traci4j.src.java.it.polito.appeal.traci.Route;

import java.util.ArrayList;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class Company extends Thread {
    private static ServerSocket server;
    private static ArrayList<BufferedWriter>clientes;
    Account conta;
    private Socket socket;
    private OutputStream ou ;
    private Writer ouw;
    private BufferedWriter bfw;
    private String txtIP;
    private String txtPorta;
    private String txtNome;
    //private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    private ArrayList<Route>rotas;
    private ArrayList<Route>executar; 
    private ArrayList<Route>executando; 
    // private  ArrayList<Routes>executadas; 
    static SumoTraciConnection sumo;

    public Company(Socket con) throws IOException{

        this.txtIP = "127.0.0.2";
        this.txtPorta = "12347";
        this.txtNome = "Company";
        this.rotas = new ArrayList<Route>();
        this.executar = new ArrayList<Route>();
        this.executando = new ArrayList<Route>();
        // this.executadas = new ArrayList<Routes>();
        this.conta = new Account(2000000.00, "abc", "123");
        this.con = con;
        try {
            in  = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //conectar();
        Definir_percurso();
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
        bfw.write(this.txtNome+"\r\n");
        bfw.flush();
    }

    /***
     * Método usado para enviar mensagem para o server socket
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void enviarMensagem(String msg) throws Exception{
		Cryptography crpt = new Cryptography();
		msg = crpt.encrypt(msg, crpt.genKey(msg.length()));
        bfw.write(msg+"\r\n");    
        bfw.flush();
    }

    /**
     * Método usado para receber mensagem do servidor
     * @throws Exception
     */
    public void escutar(BufferedWriter bwSaida) throws Exception{

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while(!"Sair".equalsIgnoreCase(msg)){

                msg = bfr.readLine();
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
        try{
            System.out.println("RuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuUN");
            //enviarMensagem("Company_Conectada");
            String msg;
            OutputStream ou =  this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            clientes.add(bfw);
            //msg = bfr.readLine();
            //System.out.println(msg);
            Cryptography crpt = new Cryptography();
            //sendRoutes(bfw);
            // while(!"Sair".equalsIgnoreCase(msg) && msg != null)
            // {
            //     msg = bfr.readLine();
            //     sendRoutes(bfw);
            //     System.out.println(msg);
            // }
            //escutar(bfw);


            if(bfr.ready()){
                msg = bfr.readLine();
                System.out.println(msg);
                msg = crpt.decrypt(msg, crpt.genKey(msg.length()));
                System.out.println(msg);
                JSONObject mensagem = new JSONObject(msg);
                if(mensagem.get("mensagem").equals("getrotas")){
                    sendRoutes(bfw);
                }
                if(mensagem.get("mensagem").equals("rota")){
                    executar.remove((Route) mensagem.get("rota"));//TIRAR ROTAAAA
                    executando.add((Route) mensagem.get("rota"));
                }
                if(mensagem.get("mensagem").equals("pagar")){
                    pagarDriver((Account) mensagem.get("conta"), 3.25, this.conta);
                    System.out.println(msg);
                }
                else{
                    System.out.println(msg);
                    }
                //}
            }

        }catch (Exception e) {
            e.printStackTrace();

        }
    }
    

    /***
     * MÃ©todo usado para enviar mensagem para todos os clients
     * @param bwSaida do tipo BufferedWriter
     * @param msg do tipo String
     * @throws Exception
     */
    public void sendRoutes(BufferedWriter bwSaida) throws  Exception
    {   
        Cryptography crpt = new Cryptography();
        System.out.println("sendRoute");
        String msg = distribuindo_rotas();
        bwSaida.write(crpt.encrypt(msg,crpt.genKey(msg.length()))+"\r\n");
        bwSaida.flush();
    }

    void pagarDriver(Account contaRecebendo,double valor, Account contaPagando){
        BotPayment botPayment = new BotPayment(contaRecebendo, valor);
        Thread t1 = new Thread(botPayment);
        t1.start();
    }
    public void Definir_percurso(){
        String uriItineraryXML = "sim/data/data_route.xml";
        //SumoStringList edge = new SumoStringList();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(uriItineraryXML);
			NodeList nList = doc.getElementsByTagName("vehicle");
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) nNode;
					String idRouteAux = String.valueOf(i);
					Node node = elem.getElementsByTagName("route").item(0);
					Element edges = (Element) node;
                    rotas.add(new Route(idRouteAux, edges.getAttribute("edges")));
                    executar.add(new Route(idRouteAux, edges.getAttribute("edges")));
                }
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
    System.out.println("definir percurso " +rotas.size());
    }
    
    private String distribuindo_rotas(){
        Json json = new Json();
        System.out.println("destribuindo");
        System.out.println("MEU VETOR DE ROTAS: "+rotas.size());

        ArrayList<String> auxedges = new ArrayList<String>();
        ArrayList<String> auxid = new ArrayList<String>();
        for (int j=0;j<executar.size();j++){
            auxid.add(executar.get(j).getIdItinerary());
            auxedges.add(executar.get(j).getEdges());
        }
        System.out.println("MEU VETOR DE ROTAS: "+executar.size());
        return json.Json_Rotas(auxid, auxedges);
    }
    
    public static void main(String []args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                server = new ServerSocket(Integer.parseInt("12346"));
                clientes = new ArrayList<BufferedWriter>();
                while(true){
                    System.out.println("Aguardando conex�o...");
                    Socket con = server.accept();
                    System.out.println("Cliente conectado...");
                    Thread t = new Company(con);
                    t.start();
                }

                }catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

        thread.start();
        
    }
    class BotPayment extends Thread{
        private Account conta_recebendo;

        private Double valor;
        private boolean on_off;
        public BotPayment(Account contaRecebendo,double valor){
            this.conta_recebendo = contaRecebendo;
            this.valor = valor;
            this.on_off = true;
        }

        @Override
        public void run() {
            while(getOn_off()){
                Json json = new Json();

                try {
                    if(conta.getSaldo(conta.getLogin(), conta.getSenha()) < valor){
                            System.out.println("Saldo insuficiente.");
                    
                        }else{
                            conta.setSaldo(conta.getSaldo(conta.getLogin(), conta.getSenha())-valor,conta.getLogin(), conta.getSenha());
                            enviarMensagem(json.Json_pagamento(conta, conta_recebendo, valor));
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // conta.setSaldo(conta.getSaldo(conta.getLogin(), conta.getSenha())-this.valor,conta.getLogin(), conta.getSenha());
                // conta_recebendo.setSaldo(conta_recebendo.getSaldo(conta_recebendo.getLogin(), conta_recebendo.getSenha())-this.valor,conta_recebendo.getLogin(), conta_recebendo.getSenha());
            } 
        }
        

        public void setOn_off(){
            this.on_off = !this.on_off;
        }

        public boolean getOn_off(){
            return this.on_off;
        }
    } 

      
} 

