
package demo.src.main.java.com.avancada;

import java.io.IOException;

import de.tudresden.sumo.cmd.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;

import it.polito.appeal.traci.SumoTraciConnection;

public class Car_rec extends Vehicle implements Runnable {
    
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
	private double range_dist;
	private double[] y = new double[] {900.0, 109.6352103,	186.33918,	224.3269458,	112.0867752,	152.0420778};
	private double[] v = new double[] {0.5 , 9.502295778,	27.0553505,	51.32731668,	6.577071701,	25.90185436};
	private double[][] A = new double[][] {{1, -1, -1, -1, -1 ,- 1}};
	private long tempo_anterior;
	private double tempo_total;
	private String rota_atual;
	public Car_rec(boolean _on_off, String _idAuto, SumoTraciConnection _sumo, int fuelType, double _fuelPrice, int contaDriver) throws IOException {
		super(); 
        this.txtIP = ("127.0.0.1");
        this.txtPorta = "12346";

		this.on_off = _on_off;
		this.idAuto = _idAuto;
		
		this.sumo = _sumo;
		this.fuelPrice = _fuelPrice;
		this.fuelType = fuelType;
		this.distancia =0;
		this.range_dist = 0;
		tempo_total =0;
	}

	@Override
	public void run() {
		this.tempo_anterior = System.nanoTime();
		// System.out.println("teste");
		// try {
		// 	while(!this.getSumo().isClosed() && !((SumoStringList) sumo.do_job_get(Vehicle.getIDList())).contains(this.getIdAuto())){}
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// this.relatorio();
		// System.out.println("teste");
		while (!this.getSumo().isClosed()) {
			try {
				System.out.println("Distância percorrida: "+(double) sumo.do_job_get(getDistance(this.idAuto)));
				//this.relatorio();
				this.distancia_medidor();
				Thread.sleep(200);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void relatorio() {
		try {
			if (!this.getSumo().isClosed() && ((SumoStringList) sumo.do_job_get(Vehicle.getIDList())).contains(this.getIdAuto())) {
				//System.err.println("distanciaaaa_mediu");
				//setFuelTank(fuelTank - ((double) sumo.do_job_get(getFuelConsumption(this.idAuto)))*5/770000);
				//System.out.println("Tank                                        "+fuelTank );
				Json Json = new Json();
				SumoPosition2D posicion = (SumoPosition2D) sumo.do_job_get(getPosition(this.idAuto));
				String json_relatorio= Json.Json_RelatorioCar(System.nanoTime() - this.tempo_anterior, 
												this.idAuto, 
												(String) this.sumo.do_job_get(getRouteID(this.idAuto)), 
												(double) sumo.do_job_get(getSpeed(this.idAuto)), 
												this.range_dist*10, 
												(double) sumo.do_job_get(getFuelConsumption(this.idAuto)), 
												this.fuelType, 
												(double) sumo.do_job_get(getCO2Emission(this.idAuto)), 
												(double) posicion.x, 
												(double) posicion.y);

				enviarMensagem(json_relatorio);
				this.distancia_medidor();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setVelocidade(double vel) throws Exception{
		sumo.do_job_set(Vehicle.setSpeed(this.idAuto, vel));
	}

	public void setDistancia(double d) throws Exception{
		this.range_dist = d;
		this.distancia = d;
	}
	public int getRoute() throws Exception{
		return Integer.parseInt((String) this.sumo.do_job_get(getRouteID(this.idAuto)));
	}

	private void distancia_medidor() throws Exception{
		if (!this.getSumo().isClosed() && ((SumoStringList) sumo.do_job_get(Vehicle.getIDList())).contains(this.getIdAuto())) {
			System.out.println((double) sumo.do_job_get(getDistance(this.idAuto)) - this.distancia);
			if((double) sumo.do_job_get(getDistance(this.idAuto)) - this.distancia >= 300.00){ //(double) sumo.do_job_get(getDistance(this.idAuto)) == 0.00 ||
				this.range_dist = (double) sumo.do_job_get(getDistance(this.idAuto)) - this.distancia;
				this.distancia = (double) sumo.do_job_get(getDistance(this.idAuto));
				//relatorio();
				double v = getReconciliacao();
				System.out.println("v " +v);
				sumo.do_job_set(Vehicle.setSpeed(this.idAuto, v));
				//this.tempo_anterior = System.nanoTime();
				System.err.println("distanciaaaa_mediu");
			}
		}
	}

	private double getReconciliacao() throws Exception{

		double diferenca = (System.nanoTime() - this.tempo_anterior)/(1000000000.0);
		this.tempo_anterior = System.nanoTime();
		this.tempo_total += diferenca;
		System.out.println(this.tempo_total);
		System.out.println("diferenca " + diferenca);
		int novo_tam = y.length -1;
		double[] y_aux = new  double[novo_tam];
		double[][] A_aux = new double[1][novo_tam];
		double[] v_aux = new double[novo_tam];
		y_aux[0] = y[0] - diferenca;
		A_aux[0][0] = A[0][0];
		v_aux[0] = v[0];
		for (int i = 1; i < novo_tam; i++) {
            y_aux[i] = y[i+1];
			A_aux[0][i] = A[0][i+1];
			v_aux[i] = v[i+1];
        }
		if(novo_tam>=2){
			y_aux[1] += y[1]-diferenca;
		}
		y=null;
		y =y_aux;

		A = null;
		A = A_aux;

		v = null;
		v = v_aux;
		System.out.println(y[0]);
		Reconciliation rec = new Reconciliation(y, v, A);
		
        double[] res = rec.getReconciledFlow();
		System.out.println("novo :");
		rec.printMatrix(rec.getReconciledFlow());

        y= res;

		relatorio_reconciliação(900.0-tempo_total,tempo_total,diferenca, (double) sumo.do_job_get(getSpeed(this.idAuto)), y);
		return 3000/res[1];
	}

	private void relatorio_reconciliação(double resto, double gasto,double percurso, double vel_rec, double[] tempos) throws IOException{
		String excelFilePath = "sim/data/relatorio_reconciliacao.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        int rowCount = workbook.getSheetAt(0).getPhysicalNumberOfRows();
        Row row = workbook.getSheetAt(0).createRow(rowCount);
        int cellnum = 0;
        Cell timestamp = row.createCell(cellnum++);
        timestamp.setCellValue(resto);

        Cell IdCar = row.createCell(cellnum++);
        IdCar.setCellValue(gasto);

        Cell IdRoute = row.createCell(cellnum++);
        IdRoute.setCellValue(percurso);

        Cell Speed = row.createCell(cellnum++);
        Speed.setCellValue(vel_rec);


        // Crie células na linha e insira valores
        for (int i = 0; i < tempos.length; i++) {
            Cell tempo = row.createCell(cellnum++);
        	tempo.setCellValue(tempos[i]);
			
        }

        FileOutputStream out = new FileOutputStream(new File(excelFilePath));
        workbook.write(out);
        out.close();
        workbook.close();
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
