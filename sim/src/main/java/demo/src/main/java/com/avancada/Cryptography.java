package demo.src.main.java.com.avancada;

public class Cryptography {

    public Cryptography(){
    }

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
    // System.out.println(msg);
    System.exit(-1);
  }
    
}
