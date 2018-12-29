package pt.ulusofona.lp2.crazyChess;

import java.util.ArrayList;
import java.util.List;

public class Rei extends CrazyPiece {

    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "Rei" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "Rei" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y + ")";
        }
    }

    public Rei(int id, int tipo, int idEquipa, String alcunha, Simulador s) {
        super(id, tipo, idEquipa, alcunha, s);
    }

    @Override
    public String getImagePNG() {
        if (idEquipa == 10)
            return "bird.png";

        return "bird.png";
    }

    public List<String> sugestoesMovimento(List<String> sugestoes) {

        if (verificaPosicao(x + 1, y)) {
            sugestoes.add((x + 1) + ", " + y);
        }

        if (verificaPosicao(x + 1, y + 1)) {
            sugestoes.add((x + 1) + ", " + (y + 1));//(3,2)
        }

        if (verificaPosicao(x, y + 1)) {
            sugestoes.add(x + ", " + (y + 1));//(2,2)
        }

        // x+1 y-1
        if (verificaPosicao(x + 1, y - 1)) {
            sugestoes.add((x + 1) + ", " + (y - 1)); //(3,0)
        }
        if (verificaPosicao(x, y - 1)) {
            sugestoes.add(x + ", " + (y - 1)); //(2,0)
        }

        if (verificaPosicao(x - 1, y)) { // x=2 y=1 dim=4
            sugestoes.add((x - 1) + ", " + y); //(1,1)
        }

        if (y + 1 < s.dimensao && verificaPosicao(x - 1, y + 1)) {
            sugestoes.add((x - 1) + ", " + (y + 1));//(1,2)
        }

        // x-1 y-1
        if (verificaPosicao(x - 1, y - 1)) {
            sugestoes.add((x - 1) + ", " + (y - 1)); //(1,0)
        }

        return sugestoes;
    }
}
