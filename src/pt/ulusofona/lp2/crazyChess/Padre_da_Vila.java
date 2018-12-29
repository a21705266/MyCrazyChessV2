package pt.ulusofona.lp2.crazyChess;

import java.util.ArrayList;
import java.util.List;

public class Padre_da_Vila extends CrazyPiece{

    public Padre_da_Vila(int id, int tipo, int idEquipa, String alcunha, Simulador s){
        super(id,tipo, idEquipa, alcunha, s);
    }

    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "Padre da Vila" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "Padre da Vila" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y+")";
        }
    }

    @Override
    public String getImagePNG() {
        if(idEquipa == 10)
            return "padre_vila_black.png";

        return "padre_vila_white.png";
    }

    @Override
    public List<String> sugestoesMovimento(List<String> sugestoes) {

        //baixo direita
        for(int i=1; i<=3; i++) {
                if (verificaPosicao((x + i), y + i)) {
                    sugestoes.add((x + i) + ", " + (y + i));
                }else{
                    break;
                }
        }
        for(int i=1; i<=3; i++) {
            if (verificaPosicao((x - i), y - i)) {
                sugestoes.add((x - i) + ", " + (y - i));
            }else{
                break;
            }
        }
        for(int i=1; i<=3; i++) {
            if (verificaPosicao((x - i), y + i)) {
                sugestoes.add((x - i) + ", " + (y + i));
            }else{
                break;
            }
        }
        for(int i=1; i<=3; i++) {
            if (verificaPosicao((x + i), y - i)) {
                sugestoes.add((x + i) + ", " + (y - i));
            }else{
                break;
            }
        }
        return sugestoes;
    }
    public List<String> encontraRainha(){
        List<String> rainha = new ArrayList<>();
        Integer[] chaves = (Integer[]) s.getHm().keySet().toArray(); // returns an array of keys
        for(int i = 1 ; i < chaves.length;i++){
            CrazyPiece c = s.getHm().get(i);
            if(c.getTipo() == 1){
                for(int j = x-2; j <= x+2;j++){
                    if(distanciaRainha(c.getX(), c.getY())){
                        rainha.add(j + ", " + y);
                    }
                }
            }
        }
        return rainha;
    }
    public boolean distanciaRainha(int x, int y){
        if (x >= s.getDimensao() || y >= s.getDimensao() || y < 0 || x < 0) {
            return false;
        }
        return true;
    }
}
