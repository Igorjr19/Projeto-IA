package Model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * @author Igor Rodrigues e Lucas Ikeda
 */
public class Tabuleiro {
    
    /***** Atributos *****/
    
    private int[][] casas;
    private int linhaLivre;
    private int colunaLivre;
    
    /***** Construtor *****/
    
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

    /***** Getters *****/
    public int[][] getCasas() {
        return casas;
    }

    public int getLinhaLivre() {
        return linhaLivre;
    }

    public int getColunaLivre() {
        return colunaLivre;
    }
    
    
    /***** Métodos *****/
    
    /**
     * Verifica se o tabuleiro está posicionado de acordo com a condição de vitória do jogo
     * @return {@code true} se o jogo foi vencido e {@code false} caso contrário 
     */
    public boolean verificarVitoria(){
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if(casas[i - 1][j - 1] != i * j) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Calcula as casas que podem ocupar o espaço livre
     * @return um array contendo um array cujo indíce 0 é a linha da casa e o índice 1 é a coluna da casa,
     * o array será igual a null caso não seja uma casa válida do tabuleiro
     */
    public int[][] movimentosDisponiveis(){
        int pos[][] = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
        
        int[][] movimentos;
        movimentos = new int[4][2];
        for(int i = 0; i < 4; i++) {
            movimentos[i][0] = linhaLivre + pos[i][0];
            movimentos[i][1] = colunaLivre + pos[i][1];
            if(movimentos[i][1] > 2 || movimentos[i][0] > 2 || linhaLivre == -1 || colunaLivre == -1) {
                movimentos[i] = null;
            }
        } 
        return movimentos;
    }
    
    /**
     * 
     * @param linha número da linha do tabuleiro da casa que vai ser movimentada
     * @param coluna número da coluna do tabuleiro da casa que vai ser movimentada
     * @return {@code true} caso seja possível realizar o movimento e {@code false} caso contrário 
     */
    public boolean movimentarPeca(int linha, int coluna) {
        if(linha < 0 || linha > 2 || coluna < 0 || coluna > 2) {
            return false;
        }
        int[][] movimentos = movimentosDisponiveis();
        boolean aux = false;
        for(int i = 0; i < 4; i++){
            if(movimentos[i] != null && linha == movimentos[i][0] & coluna == movimentos[i][1]){
                aux = true;
                break;
            }
        }
        if(aux == false) {
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
     * @return a {@code String} gerada
     */
    public String imprimirTabuleiro(){
        String s = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                s += "  " + casas[i][j];
                s += "  |";
            }
            s += "\n";
        }
        return s;
    }
    
    /**
     * Embaralha a disposição do tabuleiro {@code n} vezes
     * @param n número de movimentos que serão feitos para embaralhar o tabuleiro
     */
    public void embaralharTabuleiro(int n) {
        for (int i = 0; i < n; i++) {
            int movimentoAtual[] = null;
            int[][] movimentos = movimentosDisponiveis();
            int pos = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            movimentoAtual = movimentos[pos];
            while(movimentoAtual == null) {
                pos += 1;
                if(pos > 3) {
                    pos = 0;
                }
                if(pos < 0) {
                    pos = 2;
                }
                movimentoAtual = movimentos[pos];
            }
            movimentarPeca(movimentoAtual[0], movimentoAtual[1]);
        }
    }
}
