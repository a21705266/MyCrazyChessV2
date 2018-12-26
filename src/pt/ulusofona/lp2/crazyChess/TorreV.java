package pt.ulusofona.lp2.crazyChess;

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
}
