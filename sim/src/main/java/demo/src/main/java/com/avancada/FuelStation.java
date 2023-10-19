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

        while(!"Sair".equalsIgnoreCase(msg))

            if(bfr.ready()){
                msg = bfr.readLine();
                System.out.println(msg);
                //JSONObject mensagem = new JSONObject(msg);
                // if((int) mensagem.get("conta_recebendo")==this.conta){
                //    System.out.println("pagamento recebido");
                // }
                // else{
                //     quant_abastecimento = (double)  mensagem.get("saldo motorista");
                // }
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
                    conectar();
                    Json json = new Json();
		            enviarMensagem(json.Json_versaldo(cliente.getConta()));
                    escutar();
                } catch (Exception e) {
                    e.printStackTrace();
                } 

               qtd_combustivel = this.quant_abastecimento/5.87;
               cliente.getCar().setFuelTank(qtd_combustivel + cliente.getCar().getFuelTank());
               //sacar da conta do driver
               cliente.setfuelDivida(this.conta, this.quant_abastecimento);
            }
        //}

   
    public void setOn_off(){
        this.on_off = !this.on_off;
    }

    public boolean getOn_off(){
        return this.on_off;
    }
}
