
import Model.Tabuleiro;

/**
 *
 * @author Igor Rodrigues
 */
public class Testes {
    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro();
        System.out.println(tabuleiro.imprimirTabuleiro());
        tabuleiro.movimentarPeca(1, 2);
        System.out.println(tabuleiro.imprimirTabuleiro());
    }
}
