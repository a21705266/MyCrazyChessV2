package pt.ulusofona.lp2.crazyChess;

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
}
