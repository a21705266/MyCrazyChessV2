package pt.ulusofona.lp2.crazyChess;

import java.util.ArrayList;
import java.util.List;

public abstract class CrazyPiece {
   protected int id;
   protected int tipo;
   protected int idEquipa;
   protected String alcunha;
   protected int x;
   protected  int y;
   protected Simulador s;


    public abstract String toString();
    public CrazyPiece(int id, int tipo, int idEquipa, String alcunha, Simulador s){
        this.id = id;
        this.tipo = tipo;
        this.idEquipa = idEquipa;
        this.alcunha = alcunha;
        this.s = s;
    }
    public void setPieceCoord(int x, int y){
        this.x = x;
        this.y = y;
    }
    public  int getTipo(){
        return this.tipo;
    }
    int getX(){
        return x;
    }
    int getY(){
        return y;
    }
    public int getId(){
        return id;
    }
    public int getIdEquipa(){
        return idEquipa;
    }
    public abstract String getImagePNG();
    public boolean verificaPosicao(int xD, int yD) {
        if (xD >= s.getDimensao() || yD >= s.getDimensao() || yD < 0 || xD < 0) {
            return false;
        }
        // se for 0 nunca contem
        int [][] tabuleiro = s.getTabuleiro();

        if (s.getHm().containsKey(tabuleiro[xD][yD])) {
            CrazyPiece c = s.hm.get(tabuleiro[xD][yD]);
            //Id Iguais retorna false
            if (c.getIdEquipa() == idEquipa) {
                return false;
            }
            if (tipo == 1 && c.getTipo() == 1){
                return false;
            }
        }
        return true;
    }
    public abstract List<String> sugestoesMovimento(List<String> sugestoes);
    public boolean movePeca(int xD, int yD) {

        //retornar falso caso coords de destino estejam fora do tabuleiro
        if (x >= s.getDimensao() || y >= s.getDimensao() || y < 0 || x < 0) {
            return false;
        }

        //Buscar lista de sugestoes -> Se as coords de destino forem um sugestao temos uma jogada valida
        List<String> sugestoes = new ArrayList<String>();
        sugestoes = sugestoesMovimento(sugestoes);

        //se for invlido retorna falso
        if(sugestoes.get(0).equals("Pedido Inválido")){
            return false;
        }
        //Percorrer jogadas sugeridas e verificar se as coords destino constam nas sugestoes
        for(String str : sugestoes){
            String sugestoesCoords[] = str.split(", ");

            //coords sugeridas
            int xSugerido = Integer.parseInt(sugestoesCoords[0]);
            int ySugerido = Integer.parseInt(sugestoesCoords[1]);

            //verificar se coords sugeridas == a coords destino
            if(xSugerido == xD && ySugerido == yD){
                return true;
            }
        }
        //Nao encontrou compatibilidade
        return false;

    }
}
