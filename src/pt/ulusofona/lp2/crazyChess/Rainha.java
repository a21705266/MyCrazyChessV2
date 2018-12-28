package pt.ulusofona.lp2.crazyChess;

import java.util.List;

public class Rainha extends CrazyPiece {

    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "Rainha" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "Rainha" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y+")";
        }
    }

    public Rainha(int id, int tipo, int idEquipa, String alcunha, Simulador s){
        super(id,tipo, idEquipa, alcunha, s);
    }

    @Override
    public String getImagePNG() {
        if(idEquipa == 10)
            return "rainha_black.png";

        return "rainha_white.png";
    }

    @Override
    public List<String> sugestoesMovimento(List<String> sugestoes) {
        int xO = x;
        int yO = y;

        //x=2, y=1, dimensao = 9
        if(verificaPosicao((xO+5), yO)){
            for(int i=1; i<=5;i++){
                if(verificaPosicao((xO+i), yO)){
                    sugestoes.add((xO + i) + ", " + yO); //3,1 a 7,1
                }else{
                    break;
                }

            }
            if(verificaPosicao((xO+5), (yO+5))){
                for(int i=1; i<=5; i++){
                    if(verificaPosicao((xO+i), (yO+i))) {
                        sugestoes.add((xO + i) + ", " + (yO + i)); //3,2 a 7,6
                    }else{
                        break;
                    }
                }
            }

            if(verificaPosicao((xO+5), (yO-5))){
                for(int i=1; i<=5; i++){
                    if(verificaPosicao((xO+i), (yO-i))) {
                        sugestoes.add((xO + i) + ", " + (yO - i)); //3,0
                    }else{
                        break;
                    }
                }
            }
        }

        if(verificaPosicao((xO-5), yO)){
            for(int i =1; i<=5; i++){
                if(verificaPosicao((xO-i), (yO))) {
                    sugestoes.add((xO - i) + ", " + yO);
                }else{
                    break;
                }
            }

            if(verificaPosicao((xO-5), (yO+5))){
                for(int i=1; i<=5; i++) {
                    if(verificaPosicao((xO-i), (yO+i))) {
                        sugestoes.add((xO - i) + ", " + (yO + i));
                    }else{
                        break;
                    }
                }
            }

            if(verificaPosicao((xO-5), (yO-5))){
                for(int i=1; i<=5; i++){
                    if(verificaPosicao((xO),(yO-i)))
                        sugestoes.add(xO + ", " + (yO-i));
                }
            }
        }

        return sugestoes;
    }
}
