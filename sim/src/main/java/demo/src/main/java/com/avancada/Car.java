package demo.src.main.java.com.avancada;

import java.io.IOException;

import de.tudresden.sumo.cmd.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;

import it.polito.appeal.traci.SumoTraciConnection;

public class Car extends Vehicle implements Runnable {
    
	private Socket socket;
    private OutputStream ou ;
    private Writer ouw;
    private BufferedWriter bfw;
    private String txtIP;
    private String txtPorta;

	private Double fuelTank = 10.0;
	private String idAuto;
	private SumoColor colorAuto;

	private SumoTraciConnection sumo;

	private boolean on_off;

	private int fuelType; 			// 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
	private double fuelPrice; 		// price in liters
	private int personCapacity;		// the total number of persons that can ride in this vehicle
	private int personNumber;		// the total number of persons which are riding in this vehicle
	private double distancia;

	private String rota_atual;
	private int contaDriver;
	public Car(boolean _on_off, String _idAuto, SumoTraciConnection _sumo, int fuelType, double _fuelPrice, int contaDriver) throws IOException {
		super(); 
        this.txtIP = ("127.0.0.1");
        this.txtPorta = "12346";

		this.on_off = _on_off;
		this.idAuto = _idAuto;
		
		this.sumo = _sumo;
		this.contaDriver = contaDriver;
		this.fuelPrice = _fuelPrice;
		this.fuelType = fuelType;
		this.distancia =0;
	}

	@Override
	public void run() {
		System.out.println("Run thread carro: "+getIdAuto()+" " + System.nanoTime());
		try {
			while(!this.getSumo().isClosed() && !((SumoStringList) sumo.do_job_get(Vehicle.getIDList())).contains(this.getIdAuto())){}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			while (!this.getSumo().isClosed() && ((SumoStringList) sumo.do_job_get(Vehicle.getIDList())).contains(this.getIdAuto())) {
				try {
					this.relatorio();
					// setFuelTank(fuelTank - ((double) sumo.do_job_get(getFuelConsumption(this.idAuto)))*5/770000);
					// System.out.println("Tank                                        "+fuelTank );
					Thread.sleep(200);				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fim run thread carro: "+getIdAuto()+" " + System.nanoTime());
		System.err.println();
	}

	private void relatorio() {

		try {
			if (!this.getSumo().isClosed() && ((SumoStringList) sumo.do_job_get(Vehicle.getIDList())).contains(this.getIdAuto())) {

				setFuelTank(fuelTank - ((double) sumo.do_job_get(getFuelConsumption(this.idAuto)))*5/770000);
				//System.out.println("Tank                                        "+fuelTank );
				//System.out.println("Distância percorrida: "+(double) sumo.do_job_get(getDistance(this.idAuto)));
				Json Json = new Json();
				SumoPosition2D posicion = (SumoPosition2D) sumo.do_job_get(getPosition(this.idAuto));
				double[] coord= converterGeo((double) posicion.x, (double) posicion.y);
				String json_relatorio= Json.Json_RelatorioCar(System.currentTimeMillis() * 1000000, this.idAuto, 
												(String) this.sumo.do_job_get(getRouteID(this.idAuto)), 
												(double) sumo.do_job_get(getSpeed(this.idAuto)), 
												(double) sumo.do_job_get(getDistance(this.idAuto)), 
												(double) sumo.do_job_get(getFuelConsumption(this.idAuto)), 
												this.fuelType, 
												(double) sumo.do_job_get(getCO2Emission(this.idAuto)), 
												(double) posicion.x, 
												(double) posicion.y);
				
				this.distancia();
				//this.RotaExecutada((String) this.sumo.do_job_get(getRouteID(this.idAuto)));
				//enviarMensagem(json_relatorio);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setVelocidade(double vel) throws Exception{
		sumo.do_job_set(Vehicle.setSpeed(this.idAuto, vel));
	}

	public int getRoute() throws Exception{
		return Integer.parseInt((String) this.sumo.do_job_get(getRouteID(this.idAuto)));
	}


	private void RotaExecutada(String rota) throws Exception{
		Json json = new Json();
		enviarMensagem(json.Json_RotaExecutando(rota));
	}
	private void distancia() throws Exception{
		if(!this.getSumo().isClosed()){
			if(distancia+100 <= (double) sumo.do_job_get(getDistance(this.idAuto))){
				Json json = new Json();
				
				enviarMensagem(json.Json_pagamentoDriver("pagar", this.contaDriver));
				distancia = distancia + 100;
			}
		}
	}


	private double[] converterGeo(Double x, Double y) {
		
		double EARTH_RADIUS = 6371.0;
	
		x = x*1000000000;
		y = y*1000000000;
		double z = Math.sqrt(Math.pow(EARTH_RADIUS, 2) - Math.pow(x, 2) - Math.pow(y, 2));
		double longitude = Math.atan(y / x);
		double latitude = Math.atan(z / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));


		return new double[] {latitude, longitude}; 
	}

	public boolean isOn_off() {
		return this.on_off;
	}

	public void setOn_off(boolean _on_off) {
		this.on_off = _on_off;
	}

	public String getIdAuto() {
		return this.idAuto;
	}

	public SumoTraciConnection getSumo() {
		return this.sumo;
	}

	public int getFuelType() {
		return this.fuelType;
	}

	public String getRotaAtual(){
		return rota_atual;
	}
	public void setFuelType(int _fuelType) {
		if((_fuelType < 0) || (_fuelType > 4)) {
			this.fuelType = 4;
		} else {
			this.fuelType = _fuelType;
		}
	}

	public double getFuelPrice() {
		return this.fuelPrice;
	}

	public void setFuelPrice(double _fuelPrice) {
		this.fuelPrice = _fuelPrice;
	}

	public SumoColor getColorAuto() {
		return this.colorAuto;
	}

	public int getPersonCapacity() {
		return this.personCapacity;
	}

	public int getPersonNumber() {
		return this.personNumber;
	}

	public Double getFuelTank(){
		return fuelTank;
	}
	public void setFuelTank(Double fuelTank){

		this.fuelTank = fuelTank;
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
		
        bfw.flush();
    }

	/***
     * Método usado para enviar mensagem para o server socket
     * @param msg do tipo String
	 * @throws Exception
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
	public ArrayList<Route> Solicita_rotas(int inf, int sup) throws Exception{
		
		Json json = new Json();
		enviarMensagem(json.Json_mensagens("getrotas"));

		JSONObject rotas = new JSONObject(escutar()); 
		
		JSONArray arrayIds = (JSONArray) rotas.get("Idrotas");
		JSONArray arrayEdges = (JSONArray) rotas.get("edges");

		ArrayList<Route> conjuntorotas = new ArrayList<Route>();

		for(int i=inf;i< sup ; i++){
			conjuntorotas.add(new Route((String) arrayIds.get(i),(String) arrayEdges.get(i)));
		}
		
		return conjuntorotas;
	}

    public void enviarMensagem(String msg) throws Exception{
		conectar();
		Cryptography crpt = new Cryptography();
		msg = crpt.encrypt(msg, crpt.genKey(msg.length()));
        bfw.write(msg+"\r\n");    
        bfw.flush();
		
    }

    /**
     * Método usado para receber mensagem do servidor
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public String escutar() throws IOException{
		Cryptography crpt = new Cryptography();
	
        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);

		String txt="";
		
		txt= bfr.readLine();
		txt = crpt.decrypt(txt, crpt.genKey(txt.length()));
        
		return txt;
    }

    /***
     * Método usado quando o usu�rio clica em sair
     * @throws Exception
     */
    public void sair() throws Exception{
		Json json = new Json();
		enviarMensagem(json.Json_mensagens("Sair"));
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }



}