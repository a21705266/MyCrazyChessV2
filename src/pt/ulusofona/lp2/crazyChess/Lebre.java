package pt.ulusofona.lp2.crazyChess;

import java.util.List;

public class Lebre extends CrazyPiece {
    public Lebre(int id, int tipo, int idEquipa, String alcunha, Simulador s) {
        super(id, tipo, idEquipa, alcunha, s);
    }

    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "Lebre" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "Lebre" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y + ")";
        }
    }

    @Override
    public String getImagePNG() {
        if (idEquipa == 10)
            return "lebre_black.png";

        return "lebre_white.png";
    }

    @Override
    public List<String> sugestoesMovimento(List<String> sugestoes) {
        int idJogador = s.calculaEquipa(idEquipa);
        //Buscar o jogador que tem a peça
        Jogador jogador = s.getJogadores(idJogador);
        if(jogador.getNrTurno()%2 != 0){
            sugestoes.add("Pedido Inválido");
            return sugestoes;
        }
        if(verificaPosicao(x+1,y+1)){
            sugestoes.add((x+1) + ", " + (y+1));
        }
        if(verificaPosicao(x-1,y-1)){
            sugestoes.add((x-1) + ", " + (y-1));
        }
        if(verificaPosicao(x+1,y-1)){
            sugestoes.add((x+1) + ", " + (y-1));
        }
        if(verificaPosicao(x-1,y+1)){
            sugestoes.add((x-1) + ", " + (y+1));
        }

        return sugestoes;
    }
}

