package pt.ulusofona.lp2.crazyChess;

import java.util.ArrayList;
import java.util.List;

public class TorreV extends CrazyPiece {
    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "TorreV" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "TorreV" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y+")";
        }
    }

    public TorreV(int id, int tipo, int idEquipa, String alcunha, Simulador s){
        super(id,tipo, idEquipa, alcunha, s);
    }

    @Override
    public String getImagePNG() {
        if(idEquipa == 10)
            return "torre_v_black.png";

        return "torre_v_white.png";
    }

    public List<String> sugestoesMovimento(List<String> sugestoes){
        //Verifica os caminhos para a esquerda (x+1 para evitar a posiçao origem)
        for(int i = y+1; i<=s.getDimensao(); i++){
            //Chama função de verificar
            if(verificaPosicao(x,i)){
                //adiciona String
                sugestoes.add(x + ", " + i);
            }else{
                //Se o verificaPosicao econtrar uma peça no caminho da mesma equipa termina o ciclo
                break;
            }
        }
        //Verifica os caminhos para a esquerda (x-1 para evitar a posiçao origem)
        for(int i = y-1; i>0; i--){
            //Chama função de verificar
            if(verificaPosicao(x,i)){
                //adiciona String
                sugestoes.add(x + ", " + i);
            }else{
                //Se o verificaPosicao econtrar uma peça no caminho da mesma equipa termina o ciclo
                break;
            }
        }

        return sugestoes;
    }
}
