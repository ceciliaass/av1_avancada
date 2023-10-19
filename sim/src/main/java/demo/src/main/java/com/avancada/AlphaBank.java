package demo.src.main.java.com.avancada;

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
import java.util.ArrayList;

import org.json.JSONObject;

public class AlphaBank extends Thread{
    private static ArrayList<BufferedWriter>clientes;
    private ArrayList<Account> contas;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;
    private static ServerSocket server;

    /**
     * MÃ©todo construtor
     * @param contas
     * @param com do tipo Socket
     */
    public AlphaBank(Socket con, ArrayList<Account> contas) throws IOException{
        this.con = con;
        this.contas = contas;
        try {
            in  = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * MÃ©todo run
     */
    public void run(){

        try{

            String msg;
            OutputStream ou =  this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            clientes.add(bfw);
            //System.out.println(bfw);
            nome = msg = bfr.readLine();
            Cryptography crpt = new Cryptography();
            //while(!"Sair".equalsIgnoreCase(msg)) // && msg != null
            //{
            if(bfr.ready()){
                msg = bfr.readLine();
                //System.out.println(msg);
                msg = crpt.decrypt(msg, crpt.genKey(msg.length()));
                //System.out.println(msg);
                JSONObject mensagem = new JSONObject(msg);
                if(mensagem.get("mensagem").equals("ver saldo")){
                    sendSaldo(bfw, getSaldo((int) mensagem.get("conta")));
                }
                if(mensagem.get("mensagem").equals("transacao")){
                    saque( Double.parseDouble(mensagem.get("valor").toString()), (int) mensagem.get("conta_pagando"));
                    deposito( Double.parseDouble(mensagem.get("valor").toString()), (int) mensagem.get("conta_recebendo"));
                    sendToAll(bfw, msg);
                    System.out.println("saldooooooooooo pagando" + getSaldo((int) mensagem.get("conta_pagando")));
                }
            }

        }catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void sendSaldo(BufferedWriter bwSaida, double msg) throws  IOException
    {
        BufferedWriter bwS;
        bwSaida.write(msg+"\r\n");
        bwSaida.flush();
        }
    

    /***
     * MÃ©todo usado para enviar mensagem para todos os clients
     * @param bwSaida do tipo BufferedWriter
     * @param msg do tipo String
     * @throws IOException
     */
    public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException
    {
    
            bwSaida.write(nome + " -> " + msg+"\r\n");
            bwSaida.flush();
//            }
        }
    

    private void saque(Double valor, int i){
		if(contas.get(i).getSaldo(contas.get(i).getLogin(), contas.get(i).getSenha()) < valor){
            System.out.println("Saldo insuficiente.");

        }else{
            contas.get(i).setSaldo(contas.get(i).getSaldo(contas.get(i).getLogin(), contas.get(i).getSenha())-valor,contas.get(i).getLogin(), contas.get(i).getSenha());
        }
        contas.get(i).start();
    }

    private void deposito(Double valor, int i){
        contas.get(i).setSaldo(contas.get(i).getSaldo(contas.get(i).getLogin(), contas.get(i).getSenha())+valor,contas.get(i).getLogin(), contas.get(i).getSenha());
        contas.get(i).start();
    }

    private double getSaldo(int i){
        return contas.get(i).getSaldo(contas.get(i).getLogin(), contas.get(i).getSenha());
    }

    public static void main(String []args) {
        ArrayList<Account> contass = new ArrayList<Account>();
        for(int i=0;i<100;i++){
            contass.add(new Account(50.0, "abc", "123"));
            //contass.get(i).start();
        }
        contass.add(new Account(500000.0, "abc", "123"));
       // contass.get(100).start();

        contass.add(new Account(50.0, "abc", "123"));
       // contass.get(101).start();

        // for(int i=0;i<102;i++){
        //     try {
        //         contass.get(i).join();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                server = new ServerSocket(Integer.parseInt("12347"));
                clientes = new ArrayList<BufferedWriter>();
                
                while(true){
                    System.out.println("AlphaBank aguardando conex�o...");
                    Socket con = server.accept();
                    System.out.println("Cliente conectado...");
                    Thread t = new AlphaBank(con, contass);
                    t.start();
                }

                }catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

        thread.start();

    } // Fim do m�todo main
    
}
