package pt.ulusofona.lp2.crazyChess;

import java.util.List;

public class Rainha extends CrazyPiece {

    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "Rainha" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "Rainha" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y + ")";
        }
    }

    public Rainha(int id, int tipo, int idEquipa, String alcunha, Simulador s) {
        super(id, tipo, idEquipa, alcunha, s);
    }

    @Override
    public String getImagePNG() {
        if (idEquipa == 10)
            return "rainha_black.png";

        return "rainha_white.png";
    }

    @Override
    public List<String> sugestoesMovimento(List<String> sugestoes) {
        //+6 porque queremos contar ainda com a posicao x-5
        //direita
        for(int i = x+1; i != x + 6 ; i++){
            //Chama função de verificar
            if(verificaPosicao(i,y)){
                sugestoes.add(i + ", " + y);
            }else{
                //Se o verificaPosicao econtrar uma peça no caminho da mesma equipa termina o ciclo
                break;
            }
        }

        //Verifica os caminhos para a esquerda (x-1 para evitar a posiçao origem)
        //esquerda
        for(int i = x-1; i != x - 6; i--){
            //Chama função de verificar
            if(verificaPosicao(i,y)){
                //adiciona String
                sugestoes.add(i + ", " + y);
            }else{
                //Se o verificaPosicao econtrar uma peça no caminho da mesma equipa termina o ciclo
                break;
            }
        }

        //Verifica os caminhos para a esquerda (x+1 para evitar a posiçao origem)
        //baixo
        for(int i = y+1; i != y + 6; i++){
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
        //cima
        for(int i = y-1; i != y - 6; i--){
            //Chama função de verificar
            if(verificaPosicao(x,i)){
                //adiciona String
                sugestoes.add(x + ", " + i);
            }else{
                //Se o verificaPosicao econtrar uma peça no caminho da mesma equipa termina o ciclo
                break;
            }
        }
        //baixo direita
        for(int i = x + 1, j = y + 1; i != x + 6 && j != y + 6; i++,j++){
            if(verificaPosicao(i,j)){
                sugestoes.add(i + ", " + j);
            }else{
                break;
            }
        }
        //baixo esquerda
        for(int i = x - 1, j = y + 1; i != x - 6 && j != y + 6; i--,j++){
            if(verificaPosicao(i,j)){
                sugestoes.add(i + ", " + j);
            }else{
                break;
            }
        }
        //cima esquerda
        for(int i = x - 1, j = y - 1; i != x - 6 && j != y - 6; i--,j--){
            if(verificaPosicao(i,j)){
                sugestoes.add(i + ", " + j);
            }else{
                break;
            }
        }
        //cima direita
        for(int i = x + 1, j = y - 1; i != x + 6 && j != y - 6; i++,j--){
            if(verificaPosicao(i,j)){
                sugestoes.add(i + ", " + j);
            }else{
                break;
            }
        }

        return sugestoes;
    }
}
