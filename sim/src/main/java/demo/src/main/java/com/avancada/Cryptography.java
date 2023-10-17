package demo.src.main.java.com.avancada;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Random;

import javax.crypto.Cipher;

public class Cryptography {

    // private static String IV;
    // private static String chaveencriptacao;

    public Cryptography(){
    }

    // public String encrypt(String textopuro) throws Exception {
    //     Cipher encripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
    //     SecretKeySpec key = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), "AES");
    //     encripta.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    //     return encripta.doFinal(textopuro.getBytes("UTF-8")).toString();
    // }
     
    // public String decrypt(String textoencriptado) throws Exception{
    //     Cipher decripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
    //     SecretKeySpec key = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), "AES");
    //     decripta.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    //     return new String(decripta.doFinal(textoencriptado.getBytes()),"UTF-8");
        
    //   }



    public String encrypt(String mensagem, String chave) {
    if (mensagem.length() != chave.length())
    error("O tamanho da mensagem e da chave devem ser iguais.");
    int[] im = charArrayToInt(mensagem.toCharArray());
    int[] ik = charArrayToInt(chave.toCharArray());
    int[] data = new int[mensagem.length()];

    for (int i=0;i<mensagem.length();i++) {
      data[i] = im[i] + ik[i];
    }

    return new String(intArrayToChar(data));
  }

  public String decrypt(String mensagem, String chave) {
    if (mensagem.length() != chave.length())
    error("O tamanho da mensagem e da chave devem ser iguais.");
    int[] im = charArrayToInt(mensagem.toCharArray());
    int[] ik = charArrayToInt(chave.toCharArray());
    int[] data = new int[mensagem.length()];

    for (int i=0;i<mensagem.length();i++) {
      data[i] = im[i] - ik[i];
    }

    return new String(intArrayToChar(data));
  }

  public String genKey(int tamanho) {
    char[] key = new char[tamanho];
    for (int i=0;i<tamanho;i++) {
      key[i] = 'c';
    }

    return new String(key);
  }


  private int chartoInt(char c) {
    return (int) c;
  }

  private char intToChar(int i) {
    return (char) i;
  }
    private int[] charArrayToInt(char[] cc) {
    int[] ii = new int[cc.length];
    for(int i=0;i<cc.length;i++){
      ii[i] = chartoInt(cc[i]);
    }
    return ii;
  }

  private char[] intArrayToChar(int[] ii) {
    char[] cc = new char[ii.length];
    for(int i=0;i<ii.length;i++){
      cc[i] = intToChar(ii[i]);
    }
    return cc;
  }

  private void error(String msg) {
    System.out.println(msg);
    System.exit(-1);
  }
    
}
