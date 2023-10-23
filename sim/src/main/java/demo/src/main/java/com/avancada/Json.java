package demo.src.main.java.com.avancada;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


public class Json{
	private JSONObject my_obj;
	public Json(){
		this.my_obj = new JSONObject();

	}

	public String Json_Comprovante(long timestamp, String IdCar, String IdRoute, Double Speed, Double distance, Double FuelConsumption, int FuelType, Double CO2Emission, Double Lat, Double Lon ){
		return "";
	}

	public String Json_Rotas(ArrayList<String> Idrotas, ArrayList<String> edges){
		JSONArray arrayrota = new JSONArray();
		for(int i=0; i<Idrotas.size();i++){
			arrayrota.put(Idrotas.get(i));
		}

		JSONArray arrayedges = new JSONArray();
			for(int i=0; i<edges.size();i++){
				arrayedges.put(edges.get(i));
		}

		my_obj.put("mensagem", "rotas");
		my_obj.put("Idrotas", arrayrota);
		my_obj.put("edges", arrayedges);
		return my_obj.toString();
	}

	public String Json_RotaExecutando(String rota){
		my_obj.put("mensagem", "rota");
		my_obj.put("rota", rota);
		return my_obj.toString();
	}

	public String Json_mensagens(String pedido){
		my_obj.put("mensagem", pedido);
		return my_obj.toString();
	}

	public String Json_versaldo(int conta){
		my_obj.put("mensagem", "ver saldo");
		my_obj.put("conta", conta);
		return my_obj.toString();
	}

	public String Json_pagamentoDriver(String mensagem, int conta){
		my_obj.put("mensagem", mensagem);
		my_obj.put("conta", conta);
		return my_obj.toString();
	}

	public String Json_pagamentoFuel(String mensagem, int conta){
		my_obj.put("mensagem", mensagem);
		my_obj.put("conta", conta);
		return my_obj.toString();
	}
    public String Json_RelatorioCar(long timestamp, String IdCar, String IdRoute, Double Speed, Double distance, Double FuelConsumption, int FuelType, Double CO2Emission, Double Lat, Double Lon ){
		my_obj.put("mensagem", "relatorio");
		//preenche o objeto com os campos: titulo, ano e genero
		my_obj.put("Timestamp", timestamp);
		my_obj.put("IDcar", IdCar);
		my_obj.put("IDroute", IdRoute);
		my_obj.put("Speed", Speed);
		my_obj.put("Distance", distance);
		my_obj.put("FuelConsumption", FuelConsumption);
		my_obj.put("FuelType", FuelType);
		my_obj.put("CO2Emission", CO2Emission);
		my_obj.put("Lat", Lat);
		my_obj.put("Lon", Lon);
		//serializa para uma string e imprime
		return my_obj.toString();
    }

	public String Json_pagamento(int conta_pagando, int conta_recebendo, Double valor) {
		my_obj.put("mensagem", "transacao");
		my_obj.put("conta_pagando", conta_pagando);
		my_obj.put("conta_recebendo", conta_recebendo);
		my_obj.put("valor", valor);
		return my_obj.toString();

	}


	public String Json_saldo(double saldo, int conta_perguntando){
		my_obj.put("mensagem", "ver saldo");
		my_obj.put("saldo", saldo);
		my_obj.put("conta_recebendo", conta_perguntando);
		return my_obj.toString();
	}
}
