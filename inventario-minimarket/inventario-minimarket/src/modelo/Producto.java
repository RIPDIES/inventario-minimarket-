package modelo;
public abstract class Producto {
    private final String codigo;
    private String nombre;
    private int cantidad;
    private double precioCosto;
    private int stockMinimo;
    protected Producto(String codigo, String nombre, int cantidad, double precioCosto, int stockMinimo) {
        this.codigo = codigo;
        setNombre(nombre);
        setCantidad(cantidad);
        setPrecioCosto(precioCosto);
        setStockMinimo(stockMinimo);
    }
    public abstract double calcularPrecioVenta();
    public abstract String getTipo();
    public abstract String getDetalleEspecifico();
    public double calcularValorInventario() {
        return cantidad * precioCosto;
    }
    public boolean tieneStockBajo() {
        return cantidad <= stockMinimo;
    }
    public void aumentarStock(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades a ingresar deben ser mayores a 0.");
        }
        this.cantidad += unidades;
    }
    public void disminuirStock(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades a retirar deben ser mayores a 0.");
        }
        if (unidades > cantidad) {
            throw new IllegalArgumentException("No hay stock suficiente.");
        }
        this.cantidad -= unidades;
    }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }
    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.cantidad = cantidad;
    }
    public double getPrecioCosto() { return precioCosto; }

    public void setPrecioCosto(double precioCosto) {
        if (precioCosto <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        this.precioCosto = precioCosto;
    }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock minimo no puede ser negativo.");
        }
        this.stockMinimo = stockMinimo;
    }
    @Override
    public String toString() {
        return String.format(java.util.Locale.US,
                "%-5s | %-16s | %-13s | cant: %4d | costo: $%7.2f | venta: $%7.2f | %s",
                codigo, nombre, getTipo(), cantidad, precioCosto, calcularPrecioVenta(), getDetalleEspecifico());
    }
}
