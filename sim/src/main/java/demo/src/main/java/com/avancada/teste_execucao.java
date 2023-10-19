package demo.src.main.java.com.avancada;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
// import java.net.ServerSocket;
// import java.net.Socket;
import java.util.ArrayList;

// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoStringList;
import io.sim.Auto;
import it.polito.appeal.traci.SumoTraciConnection;

public class teste_execucao {
// 	//private static ServerSocket server;
//     //private SumoTraciConnection sumo;
    public static void main(String[] args) throws IOException { 
// 		// int cont=0;
// 		// ArrayList<Routes>executar = new ArrayList<Routes>(); 
// 		// String uriItineraryXML = "sim/data/data_route.xml";
//         // SumoStringList edge = new SumoStringList();
// 		// try {
// 		// 	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// 		// 	DocumentBuilder builder = factory.newDocumentBuilder();
// 		// 	Document doc = builder.parse(uriItineraryXML);
// 		// 	NodeList nList = doc.getElementsByTagName("vehicle");
// 		// 	for (int i = 0; i < nList.getLength(); i++) {
// 		// 		Node nNode = nList.item(i);
// 		// 		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
// 		// 			Element elem = (Element) nNode;
// 		// 			String idRouteAux = String.valueOf(i);
// 		// 			Node node = elem.getElementsByTagName("route").item(0);
// 		// 			Element edges = (Element) node;
//         //             // for (String e : edges.getAttribute("edges").split(" ")) {
//         //             //     edge.add(e);
//         //             // }
//         //             // System.out.println(edge.size());
//         //             executar.add(new Routes(idRouteAux, edges));
//         //             edge.clear();
// 		// 			cont++;
//         //         }
// 		// 	}

// 		// } catch (SAXException e) {
// 		// 	System.out.println("1");
// 		// 	e.printStackTrace();
// 		// } catch (IOException e) {
// 		// 	System.out.println("2");
// 		// 	e.printStackTrace();
// 		// } catch (ParserConfigurationException e) {
// 		// 	System.out.println("3");
// 		// 	e.printStackTrace();
// 		// }
// 		// int fuelType = 2;
//         // int fuelPreferential = 2;
//         // double fuelPrice = 5.87;
//         // int personCapacity = 1;
//         // int personNumber = 1;
//         // SumoColor green = new SumoColor(0, 255, 0, 126);
// 		// String sumo_bin = "sumo-gui";		
// 		// String config_file = "sim/map/map.sumo.cfg";
// 		// SumoTraciConnection sumo;
// 		// sumo = new SumoTraciConnection(sumo_bin, config_file);
// 		// sumo.addOption("start", "1"); // auto-run on GUI show
// 		// sumo.addOption("quit-on-end", "1"); // auto-close on end
// 		// sumo.runServer(12345);
// 		// 	for(int i =0; i<executar.size();i++){
// 		// 		//ArrayList<Routes> aux = new ArrayList<Routes>();
				
// 		// 		Auto carro;
// 		// 		try {
// 		// 			carro = new Auto(true, executar.get(i).getIdItinerary(), green,executar.get(i).getIdItinerary(), sumo, 500, fuelType, fuelPreferential, fuelPrice, personCapacity, personNumber);
// 		// 			for (String e : executar.get(i).getEdges().getAttribute("edges").split(" ")) {
//         //                 edge.add(e);
//         //             }
// 		// 			System.out.println(edge.size());
		
// 		// 			sumo.do_job_set(Route.add(executar.get(i).getIdItinerary(), edge));
					
// 		// 			edge.clear();
// 		// 			System.out.println(edge.size());
// 		// 			//sumo.do_job_set(Vehicle.add(this.auto.getIdAuto(), "DEFAULT_VEHTYPE", this.itinerary.getIdItinerary(), 0,
// 		// 			//		0.0, 0, (byte) 0));
// 		// 			System.out.println("passei aqui 2");
// 		// 			sumo.do_job_set(Vehicle.addFull(carro.getIdAuto(), 				//vehID
// 		// 											executar.get(i).getIdItinerary(), 	//routeID 
// 		// 											"DEFAULT_VEHTYPE", 					//typeID 
// 		// 											"now", 								//depart  
// 		// 											"0", 								//departLane 
// 		// 											"0", 								//departPos 
// 		// 											"0",								//departSpeed
// 		// 											"current",							//arrivalLane 
// 		// 											"max",								//arrivalPos 
// 		// 											"current",							//arrivalSpeed 
// 		// 											"",									//fromTaz 
// 		// 											"",									//toTaz 
// 		// 											"", 								//line 
// 		// 											carro.getPersonCapacity(),		//personCapacity 
// 		// 											carro.getPersonNumber())		//personNumber
// 		// 					);
					
// 		// 			sumo.do_job_set(Vehicle.setColor(carro.getIdAuto(), carro.getColorAuto()));
// 		// 			((Auto) carro).run();
// 		// 			sumo.do_timestep();
					
// 		// 		} catch (IOException e) {
					
// 		// 			e.printStackTrace();
// 		// 		} catch (Exception e) {
// 		// 			System.out.println("5");
// 		// 			e.printStackTrace();
// 		// 		}
// 		// 	}
//     Cryptography crpt = new Cryptography();
//     for(int i=0; i<3;i++){
	
// 		try {
// 			String msg = "A barata diz que tem";
// 			String en = crpt.encrypt(msg, crpt.genKey(msg.length()));
// 			System.out.println(en);
			
// 			System.out.println(crpt.decrypt(en, crpt.genKey(en.length())));
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 		}
// // 		Company conm = new Company();
//         }



	// String excelFilePath = "sim/data/relatorio.xlsx";
	// FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	// XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	// int rowCount = workbook.getSheetAt(0).getPhysicalNumberOfRows();
	// Row row = workbook.getSheetAt(0).createRow(rowCount);
	// int cellnum = 0;
	// Cell timestamp = row.createCell(cellnum++);
	// timestamp.setCellValue((long) System.currentTimeMillis() * 1000000);

	// Cell IdCar = row.createCell(cellnum++);
	// IdCar.setCellValue((String) "IDcar");

	// Cell IdRoute = row.createCell(cellnum++);
	// IdRoute.setCellValue((String) "IDroute");

	// Cell Speed = row.createCell(cellnum++);
	// Speed.setCellValue((double) 6.25);

	// Cell Distance = row.createCell(cellnum++);
	// Distance.setCellValue((double) 6.25);

	// Cell FuelConsumption = row.createCell(cellnum++);
	// FuelConsumption.setCellValue((double) 6.25);

	// Cell FuelType = row.createCell(cellnum++);
	// FuelType.setCellValue((double)  6.25);

	// Cell CO2Emission = row.createCell(cellnum++);
	// CO2Emission.setCellValue((double)  6.25);

	// Cell Lat = row.createCell(cellnum++);
	// Lat.setCellValue((double)  6.25);

	// Cell Lon = row.createCell(cellnum++);
	// Lon.setCellValue((double)  6.25);
	// FileOutputStream out = new FileOutputStream(new File(excelFilePath));
	// workbook.write(out);
	// out.close();
	// workbook.close();
	// System.out.println("Arquivo Excel criado com sucesso!"); 
	double fuelPrice = 5.87;
        // fuelType: 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
        ArrayList<Car> carros = new ArrayList<Car>();
        ArrayList<Driver> motoristas = new ArrayList<Driver>();
		int fuelType = 2;
        Main principal = new Main();
        SumoTraciConnection sumo;
        sumo = principal.ativarSumo();
        try {
            sumo.runServer(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
	AlphaBank.main(args);

        Company.main(args);
        //FuelStation posto = new FuelStation();
        Cryptography crpt = new Cryptography();
        for(int i=0; i<1;i++){
            //Account conta = new Account(50.0, "abc", "123");
            Car carro = new Car(true, ("455k"+i) /*,("D"+i)*/, sumo,fuelType, fuelPrice, i);
            //carro.Solicita_rotas();
            System.out.println(sumo);
            Driver motorista = new Driver(carro,  sumo, ("Motorista"+i), i, i*9, (i*9)+9);
            motorista.conectar();
            motorista.start();
            //Thread.sleep(5000);
            motoristas.add(motorista);

        // //     motorista.carro.Solicita_rotas();
        // // //     motorista.enviarMensagem("socorro"+i);
        // //     //motorista.escutar();
        // //     // // motorista.start();
        //     Json json = new Json();
		//     String msg= json.Json_mensagens("deus");
        //     carro.enviarMensagem(msg);
            
        }

    }
}

    

