package negocio;
public class ProductoNoEncontradoException extends Exception {
    public ProductoNoEncontradoException(String codigo) {
        super("No existe un producto con el codigo '" + codigo + "'.");
    }
}
