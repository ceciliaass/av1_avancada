package demo.src.main.java.com.avancada;

import java.io.IOException;

//import java.io.BufferedWriter;

import java.net.ServerSocket;
import java.net.Socket;
//import java.util.ArrayList;
import java.util.ArrayList;

import de.tudresden.sumo.objects.SumoColor;
import io.sim.Auto;
import it.polito.appeal.traci.SumoTraciConnection;


public class Main {
    //private static ArrayList<BufferedWriter>clientes;
    private static ServerSocket server;
    //private Socket con;

    public SumoTraciConnection ativarSumo(){
        String sumo_bin = "sumo-gui";		
		String config_file = "map/map.sumo.cfg";
		SumoTraciConnection sumo;
        sumo = new SumoTraciConnection(sumo_bin, config_file);
		sumo.addOption("start", "1"); // auto-run on GUI show
		sumo.addOption("quit-on-end", "1"); // auto-close on end
        return sumo;
    }

    public static void main(String[] args) throws Exception {  
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

        // for (int i=0; i<carros.size();i++){
        //     //ccarros.get(i).join();
        //     //motoristas.get(i).join();
            
        // }
        // MobilityCompany.escutar();
    } // Fim do m�todo main
    
}
