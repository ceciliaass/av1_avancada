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
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;
    private static ServerSocket server;

    /**
     * MÃ©todo construtor
     * @param com do tipo Socket
     */
    public AlphaBank(Socket con) throws IOException{
        this.con = con;
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
            System.out.println(bfw);
            nome = msg = bfr.readLine();
            Cryptography crpt = new Cryptography();
            while(!"Sair".equalsIgnoreCase(msg) && msg != null)
            {
                msg = bfr.readLine();
                System.out.println(msg);
                msg = crpt.decrypt(msg, crpt.genKey(msg.length()));
                System.out.println(msg);
                JSONObject mensagem = new JSONObject(msg);
                //saque((Account) mensagem.get("conta_pagando"), (double) mensagem.get("valor"));
                //deposito((Account) mensagem.get("conta_recebendo"), (double) mensagem.get("valor"));
                sendToAll(bfw, msg);
                System.out.println(msg);
            }

        }catch (Exception e) {
            e.printStackTrace();

        }
    }

    /***
     * MÃ©todo usado para enviar mensagem para todos os clients
     * @param bwSaida do tipo BufferedWriter
     * @param msg do tipo String
     * @throws IOException
     */
    public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException
    {
        BufferedWriter bwS;
        System.out.println("send");
        for(BufferedWriter bw : clientes){
             System.out.println("sendq1");
            System.out.println("send2");
            bw.write(nome + " -> " + msg+"\r\n");
            bw.flush();
            }
        }
    

    // private void saque(Account conta, Double valor){
	
	// 	if(conta.getSaldo(conta.getLogin(), conta.getSenha()) < valor){
    //         System.out.println("Saldo insuficiente.");

    //     }else{
    //         conta.setSaldo(conta.getSaldo(conta.getLogin(), conta.getSenha())-valor,conta.getLogin(), conta.getSenha());
    //     }

    // }

    // private void deposito(Account conta, Double valor){
    //     conta.setSaldo(conta.getSaldo(conta.getLogin(), conta.getSenha())+valor,conta.getLogin(), conta.getSenha());
    // }

    public static void main(String []args) {
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
                    Thread t = new AlphaBank(con);
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
