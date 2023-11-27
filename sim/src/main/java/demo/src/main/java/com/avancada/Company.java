package demo.src.main.java.com.avancada;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Company extends Thread {
    private static ServerSocket server;
    int conta;
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

    private ArrayList<Route>executar; 
    private ArrayList<Route>executando; 
    private ArrayList<Route>executadas; 
    static SumoTraciConnection sumo;

    public Company(Socket con) throws IOException{

        this.txtIP = "127.0.0.2";
        this.txtPorta = "12347";
        this.txtNome = "Company";

        this.executar = new ArrayList<Route>();
        this.executando = new ArrayList<Route>();
        this.executadas = new ArrayList<Route>();
        this.conta = 100;
        this.con = con;
        try {
            in  = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        conectar();
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
        conectar();
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
            String msg;
            OutputStream ou =  this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            Cryptography crpt = new Cryptography();
            while(true){
            if(bfr.ready()){
                msg = bfr.readLine();
                msg = crpt.decrypt(msg, crpt.genKey(msg.length()));
                JSONObject mensagem = new JSONObject(msg);
                if(mensagem.get("mensagem").equals("getrotas")){
                    sendRoutes(bfw);
                }
                // if(mensagem.get("mensagem").equals("rota")){
                //     if(executar.size()==900){
                //         addExecutando(executar.get(Integer.parseInt(mensagem.get("rota").toString())));
                //         delExecutar(Integer.parseInt(mensagem.get("rota").toString()));
                //     }else{
                //     addExecutadas(executando.get(executando.size()-1));
                //     delExecutando(executando.size()-1);
                //     delExecutar(Integer.parseInt(mensagem.get("rota").toString()));
                //     addExecutando((Route) mensagem.get("rota"));
                //     }
                // }
                if(mensagem.get("mensagem").equals("pagar")){
                    pagarDriver((int) mensagem.get("conta"), this.conta);
                }
                else if(mensagem.get("mensagem").equals("relatorio")){
                    Gerar_relatorio(mensagem);
                    }
            }
        }
        }catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    /**
     * @param mensagem
     * @throws IOException
     * @throws InterruptedException
     */
    private synchronized  static void Gerar_relatorio(JSONObject mensagem) throws IOException, InterruptedException{

        String excelFilePath = "sim/data/relatorio.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        int rowCount = workbook.getSheetAt(0).getPhysicalNumberOfRows();
        Row row = workbook.getSheetAt(0).createRow(rowCount);
        int cellnum = 0;
        Cell timestamp = row.createCell(cellnum++);
        timestamp.setCellValue((long) mensagem.get("Timestamp"));

        Cell IdCar = row.createCell(cellnum++);
        IdCar.setCellValue((String) mensagem.get("IDcar"));

        Cell IdRoute = row.createCell(cellnum++);
        IdRoute.setCellValue((String) mensagem.get("IDroute"));

        Cell Speed = row.createCell(cellnum++);
        Speed.setCellValue(Double.parseDouble(mensagem.get("Speed").toString()));

        Cell Distance = row.createCell(cellnum++);
        Distance.setCellValue(Double.parseDouble(mensagem.get("Distance").toString()));

        Cell FuelConsumption = row.createCell(cellnum++);
        FuelConsumption.setCellValue(Double.parseDouble(mensagem.get("FuelConsumption").toString()));

        Cell FuelType = row.createCell(cellnum++);
        FuelType.setCellValue(Double.parseDouble(mensagem.get("FuelType").toString()));

        Cell CO2Emission = row.createCell(cellnum++);
        CO2Emission.setCellValue(Double.parseDouble(mensagem.get("CO2Emission").toString()));

        Cell Lat = row.createCell(cellnum++);
        Lat.setCellValue(Double.parseDouble(mensagem.get("Lat").toString()));

        Cell Lon = row.createCell(cellnum++);
        Lon.setCellValue(Double.parseDouble(mensagem.get("Lon").toString()));
        FileOutputStream out = new FileOutputStream(new File(excelFilePath));
        workbook.write(out);
        out.close();
        workbook.close();
        Thread.sleep(200);
        // System.out.println("Arquivo Excel atualizado com sucesso!"); 
        
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
       
        String msg = distribuindo_rotas();
        bwSaida.write(crpt.encrypt(msg,crpt.genKey(msg.length()))+"\r\n");
        bwSaida.flush();
    }

    
    /**
     * @param contaRecebendo
     * @param valor
     * @param contaPagando
     */
    void pagarDriver(int contaRecebendo, int contaPagando){
        BotPayment botPayment = new BotPayment(contaRecebendo);
        Thread t1 = new Thread(botPayment);

        t1.start();
    }

    public void Definir_percurso(){
        String uriItineraryXML = "sim/data/data_route.xml";

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(uriItineraryXML);
			NodeList nList = doc.getElementsByTagName("vehicle");
			for (int i = 0; i < 100; i++) {
				Node nNode = nList.item(0);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) nNode;
					String idRouteAux = String.valueOf(i);
					Node node = elem.getElementsByTagName("route").item(0);
					Element edges = (Element) node;
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
    }
    
    private String distribuindo_rotas(){
        Json json = new Json();
       
        ArrayList<String> auxedges = new ArrayList<String>();
        ArrayList<String> auxid = new ArrayList<String>();
        for (int j=0;j<executar.size();j++){
            auxid.add(executar.get(j).getIdItinerary());
            auxedges.add(executar.get(j).getEdges());
        }
       
        return json.Json_Rotas(auxid, auxedges);
    }

    public synchronized void addExecutando(Route executar){
        this.executando.add(executar);
    }

    public synchronized void addExecutadas(Route executando){
        this.executadas.add(executando);
    }

    public synchronized void delExecutar(int executar){
        this.executar.remove(executar);
    }

    public synchronized void delExecutando(int executar){
        this.executando.remove(executar);
    }

    public synchronized void delExecutadas(int executando){
        this.executadas.remove(executando);
    }
    
    public static void main(String []args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                server = new ServerSocket(Integer.parseInt("12346"));
                while(true){
                    Socket con = server.accept();
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
        private int conta_recebendo;
        public BotPayment(int contaRecebendo){
            this.conta_recebendo = contaRecebendo;
        }

        @Override
        public void run() {
                Json json = new Json();

                try {     
                    enviarMensagem(json.Json_pagamento(conta, conta_recebendo,(double) 3.25));
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
        
    } 

      
} 

