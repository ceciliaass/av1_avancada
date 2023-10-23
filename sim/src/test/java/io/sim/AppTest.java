package io.sim;

import static org.junit.Assert.assertTrue;

import org.json.JSONObject;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import demo.src.main.java.com.avancada.Account;
import demo.src.main.java.com.avancada.Json;
import demo.src.main.java.com.avancada.Route;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testJson_mensagem(){
        Json json = new Json();
        JSONObject esperado = new JSONObject();
        esperado.put("mensagem", "teste");

        assertEquals( esperado.toString(), json.Json_mensagens("teste"));
    }

    @Test
    public void testJson_pagamento(){
        Json json = new Json();
        JSONObject esperado = new JSONObject();
        esperado.put("mensagem", "transacao");
        esperado.put("conta_pagando", 1);
        esperado.put("conta_recebendo", 2);
        esperado.put("valor", 3.0);

        assertEquals( esperado.toString(), json.Json_pagamento(1,2,3.0));
    }

    @Test
    public void testGetLogin(){
        Account conta = new Account(0, "fName", "fName", 0);
        
        assertEquals("fName",conta.getLogin());
    }
    
    @Test
    public void testgGetSaldo(){
        Account conta = new Account(0, "fName", "fName", 0);
        
        assertEquals(0,conta.getSaldo("fName", "fName"),0);
    }

    @Test
    public void testGetId(){
        Route rota = new Route("1", "134216629#2");
        assertEquals("1",rota.getIdItinerary());
    }
}
