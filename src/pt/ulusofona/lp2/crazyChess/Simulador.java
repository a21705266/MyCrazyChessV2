
package pt.ulusofona.lp2.crazyChess;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Simulador {
    public static final int idBrancas = 20;
    public static final int idPretas = 10;
    public static String inputPath;

    int dimensao;
    public int nrPecas;
    boolean captura = false;
    HashMap<Integer, CrazyPiece> hm = new HashMap<Integer, CrazyPiece>();
    Jogador[] jogadores = new Jogador[]{new Jogador(idPretas),
            new Jogador(idBrancas)};
    ;
    int[][] tabuleiro;
    // false=pretas && true=brancas
    boolean turn = false;
    int nrTurn = 0;

    public Simulador(int dimensao, int nrPecas, HashMap<Integer, CrazyPiece> hm, int[][] tabuleiro) {
        this.dimensao = dimensao;
        this.nrPecas = nrPecas;
        this.hm = hm;
        this.tabuleiro = tabuleiro;
    }

    public Simulador() {

    }

    //Funçao de leitura de ficheiro
    public boolean iniciaJogo(File ficheiro) {
        inputPath = ficheiro.getPath();
        try {

            Scanner leitorFicheiro = new Scanner(ficheiro);
            int lineNumber = 0;
            while (leitorFicheiro.hasNextLine()) {
                lineNumber++;
                String linha = leitorFicheiro.nextLine();
                String dados[] = linha.split(":");

                //Linha = 1 => dimensao
                if (lineNumber == 1) {
                    int aux = Integer.parseInt(dados[0]);
                    //Verificar se a dimensao é valida
                    if ((aux >= 4 && aux <= 12)) {
                        dimensao = aux;
                        tabuleiro = new int[dimensao][dimensao];
                    } else {
                        return false;
                    }

                    //Linha = 2 => nrPeças
                } else if (lineNumber == 2) {
                    int aux = Integer.parseInt(dados[0]);
                    //Verificar se nr e Peças é valido
                    if (aux < (dimensao * dimensao)) {
                        this.nrPecas = aux;
                    } else {
                        return false;
                    }
                    // Criar peça e adicionar ao hashmap
                } else if (lineNumber > 2 && lineNumber <= nrPecas + 2) {//2<Linha<=nrPeça+1 => ler objectos e guardar num hashmap
                    int id = Integer.parseInt(dados[0]);
                    int tipo = Integer.parseInt(dados[1]);
                    int idEquipa = Integer.parseInt(dados[2]);
                    String alcunha = dados[3];


                    CrazyPiece c = criaPeça(id, tipo, idEquipa, alcunha, this);


                    //verificar se já existe, apenas adicionar se nao existir
                    if (!hm.containsKey(id)) {
                        hm.put(id, c);
                    }
                } else {

                    //(nrPeca+3) porque nao queremos as linhas de dimensao nem as das peças
                    //e o +3 são as duas primeiras linhas +1 para contabilizar o 0
                    int y = lineNumber - (nrPecas + 3);
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
        ;
        return this.dimensao;
    }

    public boolean processaJogada(int xO, int yO, int xD, int Yd) {

        if (xD >= dimensao || Yd >= dimensao || Yd < 0 || xD < 0) {
            return false;
        }
        if (xO >= dimensao || yO >= dimensao || yO < 0 || xO < 0) {
            return false;
        }

        int idPecaJogada = tabuleiro[xO][yO];

        //Não existe peça nas coords  de origem
        if (idPecaJogada == 0) {
            if (turn) {
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
        int idEquipaPecaAtual = turn ? 1 : 0; //Converte boolean em int

        //turn = true -> equipa branca a jogar
        //turn = false -> equipa preta a jogar
        if (equipa == idBrancas && turn == false){
            jogadores[idEquipaPecaAtual].incrementaTentativasInvalidas();
            return false;
        }else if (equipa == idPretas && turn == true){
            jogadores[idEquipaPecaAtual].incrementaTentativasInvalidas();
            return false;
        }

        //Verifica se avança uma unidade
        if (xD == xO + 1 || Yd == yO + 1 || xD == xO - 1 || Yd == yO - 1) {
            //se a posicao estiver livre
            if (tabuleiro[xD][Yd] == 0) {
                tabuleiro[xD][Yd] = idPecaJogada;
                pecaJogada.setPieceCoord(xD, Yd);
                //Apagar posicao anterior
                tabuleiro[xO][yO] = 0;
                //Troca de turno
                turn = !turn;

                jogadores[idEquipaPecaAtual].incrementaJogadasValidas();
                if (captura) {
                    nrTurn++;
                }

                return true;
            }// existe peca na posicao destino
            else if (hm.containsKey(tabuleiro[xD][Yd])) {
                CrazyPiece c = hm.get(tabuleiro[xD][Yd]);
                //Id Diferente captura o C
                if (c.getIdEquipa() != pecaJogada.getIdEquipa()) {
                    int idEquipaPecaCapturada = !turn ? 1 : 0; //Converte boolean em int da peça capturada
                    c.setPieceCoord(-1, -1);
                    jogadores[idEquipaPecaCapturada].removePeca(c);
                    tabuleiro[xD][Yd] = idPecaJogada;
                    pecaJogada.setPieceCoord(xD, Yd);
                    //Apagar posicao anterior
                    tabuleiro[xO][yO] = 0;
                    //Troca de turno
                    turn = !turn;

                    jogadores[idEquipaPecaAtual].incrementaJogadasValidas();
                    jogadores[idEquipaPecaAtual].incrementaCapuradas();
                    nrTurn = 0;
                    captura = true;
                    return true;
                    // Peças da mesma equipa
                } else {
                    jogadores[idEquipaPecaAtual].incrementaTentativasInvalidas();
                    return false;
                }
            }
        }
        jogadores[equipa].incrementaTentativasInvalidas();
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
        if (turn) {
            return idBrancas;
        } else {
            return idPretas;
        }
    }

    public List<String> getResultados() {
        List<String> resultados = new ArrayList<String>();
        resultados.add("JOGO DE CRAZY CHESS");
        if (nrTurn >= 10) {
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
        } else if (nrTurn >= 10 && captura == true) {
            return true;
        }
        return false;
    }

    public CrazyPiece criaPeça(int id, int tipo, int idEquipa, String alcunha, Simulador s) {
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
        return nrTurn;
    }

    public Simulador getSimulador() {
        return this;
    }

    public boolean gravarJogo(File ficheiroDestino) throws Exception {
        //faz copia das 3 primeiras secçoes do ficheiro de input
        try {
            FileOutputStream destino = new FileOutputStream(ficheiroDestino);//Criar outputStream para file de destino
            copiarInputToOutput(new File(inputPath), destino, nrPecas+2);//invocar função de cópia
                                                                                // nrPeças+2 -> As duas linhas do inicio
        } catch (Exception exception){
            String mensagem = "Excepção Lançada!";
            return false;
        }
        //Preenche as secçoes de posiçoes e de retoma de jogo
        try {
            int idEquipaAtual;
            String output;
            if (turn)
                idEquipaAtual = idBrancas;
            else
                idEquipaAtual = idPretas;

            //Obter Estatisticas
            List estatisticasPretas = jogadores[0].getEstatisticas();
            List estatisticasBrancas = jogadores[1].getEstatisticas();

            output = idEquipaAtual + ":" + estatisticasPretas.get(1) + ":" + estatisticasPretas.get(0) + ":" + estatisticasPretas.get(2)
                        + ":" + estatisticasPretas.get(1) + ":" + estatisticasPretas.get(0) + ":" + estatisticasPretas.get(2);
            return true;
        } catch (Exception exception) {
            String mensagem = "Excepção Lançada!";
            return false;
        }
    }

    public static void copiarInputToOutput(final File source, final FileOutputStream dest, int nLinhas) throws IOException {
        Scanner leitorFicheiro = new Scanner(source);
        try {

            //ler até não alcançar o nrLinhas pretendido
            for (int i = 0; i < nLinhas; i++) {
                String line = leitorFicheiro.nextLine() + "\n";
                dest.write(line.getBytes(), 0, line.length());
            }
        } finally {
            dest.close();
        }
    }
}