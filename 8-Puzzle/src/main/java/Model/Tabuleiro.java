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

    /**
     * *** Atributos ****
     */
    private int[][] casas;
    private int linhaLivre;
    private int colunaLivre;

    /**
     * *** Construtor ****
     */
    /**
     * Instancia um novo Tabuleiro
     */
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

    /**
     * *** Getters ****
     */
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
     * *** Métodos ****
     */
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

    /**
     * Calcula as casas que podem ocupar o espaço livre
     *
     * @return um array contendo um array cujo indíce 0 é a linha da casa e o
     * índice 1 é a coluna da casa, o array será igual a null caso não seja uma
     * casa válida do tabuleiro
     */
    public int[][] movimentosDisponiveis() {
        int pos[][] = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

        int[][] movimentos;
        movimentos = new int[4][2];
        for (int i = 0; i < 4; i++) {
            movimentos[i][0] = linhaLivre + pos[i][0];
            movimentos[i][1] = colunaLivre + pos[i][1];
            if (movimentos[i][1] > 2 || movimentos[i][0] > 2 || movimentos[i][1] < 0 || movimentos[i][0] < 0) {
                movimentos[i] = null;
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
        int[][] movimentosDispo = movimentosDisponiveis();
        for (int i = 0; i < 4; i++) {
            if (movimentosDispo[i] == null) {
                matriz[i] = null;
            } else {
                matriz[i] = movimentarMatriz(matrizOriginal, movimentosDispo[i][0], movimentosDispo[i][1], linhaLivre, colunaLivre);
            }
        }
        return matriz;
    }

    /**
     *
     * @param linha número da linha do tabuleiro da casa que vai ser movimentada
     * @param coluna número da coluna do tabuleiro da casa que vai ser
     * movimentada
     * @return {@code true} caso seja possível realizar o movimento e
     * {@code false} caso contrário
     */
    public boolean movimentarPeca(int linha, int coluna) {
        if (linha < 0 || linha > 2 || coluna < 0 || coluna > 2) {
            return false;
        }
        int[][] movimentos = movimentosDisponiveis();
        boolean aux = false;
        for (int i = 0; i < 4; i++) {
            if (movimentos[i] != null && linha == movimentos[i][0] & coluna == movimentos[i][1]) {
                aux = true;
                break;
            }
        }
        if (aux == false) {
            return false;
        }
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
            int movimentoAtual[] = null;
            int[][] movimentos = movimentosDisponiveis();
            int pos = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            movimentoAtual = movimentos[pos];
            while (movimentoAtual == null) {
                pos += 1;
                if (pos > 3) {
                    pos = 0;
                }
                if (pos < 0) {
                    pos = 2;
                }
                movimentoAtual = movimentos[pos];
            }
            movimentarPeca(movimentoAtual[0], movimentoAtual[1]);
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

    
    public Double verificarHeuristica(int[][] matriz) {
        if (matriz == null) {
            return Double.MAX_VALUE;
        }
        Double val = 0.;
        int casa = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 2 && j == 2) {
                    break;
                }
                val += Math.sqrt(Math.pow((casa - matriz[i][j]), 2));
                casa++;
            }
        }
        return val;
    }

    public int buscaHeuristica1Nivel(int limite) {
        ArrayList<Integer[]> movimentos = new ArrayList();
        int nMovimentos = 0;
        ArrayList<int[][]> movimentosAlternativos = new ArrayList();
        while (nMovimentos < limite) {
            if (verificarVitoria()) {
                return nMovimentos;
            }
            
            int[][] movimentosPossiveis = movimentosDisponiveis();
            int[][][] matrizMovimentosDispo = matrizMovimentosDisponiveis();
            Double valorAtualHeuristica = Double.MAX_VALUE;
            Double valorProximaHeuristica = Double.MAX_VALUE;
            Double valorAlternativoHeuristica = Double.MAX_VALUE;
            Integer[] movimento = new Integer[2];
            
            // Vetor para verificação de loop
            if (movimentos.size() > 3) {
                movimentos.remove(0);
            }
            
            movimentosPossiveis = movimentosDisponiveis();
            valorAtualHeuristica = verificarHeuristica(matrizMovimentosDispo[0]);
            int index = 0;
            for (int i = 1; i < 4; i++) {
                if (movimentosPossiveis[i] == null) {
                    continue;
                }
                valorProximaHeuristica = verificarHeuristica(matrizMovimentosDispo[i]);
                if (valorAtualHeuristica > valorProximaHeuristica) {
                    valorAtualHeuristica = valorProximaHeuristica;
                    index = i;
                }
                
            }
            
            // Verficar Loop
            boolean loop = false;
            int loopIndex = -1;
            for (int j = 0; j < 3 && movimentos.size() > 3; j++) {
                if (movimentosPossiveis[index][0] == movimentos.get(j)[0] && movimentosPossiveis[index][1] == movimentos.get(j)[1]) {
                    loop = true;
                    loopIndex = j;
                }
            }
            if (loop) {
                casas = movimentosAlternativos.get(movimentosAlternativos.size() - (loopIndex + 1));
                continue;
            }
            movimentarPeca(movimentosPossiveis[index][0], movimentosPossiveis[index][1]);
            imprimirTabuleiro();
            nMovimentos++;
            movimento[0] = movimentosPossiveis[index][0];
            movimento[1] = movimentosPossiveis[index][1];
            movimentos.add(movimento);
        }
        return -1;
    }

    // TODO
    public void arquivoMovimentos(File arquivo) {

    }
}
