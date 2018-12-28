package pt.ulusofona.lp2.crazyChess;

import java.util.ArrayList;
import java.util.List;

public class TorreH extends CrazyPiece {

    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "TorreH" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "TorreH" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y+")";
        }
    }

    public TorreH(int id, int tipo, int idEquipa, String alcunha, Simulador s){
        super(id,tipo, idEquipa, alcunha, s);
    }

    @Override
    public String getImagePNG() {
        if(idEquipa == 10)
            return "torre_h_black.png";

        return "torre_h_white.png";
    }

    @Override
    public boolean movePeca(int xD, int yD) {
        return false;
    }

    public List<String> sugestoesMovimento(List<String> sugestoes){

        //Não existe = Pediod Invalido
        if(s.tabuleiro[x][y]==0){
            sugestoes.add("Pedido Inválido");
            return sugestoes;
        }

        //Verifica os caminhos para a esquerda (x+1 para evitar a posiçao origem)
        for(int i = x+1; i<=s.dimensao; i++){
            //Chama função de verificar
            if(verificaPosicao(i,y)){
                sugestoes.add(i + ", " + y);
            }else{
                //Se o verificaPosicao econtrar uma peça no caminho da mesma equipa termina o ciclo
                break;
            }
        }
        //Verifica os caminhos para a esquerda (x-1 para evitar a posiçao origem)
        for(int i = x-1; i>0; i--){
            //Chama função de verificar
            if(verificaPosicao(i,y)){
                //adiciona String
                sugestoes.add(i + ", " + y);
            }else{
                //Se o verificaPosicao econtrar uma peça no caminho da mesma equipa termina o ciclo
                break;
            }
        }

        return sugestoes;
    }
}

