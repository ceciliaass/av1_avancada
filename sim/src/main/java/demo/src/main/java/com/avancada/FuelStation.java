package demo.src.main.java.com.avancada;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import org.json.JSONObject;

public class FuelStation extends  Thread{
    //private int bombas;
    private int conta;
    private boolean on_off;
    private double quant_abastecimento;
    private Socket socket;
    private OutputStream ou ;
    private Writer ouw;
    private BufferedWriter bfw;
    private String txtIP;
    private String txtPorta;
    private String txtNome;
    private Driver cliente;
    private static Boolean bomba1=true;
    private static Boolean bomba2=true;
    private static Boolean espera=false;
    public FuelStation(Driver cliente) throws IOException{

        this.quant_abastecimento = 0;
        this.txtIP ="127.0.0.2";
        this.txtPorta = "12347";
        this.txtNome = "FuelStation";
        this.cliente = cliente;
        this.conta = 101;
        //this.bombas = 2;
        this.on_off = true;
        conectar();
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
		Cryptography crpt = new Cryptography();
		msg=crpt.encrypt(msg, crpt.genKey(msg.length()));

        bfw.write(msg+"\r\n");    
        bfw.flush();
    }

    /**
     * Método usado para receber mensagem do servidor
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void escutar() throws IOException{

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";
        System.out.println("escutaando3");
        Cryptography crpt = new Cryptography();
        //while(!"Sair".equalsIgnoreCase(msg))

            if(bfr.ready()){
                msg = bfr.readLine();
                msg = crpt.decrypt(msg, crpt.genKey(msg.length()));
                System.out.println("FUEEEEEL RECEBEU O SALDO");
                JSONObject mensagem = new JSONObject(msg);
                if((int) mensagem.get("conta_recebendo")==this.conta){
                    if(mensagem.get("mensagem").equals("ver saldo")){
                        quant_abastecimento = (Double.parseDouble(mensagem.get("saldo").toString()));
                        System.out.println("Abasteeeece" + quant_abastecimento);
                    }
                    if(mensagem.get("mensagem").equals("transacao")){
                        System.out.println(" PAGAMENTO RECEBIDO");
                    }
                }
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
        double qtd_combustivel = 0;
        //while(getOn_off()){
                //abastecer
                try {
                    cliente.setAbastecendo(true);
                    Json json = new Json();
		            enviarMensagem(json.Json_versaldo(cliente.getConta()));
                    //System.out.println("escutaando1");
                    escutar();
                    //System.out.println("escutaando2");
                } catch (Exception e) {
                    e.printStackTrace();
                } 

               qtd_combustivel = this.quant_abastecimento/5.87;
               cliente.getCar().setFuelTank(qtd_combustivel + cliente.getCar().getFuelTank());
               cliente.setAbastecendo(false);
               System.out.println(qtd_combustivel);
               //sacar da conta do driver
               cliente.setfuelDivida(this.conta, this.quant_abastecimento);
               System.out.println("enviei a divida");
               if(!bomba1) {setbomba1(); if(espera) {notifyAll();}}
               if(!bomba2) {setbomba2(); if(espera) {notifyAll();}}
                }
        //}

   
    public void setOn_off(){
        this.on_off = !this.on_off;
    }

    public boolean getOn_off(){
        return this.on_off;
    }

    public static Boolean getbomba1(){
        return bomba1;
    }

    public static Boolean getbomba2(){
        return bomba1;
    }

    public synchronized static void setbomba1(){
        bomba1=!bomba1;
    }
    public synchronized static void setbomba2(){
        bomba2=!bomba2;
    }

    public synchronized static void setespera(Boolean espera){
        espera = espera;
    }
}