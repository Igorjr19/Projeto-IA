package Model;

/**
 *
 * @author Igor Rodrigues e Lucas Ikeda
 */
public class Movimento {
    protected int linha;
    protected int coluna;
    protected boolean verificado;
    
    public Movimento(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        verificado = false;
    }
    
    
}
