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

	public String Json_RotaExecutando(Route rota){
		my_obj.put("mensagem", "rota");
		my_obj.put("rota", rota);
		return my_obj.toString();
	}

	public String Json_mensagens(String pedido){
		my_obj.put("mensagem", pedido);
		return my_obj.toString();
	}

	public String Json_pagamentoDriver(String mensagem, Account conta){
		my_obj.put("mensagem", mensagem);
		my_obj.put("conta", conta);
		return my_obj.toString();
	}

    public String Json_RelatorioCar(long timestamp, String IdCar, String IdRoute, Double Speed, Double distance, Double FuelConsumption, int FuelType, Double CO2Emission, Double Lat, Double Lon ){

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
		// System.out.println("objeto original -> " + json_string);
		// System.out.println();

		// //altera o titulo e imprime a nova configuração do objeto
		// my_obj.put("titulo", "JSON x XML: o Confronto das Linguagens");
		// json_string = my_obj.toString();
		
		// //recupera campo por campo com o método get() e imprime cada um
		// String titulo = my_obj.getString("titulo");
		// Integer ano = my_obj.getInt("ano");
		// String genero = my_obj.getString("genero");

		// System.out.println("titulo: " + titulo);
		// System.out.println("ano: " + ano);
		// System.out.println("genero: " + genero);
    }

	public String Json_pagamento(Account conta_pagando, Account conta_recebendo, Double valor) {
		my_obj.put("conta_pagando", conta_pagando);
		my_obj.put("conta_recebendo", conta_recebendo);
		my_obj.put("valor", valor);
		return my_obj.toString();

	}


}
