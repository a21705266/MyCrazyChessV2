package pt.ulusofona.lp2.crazyChess;

import java.util.ArrayList;
import java.util.List;

public class Jogador {
    int idEquipa;
    List<CrazyPiece> listaPecas;
    int capturadas = 0;
    int jogadasValidas = 0;
    int tentativasInvalidas = 0;
    int nrTurno = 0;

    Jogador(int idEquipa) {
        this.idEquipa = idEquipa;
        listaPecas = new ArrayList<>();
    }

    public int getNrTurno() {
        return nrTurno;
    }

    public void incrementaNrTurno() {
        nrTurno++;
    }

    public void decrementaNrTurno() {
        nrTurno--;
    }

    public void addPeca(CrazyPiece piece) {
        listaPecas.add(piece);
    }

    public void removePeca(CrazyPiece piece) {
        listaPecas.remove(piece);
    }

    public void incrementaCapuradas() {
        capturadas++;
    }

    public void incrementaJogadasValidas() {
        jogadasValidas++;
    }

    public void incrementaTentativasInvalidas() {
        tentativasInvalidas++;
    }

    public int listaComprimento() {
        return listaPecas.size();
    }

    //1.Peças Capturadas || 2.Jogadas Validas || 3.Tentativas Invalidas
    public List<Integer> getEstatisticas() {
        List<Integer> estatisticas = new ArrayList<Integer>();
        estatisticas.add(capturadas);
        estatisticas.add(jogadasValidas);
        estatisticas.add(tentativasInvalidas);

        return estatisticas;
    }

    public void setCapturadas(int capturadas) {
        this.capturadas = capturadas;
    }

    public void setJogadasInvalidas(int jogadasInvalidas) {
        this.tentativasInvalidas = jogadasInvalidas;
    }

    public void setJogadasValidas(int jogadasValidas) {
        this.jogadasValidas = jogadasValidas;
    }

    public int getTentativasInvalidas() {
        return tentativasInvalidas;
    }
}
