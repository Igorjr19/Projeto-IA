
import Model.Tabuleiro;

/**
 *
 * @author Igor Rodrigues
 */
public class Testes {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        System.out.println(tabuleiro.imprimirTabuleiro());
        int[][][] matriz = tabuleiro.matrizMovimentosDisponiveis();
        for (int k = 0; k < 4; k++) {
            if(matriz[k] == null){
                continue;
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print("|  " + matriz[k][i][j] + "  ");
                }
                System.out.print("|");
                System.out.println("");
            }
            System.out.println("\n");
        }
       
    }
}
