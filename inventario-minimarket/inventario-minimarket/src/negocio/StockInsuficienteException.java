package negocio;
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String nombre, int disponible, int solicitado) {
        super("Stock insuficiente de '" + nombre + "': disponible " + disponible
                + ", solicitado " + solicitado + ".");
    }
}
