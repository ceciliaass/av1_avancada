package demo.src.main.java.com.avancada;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Account extends Thread {
    private String login;
    private String senha;
    private double saldo_atual;
    private int conta;
    private int conta_tranferencia;
    private String ultima_acao;
    private double ultima_transacao;


    public Account(double saldo_atual, String login, String senha, int conta){
        this.saldo_atual = saldo_atual;
        this.login=login;
        this.senha = senha;
        this.conta = conta;
        this.conta_tranferencia = 0;
        this.ultima_acao="";
        this.ultima_transacao = 0;



    }

    public synchronized void setSaldo(double saldo_atual, String login, String senha){
        if ((this.login == login) && (this.senha==senha)){
            this.saldo_atual = saldo_atual;
        }else{
            System.out.println("Acesso incorreto");
        }

    }

    public synchronized double getSaldo(String login, String senha){
        if ((this.login == login) && (this.senha==senha)){
            return saldo_atual;

        }
        else{
            System.out.print("Acesso incorreto");
            return 0;
        }
    }

    public String getLogin(){
        return login;
    }

    public String getSenha(){
        return senha;
    }

    @Override
    public void run() {
        try {
            String excelFilePath = "sim/data/transações.xlsx";
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            int rowCount = workbook.getSheetAt(0).getPhysicalNumberOfRows();
            Row row = workbook.getSheetAt(0).createRow(rowCount);
            int cellnum = 0;
            Cell timestamp = row.createCell(cellnum++);
            timestamp.setCellValue((long) System.currentTimeMillis() * 1000000);

            Cell conta1 = row.createCell(cellnum++);
            conta1.setCellValue(this.conta);

            Cell acao = row.createCell(cellnum++);
            acao.setCellValue(this.ultima_acao);

            Cell valor = row.createCell(cellnum++);
            valor.setCellValue(ultima_transacao);

            Cell conta2 = row.createCell(cellnum++);
            conta2.setCellValue(conta_tranferencia);

            Cell saldo = row.createCell(cellnum++);
            saldo.setCellValue(this.saldo_atual);

            FileOutputStream out = new FileOutputStream(new File(excelFilePath));
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public void recebe_ContaTranferencia(String acao , double valor, int conta_transf ){
        this.conta_tranferencia= conta_transf;
        this.ultima_acao=acao;
        this.ultima_transacao = valor;
    }
    
}
