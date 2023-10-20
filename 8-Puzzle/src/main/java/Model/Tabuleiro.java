package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Igor Rodrigues e Lucas Ikeda
 */
public class Tabuleiro {
    private int[][] casas;
    private int linhaLivre;
    private int colunaLivre;

    public Tabuleiro() {
        this.casas = new int[3][3];

        int val = 1;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                casas[i - 1][j - 1] = val;
                val++;
            }
            casas[2][2] = 0;
            linhaLivre = 2;
            colunaLivre = 2;
        }
    }

    public int[][] getCasas() {
        return casas;
    }

    public int getLinhaLivre() {
        return linhaLivre;
    }

    public int getColunaLivre() {
        return colunaLivre;
    }

    /**
     * Verifica se o tabuleiro está posicionado de acordo com a condição de
     * vitória do jogo
     *
     * @return {@code true} se o jogo foi vencido e {@code false} caso contrário
     */
    public boolean verificarVitoria() {
        int cout = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 2 && j == 2) {
                    break;
                }
                if (casas[i][j] != cout) {
                    return false;
                }
                cout++;
            }
        }
        return true;
    }

    public ArrayList<Movimento> movimentosDisponiveis() {
        int pos[][] = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
        ArrayList<Movimento> movimentos = new ArrayList();
        for (int i = 0; i < 4; i++) {
            int movLinha = linhaLivre + pos[i][0];
            int movColuna = colunaLivre + pos[i][1];
            if (movLinha >= 0 && movLinha <= 2  && movColuna >= 0 && movColuna <= 2) {
                movimentos.add(new Movimento(movLinha, movColuna));
            }
        }
        return movimentos;
    }

    /**
     * Troca dois valores de posição em uma matriz, dados os indíces das
     * posições dos dois valores
     *
     * @param matriz a matriz com os valores a serem trocados
     * @param linhaA número da linha do primeiro valor
     * @param colunaA número da coluna do pirmeiro valor
     * @param linhaB número da linha do segundo valor
     * @param colunaB número da coluna do segundo valor
     * @return uma cópia da matriz original, porém com os valores trocados de
     * posição
     */
    public int[][] movimentarMatriz(int[][] matriz, int linhaA, int colunaA, int linhaB, int colunaB) {
        int[][] novaMatriz = new int[matriz.length][matriz[0].length];
        for (int i = 0; i < 3; i++) {
            novaMatriz[i] = (int[]) Arrays.copyOf(matriz[i], 3);
        }
        novaMatriz[linhaB][colunaB] = novaMatriz[linhaA][colunaA];
        novaMatriz[linhaA][colunaA] = 0;
        linhaB = linhaA;
        colunaB = colunaA;
        return novaMatriz;
    }

    /**
     * Gera uma matriz com as novas disposições do tabuleiro dadas as posições
     * disponíveis a partir do tabuleiro atual
     *
     * @return a matriz gerada
     */
    public int[][][] matrizMovimentosDisponiveis() {
        int[][] matrizOriginal = new int[3][3];
        for (int i = 0; i < 3; i++) {
            matrizOriginal[i] = (int[]) Arrays.copyOf(casas[i], 3);
        }
        int[][][] matriz = new int[4][3][3];
        ArrayList<Movimento> movimentosDispo = movimentosDisponiveis();
        int i = 0;
        for (Movimento movimento : movimentosDispo) {
            matriz[i] = movimentarMatriz(matrizOriginal, movimentosDispo.get(i).linha, movimentosDispo.get(i).coluna, linhaLivre, colunaLivre);
        }
        return matriz;
    }


    public boolean movimentarPeca(Movimento movimento) {
        int linha = movimento.linha;
        int coluna = movimento.coluna;
        if (linha < 0 || linha > 2 || coluna < 0 || coluna > 2) {
            return false;
        }
        ArrayList<Movimento> movimentos = movimentosDisponiveis();
        if(movimentos.contains(movimento));
        boolean aux = false;
        casas[linhaLivre][colunaLivre] = casas[linha][coluna];
        casas[linha][coluna] = 0;
        linhaLivre = linha;
        colunaLivre = coluna;
        return true;
    }

    /**
     * Gera uma string que imprime a disposição atual do tabuleiro
     *
     * @return a {@code String} gerada
     */
    public String imprimirTabuleiro() {
        String s = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                s += "|  " + casas[i][j];
                s += "  ";
            }
            s += "|\n";
        }
        return s;
    }

    /**
     * Embaralha a disposição do tabuleiro {@code n} vezes
     *
     * @param n número de movimentos que serão feitos para embaralhar o
     * tabuleiro
     */
    public void embaralharTabuleiro(int n) {
        for (int i = 0; i < n; i++) {
            Movimento movimentoAtual = null;
            ArrayList<Movimento> movimentos = movimentosDisponiveis();
            int pos = ThreadLocalRandom.current().nextInt(0, movimentos.size());
            movimentoAtual = movimentos.get(pos);
            while (movimentoAtual == null) {
                pos += 1;
                if (pos > 3) {
                    pos = 0;
                }
                if (pos < 0) {
                    pos = 2;
                }
                movimentoAtual = movimentos.get(pos);
            }
            movimentarPeca(movimentoAtual);
        }
    }

    /**
     * Utiliza busca aleatória para tentar encontrar uma solução para o jogo dos
     * quadradinhos
     *
     * @param limite o número máximo de movimentos que deverão ser realizados
     * para tentar solucionar o jogo dos quadradinhos
     * @return o número de movimentos necessários para solucionar o jogo, ou -1
     * caso execeda o limite de movimentos
     */
    public int buscaAleatoria(int limite) {
        int movimentos = 1;
        while (movimentos < limite) {
            embaralharTabuleiro(1);
            if (verificarVitoria()) {
                return movimentos;
            }
            movimentos++;
        }
        return -1;
    }

    
    public Double verificarHeuristica(int[][] matriz, int linha, int coluna) {
        if (matriz == null) {
            return Double.MAX_VALUE;
        }
        Double val = 0.;
        int casa = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == linha && j == coluna) {
                    continue;
                }
                val += Math.sqrt(Math.pow((matriz[i][j] - casa), 2));
                casa++;
            }
        }
        return val;
    }

    public int[][] copiarTabuleiro(){
        int[][] tabuleiroAtual = new int[3][];
        for (int i = 0; i < 3; i++) {
            tabuleiroAtual[i] = (int[]) Arrays.copyOf(casas[i], 3);
        }
        return tabuleiroAtual;  
    }
    
    public boolean verificaIgual(int[][] matriz) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (casas[i][j] != matriz[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    
    public int buscaHeuristica1Nivel(int limite) {
        int nMovimentos = 0;
        
        ArrayList<int[][]> tabuleiroAnterior = new ArrayList();
        ArrayList<Integer> indexAnterior = new ArrayList();
        ArrayList<ArrayList<Movimento>> ultimosMovimentos = new ArrayList();
        int evitaLoop = -1;
        while (nMovimentos < limite) {
            // Condição de Parada
            if (verificarVitoria()) {
                return nMovimentos;
            }
            
            // Verificar Heuristica
            Double heuristicaAtual, heuristicaProx;
            ArrayList<Movimento> movimentosDispo = movimentosDisponiveis();
            int[][][] matrizMovimentos = matrizMovimentosDisponiveis();
            int index = 0;
            ultimosMovimentos.add(movimentosDispo);
            while(matrizMovimentos[index] == null){
                index++;
            }
            heuristicaAtual = verificarHeuristica(matrizMovimentos[index], movimentosDispo.get(index).linha, movimentosDispo.get(index).coluna);           
            int i = index;
            while (i < movimentosDispo.size()) {                
                if(matrizMovimentos[i] == null) continue;
                heuristicaProx = verificarHeuristica(matrizMovimentos[i], movimentosDispo.get(i).linha, movimentosDispo.get(i).coluna);
                if(heuristicaAtual > heuristicaProx && !movimentosDispo.get(i).verificado) {
                    heuristicaAtual = heuristicaProx;
                    index = i;
                    evitaLoop = -1;
                    movimentosDispo.get(i).verificado = true;
                }
                i++;
            }
            
            //Verificar Loop
            boolean loop = false;
            int indLoop = -1;
            
            for (int[][] mat : matrizMovimentos) {
                if(mat != null && verificaIgual(mat)) {
                    loop = true;
                    indLoop++;
                }
            }
            if(loop) {
                evitaLoop = indexAnterior.get(indLoop);
                nMovimentos++;
                continue;
            }
            
            
            //Movimentar
            int[][] tabuleiroAtual = copiarTabuleiro();
            tabuleiroAnterior.add(tabuleiroAtual);
            indexAnterior.add(index);
            movimentarPeca(movimentosDispo.get(index));
            nMovimentos++;
        }
        return -1;
    }

    // TODO
    public void arquivoMovimentos(File arquivo) {

    }
}
