package pt.ulusofona.lp2.crazyChess;

public abstract class CrazyPiece {
   protected int id;
   protected int tipo;
   protected int idEquipa;
   protected String alcunha;
   protected int x;
   protected  int y;
   protected Simulador s;


    public abstract String toString();
    public CrazyPiece(int id, int tipo, int idEquipa, String alcunha, Simulador s){
        this.id = id;
        this.tipo = tipo;
        this.idEquipa = idEquipa;
        this.alcunha = alcunha;
        this.s = s;
    }
    public void setPieceCoord(int x, int y){
        this.x = x;
        this.y = y;
    }
    public  int getTipo(){
        return this.tipo;
    }
    int getX(){
        return x;
    }
    int getY(){
        return y;
    }
    public int getId(){
        return id;
    }
    public int getIdEquipa(){
        return idEquipa;
    }
    public abstract String getImagePNG();
}
