package demo.src.main.java.com.avancada;

public class Account implements Runnable {
    private String login;
    private String senha;
    private double saldo;
    //private int conta;
    //public static int num_conta=1;

    public Account(double saldo, String login, String senha){
        this.saldo = saldo;
        this.login=login;
        this.senha = senha;
        // this.conta=num_conta;
        // Account.num_conta = Account.num_conta + 1;


    }

    public synchronized void setSaldo(double saldo, String login, String senha){
        if ((this.login == login) && (this.senha==senha)){
            this.saldo = saldo;
        }else{
            System.out.println("Acesso incorreto");
        }

    }

    public synchronized double getSaldo(String login, String senha){
        if ((this.login == login) && (this.senha==senha)){
            return saldo;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
