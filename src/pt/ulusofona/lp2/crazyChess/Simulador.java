
package pt.ulusofona.lp2.crazyChess;

import javax.print.DocFlavor;
import java.io.*;
import java.util.*;

public class Simulador {
    public static final int idBrancas = 20;
    public static final int idPretas = 10;
    public static String inputPath;

    //0.idEquipa | 1.capturadas | 2.jogadasVal |3.jogadasInval
    //4.Peça que moveu | 5.x | 6.y | 7.Peça Capturada
    //Se algum destes campos não for alterado deve escrever 0;
    public int[] jogadaAnterior = new int[8];
    boolean undo = false;

    int dimensao;
    public int nrPecas;
    boolean captura = false;

    HashMap<Integer, CrazyPiece> hm = new HashMap<Integer, CrazyPiece>();
    Jogador[] jogadores = new Jogador[]{new Jogador(idPretas),
                          new Jogador(idBrancas)};
    int[][] tabuleiro;
    // false=pretas && true=brancas
    boolean turno = false;
    //Verifica se houve 10 jogadas sem captura
    int countTerminaJogo = 0;

    public Simulador(int dimensao, int nrPecas, HashMap<Integer, CrazyPiece> hm, int[][] tabuleiro) {
        this.dimensao = dimensao;
        this.nrPecas = nrPecas;
        this.hm = hm;
        this.tabuleiro = tabuleiro;
    }

    public Simulador() {

    }

    public boolean seccao1(String[] dados){
        int aux = Integer.parseInt(dados[0]);
        //Verificar se a dimensao é valida
        if ((aux >= 4 && aux <= 12)) {
            dimensao = aux;
            tabuleiro = new int[dimensao][dimensao];
            return true;
        }else{
            return false;
        }
    }
    public boolean seccao2(String[] dados){
        int aux = Integer.parseInt(dados[0]);
        //Verificar se nr e Peças é valido
        if (aux < (dimensao * dimensao)) {
            this.nrPecas = aux;
            return true;
        } else {
            return false;
        }
    }
    public boolean seccao3(String dados[]){
        int id = Integer.parseInt(dados[0]);
        int tipo = Integer.parseInt(dados[1]);
        int idEquipa = Integer.parseInt(dados[2]);
        String alcunha = dados[3];

        if(tipo > 8 || tipo < 0)
            return false;

        CrazyPiece c = criaPeca(id, tipo, idEquipa, alcunha, this);


        //verificar se já existe, apenas adicionar se nao existir
        if (!hm.containsKey(id)) {
            hm.put(id, c);
        }
        return true;
    }
    public boolean seccao4(String dados[], int lineNumber){
        //(nrPeca+3) porque nao queremos as linhas de dimensao nem as das peças
        //e o +3 são as duas primeiras linhas +1 para contabilizar o 0
        int y = lineNumber - (nrPecas + 3);

        //verificar tamanho do tabuleiro
        if(y > dimensao || dados.length > dimensao){
            return false;
        }

        //vamos percorrer a linha lida e verificar se temos uma posição a 0 ou nao
        for (int i = 0; i < dimensao; i++) {
            int aux = Integer.parseInt(dados[i]);
            //se nao
            if (aux != 0) {
                boolean verificaPeçaExiste = hm.containsKey(aux);
                //verificar se o id da peça encontrada existe no hashmap
                if (verificaPeçaExiste == true) {
                    //ir buscar a peça com o id que encontramos e a sua equipa
                    CrazyPiece piece = hm.get(aux);
                    int idEquipa = piece.getIdEquipa();

                    //Verifica a que jogador deve atribuir a peça
                    if (idEquipa == idPretas) {
                        jogadores[0].addPeca(piece);
                    } else {
                        jogadores[1].addPeca(piece);
                    }

                    //atualizar as coordenadas
                    piece.setPieceCoord(i, y);
                    tabuleiro[i][y] = aux;

                }
            } else {
                tabuleiro[i][y] = 0;
            }
        }
        return true;
    }
    public void seccao5(String dados[]){
        if (Integer.parseInt(dados[0]) == 10)
            turno = false;
        else
            turno = true;
        jogadores[0].setJogadasValidas(Integer.parseInt(dados[1]));
        jogadores[0].setCapturadas(Integer.parseInt(dados[2]));
        jogadores[0].setJogadasInvalidas(Integer.parseInt(dados[3]));
        jogadores[1].setJogadasValidas(Integer.parseInt(dados[4]));
        jogadores[1].setCapturadas(Integer.parseInt(dados[5]));
        jogadores[1].setJogadasInvalidas(Integer.parseInt(dados[6]));
    }

    //Funçao de leitura de ficheiro
    public boolean iniciaJogo(File ficheiro) {
        try {
            inputPath = ficheiro.getPath();
            Scanner leitorFicheiro = new Scanner(ficheiro);
            int lineNumber = 0;
            boolean valida;
            while (leitorFicheiro.hasNextLine()) {
                lineNumber++;
                String linha = leitorFicheiro.nextLine();
                String dados[] = linha.split(":");

                //Linha = 1 => dimensao
                if (lineNumber == 1) {
                    if(!seccao1(dados))
                        return false;

                    //Linha = 2 => nrPeças
                } else if (lineNumber == 2) {
                    if(!seccao2(dados))
                        return false;

                    // Criar peça e adicionar ao hashmap
                } else if (lineNumber > 2 && lineNumber <= nrPecas + 2) {//2<Linha<=nrPeça+1 => ler objectos e guardar num hashmap
                    if(!seccao3(dados))
                        return false;

                } else if (lineNumber > nrPecas + 2 && lineNumber <= nrPecas + 2 + dimensao) {
                    if(!seccao4(dados,lineNumber))
                        return false;

                    //secção 5
                } else if(lineNumber > nrPecas + dimensao + 2){
                    seccao5(dados);
                }
            }
            leitorFicheiro.close();
        } catch (FileNotFoundException exception) {
            String nomeFicheiro = ficheiro.getName(); // Vou buscar o nome do ficheiro, pois a definição da função apenas nos deixa passar um FILE
            String mensagem = "Erro: o ficheiro " + nomeFicheiro + " nao foi encontrado.";
            System.out.println(mensagem);
            return false;
        }
        return true;
    }

    public int getTamanhoTabuleiro() {
        return this.dimensao;
    }

    public boolean validaJogada(int xO, int yO){
        int idPecaJogada = tabuleiro[xO][yO];

        //Não existe peça nas coords  de origem
        if (idPecaJogada == 0) {
            if (turno) {
                jogadores[1].incrementaTentativasInvalidas();
            } else {
                jogadores[0].incrementaTentativasInvalidas();
            }
            return false;
        }

        //Vou buscar a peca a ser jogada
        CrazyPiece pecaJogada = hm.get(idPecaJogada);
        int equipa = pecaJogada.getIdEquipa();

        //Posiçoes do array de jogadores
        int idEquipaPecaAtual = turno ? 1 : 0; //Converte boolean em int

        //turn = true -> equipa branca a jogar
        //turn = false -> equipa preta a jogar
        if (equipa == idBrancas && turno == false){
            jogadores[idEquipaPecaAtual].incrementaTentativasInvalidas();
            return false;
        } else if (equipa == idPretas && turno == true) {
            jogadores[idEquipaPecaAtual].incrementaTentativasInvalidas();
            return false;
        }
        return true;
    }

    public int[][] getTabuleiro() {
        return tabuleiro;
    }

    public int getDimensao() {
        return dimensao;
    }

    public HashMap<Integer, CrazyPiece> getHm() {
        return hm;
    }

    public boolean processaJogada(int xO, int yO, int xD, int Yd) {

        int idPecaJogada = tabuleiro[xO][yO];
        //Vou buscar a peca a ser jogada
        CrazyPiece pecaJogada = hm.get(idPecaJogada);


        //Posiçoes do array de jogadores
        int idEquipaPecaAtual = turno ? 1 : 0; //Converte boolean em int

        if(!validaJogada(xO,yO)){
            return false;
        }
        setJogadaAnterior(jogadores[idEquipaPecaAtual],pecaJogada);

        //Verifica se avança
        if (pecaJogada.movePeca(xD,Yd)) {
            //se a posicao estiver livre
            if (tabuleiro[xD][Yd] == 0) {
                tabuleiro[xD][Yd] = idPecaJogada;
                pecaJogada.setPieceCoord(xD, Yd);
                //Apagar posicao anterior
                tabuleiro[xO][yO] = 0;
                //Troca de turno
                turno = !turno;

                jogadores[idEquipaPecaAtual].incrementaJogadasValidas();

                jogadaAnterior[1] = 1;

                if (captura) {
                    countTerminaJogo++;
                }
                undo = false;
                jogadores[idEquipaPecaAtual].incrementaNrTurno();
                return true;
            }// existe peca na posicao destino
            else if (hm.containsKey(tabuleiro[xD][Yd])) {
                CrazyPiece c = hm.get(tabuleiro[xD][Yd]);
                //Id Diferente captura o C
                if (c.getIdEquipa() != pecaJogada.getIdEquipa()) {
                    int idEquipaPecaCapturada = !turno ? 1 : 0; //Converte boolean em int da peça capturada
                    c.setPieceCoord(-1, -1);
                    jogadores[idEquipaPecaCapturada].removePeca(c);
                    tabuleiro[xD][Yd] = idPecaJogada;
                    pecaJogada.setPieceCoord(xD, Yd);
                    //Apagar posicao anterior
                    tabuleiro[xO][yO] = 0;
                    //Troca de turno
                    turno = !turno;

                    jogadores[idEquipaPecaAtual].incrementaJogadasValidas();
                    jogadores[idEquipaPecaAtual].incrementaCapuradas();
                    jogadores[idEquipaPecaAtual].incrementaNrTurno();

                    //preenche array de jogadaAnterior
                    jogadaAnterior[1] = 1;
                    jogadaAnterior[2] = 1;

                    //count que valida fim de jogo caso nao haja capturas durante 10 turnos
                    countTerminaJogo = 0;
                    //houve captura - regra dos 10 turnos empate ativa
                    captura = true;
                    //podemos fazer undo novamente
                    undo = false;
                    return true;
                    // Peças da mesma equipa
                } else {
                    jogadores[idEquipaPecaAtual].incrementaTentativasInvalidas();

                    jogadaAnterior[3] = 1;
                    undo = false;
                    return false;
                }
            }
        }
        jogadores[idEquipaPecaAtual].incrementaTentativasInvalidas();
        jogadaAnterior[3] = 1;
        undo = false;
        return false;
    }

    public int getIDPeca(int x, int y) {
        return tabuleiro[x][y];
    }

    public List<CrazyPiece> getPecasMalucas() {
        List<CrazyPiece> list = new ArrayList<CrazyPiece>(hm.values());
        return list;
    }

    public List<String> getAutores() {
        List<String> autores = new ArrayList<String>();
        autores.add("Marcelo Costa || 21705266 || LEIRT");
        autores.add("Bijel Premgi || 21703957 || LEI");
        return autores;
    }

    public int getIDEquipaAJogar() {
        if (turno) {
            return idBrancas;
        } else {
            return idPretas;
        }
    }

    public List<String> getResultados() {
        List<String> resultados = new ArrayList<String>();
        resultados.add("JOGO DE CRAZY CHESS");
        if (countTerminaJogo >= 10) {
            resultados.add("Resultado: EMPATE");
        } else {
            if (jogadores[1].listaComprimento() == 0 && jogadores[0].listaComprimento() > 0) {
                resultados.add("Resultado: VENCERAM AS PRETAS");
            } else if (jogadores[0].listaComprimento() == 0 && jogadores[1].listaComprimento() > 0) {
                resultados.add("Resultado: VENCERAM AS BRANCAS");
            } else {
                resultados.add("Resultado: EMPATE");
            }
        }
        resultados.add("---");
        resultados.add("Equipa das Pretas");
        //vamos buscar um array com as estatisticas da equipa das pretas
        //Posicoes : 0->Capturadas || 1->jogadas Validas || 2->jogadas Invalidas
        List<Integer> estatisticasPretas = jogadores[0].getEstatisticas();
        resultados.add(String.valueOf(estatisticasPretas.get(0)));
        resultados.add(String.valueOf(estatisticasPretas.get(1)));
        resultados.add(String.valueOf(estatisticasPretas.get(2)));

        resultados.add("Equipa das Brancas");
        List<Integer> estatisticas = jogadores[1].getEstatisticas();
        resultados.add(String.valueOf(estatisticas.get(0)));
        resultados.add(String.valueOf(estatisticas.get(1)));
        resultados.add(String.valueOf(estatisticas.get(2)));

        return resultados;
    }

    public boolean jogoTerminado() {
        if (jogadores[0].listaComprimento() == 0 || jogadores[1].listaComprimento() == 0) {
            return true;
        } else if (jogadores[0].listaComprimento() == 1 && jogadores[1].listaComprimento() == 1) {
            return true;
        } else if (countTerminaJogo >= 10 && captura == true) {
            return true;
        }
        return false;
    }

    public CrazyPiece criaPeca(int id, int tipo, int idEquipa, String alcunha, Simulador s) {
        CrazyPiece e;
        if (tipo == 0) {
            e = new Rei(id, tipo, idEquipa, alcunha, s);
        } else if (tipo == 1) {
            e = new Rainha(id, tipo, idEquipa, alcunha, s);
        } else if (tipo == 2) {
            e = new PoneiMagico(id, tipo, idEquipa, alcunha, s);
        } else if (tipo == 3) {
            e = new Padre_da_Vila(id, tipo, idEquipa, alcunha, s);
        } else if (tipo == 4) {
            e = new TorreH(id, tipo, idEquipa, alcunha, s);
        } else if (tipo == 5) {
            e = new TorreV(id, tipo, idEquipa, alcunha, s);
        } else if (tipo == 6) {
            e = new Lebre(id, tipo, idEquipa, alcunha, s);
        } else {
            e = new Joker(id, tipo, idEquipa, alcunha, s);
        }
        return e;
    }

    public int getTurno() {
        return countTerminaJogo;
    }

    public Simulador getSimulador() {
        return this;
    }

    public boolean gravarJogo(File ficheiroDestino) throws Exception {
        //faz copia das 3 primeiras secçoes do ficheiro de input
        //secçoes 1,2,3
        try {
            copiarInputToOutput(new File(inputPath), ficheiroDestino, nrPecas + 2);//invocar função de cópia
            // nrPeças+2 -> As duas linhas do inicio
            // obter secçao 4
            escreverTabuleiroNoFicheiro(ficheiroDestino, tabuleiro, dimensao);

            //Obter 5 secção
            int idEquipaAtual;
            String output;
            if (turno)
                idEquipaAtual = idBrancas;
            else
                idEquipaAtual = idPretas;

            //Obter Estatisticas
            List estatisticasPretas = jogadores[0].getEstatisticas();
            List estatisticasBrancas = jogadores[1].getEstatisticas();

            output = idEquipaAtual + ":" + estatisticasPretas.get(1) + ":" + estatisticasPretas.get(0) + ":" + estatisticasPretas.get(2)
                    + ":" + estatisticasBrancas.get(1) + ":" + estatisticasBrancas.get(0) + ":" + estatisticasBrancas.get(2);

            escreveFicheiro(output, ficheiroDestino);
            return true;
        } catch (Exception e) {
            String mensagem = "Excepção Lançada!";
            return false;
        }
    }

    public static void copiarInputToOutput(final File source, final File dest, int nLinhas) throws IOException {
        FileOutputStream destino = new FileOutputStream(dest, false);
        try {
            Scanner leitorFicheiro = new Scanner(source);
            //ler até não alcançar o nrLinhas pretendido
            for (int i = 0; i < nLinhas; i++) {
                String line = leitorFicheiro.nextLine() + "\n";
                destino.write(line.getBytes());
            }
            leitorFicheiro.close();
        } finally {
            destino.close();
        }
    }

    public static void escreverTabuleiroNoFicheiro(File ficheiro, int[][] tabuleiro, int dimTabuleiro) throws IOException {
        FileOutputStream destino = new FileOutputStream(ficheiro, true);

        //percorrer matriz e escrever
        for (int i = 0; i < dimTabuleiro; i++) {
            for (int j = 0; j < dimTabuleiro; j++) {
                String s;
                //ultima parcela
                if (j == dimTabuleiro - 1) {
                    s = Integer.toString(tabuleiro[j][i]);
                    destino.write(s.getBytes());
                } else {
                    s = tabuleiro[j][i] + ":";
                    destino.write(s.getBytes());
                }
            }
            destino.write("\n".getBytes());
        }
        destino.close();

    }

    public static void escreveFicheiro(String str, File destino) throws IOException {
        FileOutputStream dest = new FileOutputStream(destino, true);

        dest.write(str.getBytes());

        dest.close();
    }

    //0-Pretas || 1-Brancas
    public int calculaEquipa(int idEquipa){
        if(idEquipa == 10)
            return 0;
        else
            return 1;
    }

    public void anularJogadaAnterior(){
        //0.idEquipa | 1.capturadas | 2.jogadasVal |3.jogadasInval
        //4.Peça que moveu | 5.x | 6.y | 7.Peça Capturada

        if(undo){
            return;
        }

        int idEquipa = jogadaAnterior[0];

        //Vai buscar 0-Pretas | 1-Brancas Para selecionar o jogador do array de jogadores
        int equipaPecaAtual = calculaEquipa(idEquipa);

        //Estatitsticas Atuais
        List estatisticas = jogadores[equipaPecaAtual].getEstatisticas();

        //Atualizar estatisticas
        jogadores[equipaPecaAtual].setCapturadas((int)estatisticas.get(0) - jogadaAnterior[2]);
        jogadores[equipaPecaAtual].setJogadasValidas((int)estatisticas.get(1) - jogadaAnterior[1]);
        jogadores[equipaPecaAtual].setJogadasInvalidas((int)estatisticas.get(2) - jogadaAnterior[3]);


        CrazyPiece pecaMovida = hm.get(jogadaAnterior[4]);
        //Primeiro peça comida porque temos as suas coordenadas na peca atual
        //Se existir peça comida voltar a adiciona-la
        if(jogadaAnterior[7] > 0){
            CrazyPiece capturada = hm.get(jogadaAnterior[7]);
            //Alterar Coords
            int x = pecaMovida.getX();
            int y = pecaMovida.getY();

            //obter idEquipa
            int idEquipaCapturada = calculaEquipa(capturada.idEquipa);

            //set coords e adicionar peca de novo
            capturada.setPieceCoord(x,y);
            jogadores[idEquipaCapturada].addPeca(capturada);
            tabuleiro[x][y] = capturada.getId();
        }else {
            tabuleiro[pecaMovida.getX()][pecaMovida.getY()] = 0;
        }
        //Realocar peça movida
        pecaMovida.setPieceCoord(jogadaAnterior[5],jogadaAnterior[6]);
        tabuleiro[pecaMovida.getX()][pecaMovida.getY()] = pecaMovida.getId();

        jogadores[equipaPecaAtual].decrementaNrTurno();

        //mudar turno
        if (captura) {
            countTerminaJogo--;
        }
        turno = !turno;
        undo = true;
    }

    public List<String> obterSugestoesJogada(int xO, int yO){
        int idPeca = tabuleiro[xO][yO];
        CrazyPiece peca = hm.get(idPeca);
        List<String> sugestoes = new ArrayList<>();

        if(tabuleiro[xO][yO]==0){
            sugestoes.add("Pedido Inválido");
            return sugestoes;
        }

        if (peca.getIdEquipa() != getIDEquipaAJogar()){
            sugestoes.add("Pedido Inválido");
            return sugestoes;
        }

        return peca.sugestoesMovimento(sugestoes);
    }

    public void setJogadaAnterior(Jogador j , CrazyPiece c){

        //0.idEquipa | 1.capturadas | 2.jogadasInval
        //3.Peça que moveu | 4.x | 5.y | 6.Peça Capturada

        jogadaAnterior[0] = c.getIdEquipa();
        jogadaAnterior[4] = c.getId();
        jogadaAnterior[5] = c.getX();
        jogadaAnterior[6] = c.getY();

    }

    public Jogador getJogadores(int jogador) {
            return jogadores[jogador];

    }

}