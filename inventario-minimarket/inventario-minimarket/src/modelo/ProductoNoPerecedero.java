package modelo;
public class ProductoNoPerecedero extends Producto {
    public static final double MARGEN = 0.35;
    private String categoria;
    public ProductoNoPerecedero(String codigo, String nombre, int cantidad, double precioCosto,
                                int stockMinimo, String categoria) {
        super(codigo, nombre, cantidad, precioCosto, stockMinimo);
        setCategoria(categoria);
    }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoria es obligatoria.");
        }
        this.categoria = categoria.trim();
    }
    @Override
    public double calcularPrecioVenta() {
        return getPrecioCosto() * (1 + MARGEN);
    }
    @Override
    public String getTipo() { return "No perecedero"; }
    @Override
    public String getDetalleEspecifico() { return "categoria: " + categoria; }
}
