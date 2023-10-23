package demo.src.main.java.com.avancada;
import java.io.IOException;
import java.util.ArrayList;
import it.polito.appeal.traci.SumoTraciConnection;

public class Main {

    public SumoTraciConnection ativarSumo(){
        String sumo_bin = "sumo-gui";		
		String config_file = "sim/map/map.sumo.cfg";
		SumoTraciConnection sumo;
        sumo = new SumoTraciConnection(sumo_bin, config_file);
		sumo.addOption("start", "1"); // auto-run on GUI show
		sumo.addOption("quit-on-end", "1"); // auto-close on end
        return sumo;
    }

    public static void main(String[] args) throws Exception {  
        double fuelPrice = 5.87;
        ArrayList<Driver> motoristas = new ArrayList<Driver>();
		int fuelType = 2; // fuelType: 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid

        Main principal = new Main();
        SumoTraciConnection sumo;
        sumo = principal.ativarSumo();


        try {
            sumo.runServer(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread (new Runnable() {
            public void run() {
                while (!sumo.isClosed()) {
                    try {
                        sumo.do_timestep();  
                        Thread.sleep(500);					
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();

        AlphaBank.main(args);
        Company.main(args);

        for(int i=0; i<1;i++){
            Car carro = new Car(true, ("CAR"+i) , sumo,fuelType, fuelPrice, i);
            Driver motorista = new Driver(carro,  sumo, ("Motorista"+i), i, i*9, (i*9)+9);
            motorista.start();
            Thread.sleep(100);
            motoristas.add(motorista);         
        }


        for (int i=0; i<motoristas.size();i++){
            motoristas.get(i).join();  
        }
        
    } // Fim do m�todo main
    
}