package pt.ulusofona.lp2.crazyChess;

public class Lebre extends CrazyPiece {
    public Lebre(int id, int tipo, int idEquipa, String alcunha, Simulador s){
        super(id,tipo, idEquipa, alcunha, s);
    }

    @Override
    public String toString() {
        if (x == -1 && y == -1) {
            return id + " | " + "Lebre" + " | " + idEquipa + " | " + alcunha + " @ (n/a)";
            //devolve se a peça estiver capturada
        } else {
            return id + " | " + "Lebre" + " | " + idEquipa + " | " + alcunha + " @ (" + x + ", " + y+")";
        }
    }

    @Override
    public String getImagePNG() {
        if(idEquipa == 10)
            return "lebre_black.png";

        return "lebre_white.png";
    }
}
