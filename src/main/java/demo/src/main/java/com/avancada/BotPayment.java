package demo.src.main.java.com.avancada;



public class BotPayment extends Thread{
    private Account conta_pagando;
    private Account conta_recebendo;

    private Double valor;
    private boolean on_off;
    public BotPayment(Account contaRecebendo,double valor, Account contaPagando){
        this.conta_recebendo = contaRecebendo;
        this.conta_pagando = contaPagando;
        this.valor = valor;
        this.on_off = true;
    }

    @Override
    public void run() {
        while(getOn_off()){
            conta_pagando.setSaldo(conta_pagando.getSaldo(conta_pagando.getLogin(), conta_pagando.getSenha())-this.valor,conta_pagando.getLogin(), conta_pagando.getSenha());
            conta_recebendo.setSaldo(conta_recebendo.getSaldo(conta_recebendo.getLogin(), conta_recebendo.getSenha())-this.valor,conta_recebendo.getLogin(), conta_recebendo.getSenha());
        } 
    }
    

    public void setOn_off(){
        this.on_off = !this.on_off;
    }

    public boolean getOn_off(){
        return this.on_off;
    }
}
