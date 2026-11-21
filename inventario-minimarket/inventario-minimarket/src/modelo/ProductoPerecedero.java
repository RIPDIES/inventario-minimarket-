package modelo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class ProductoPerecedero extends Producto {
    public static final double MARGEN = 0.25;
    public static final int DIAS_ALERTA = 3;
    public static final double DESCUENTO_PROXIMO = 0.30;
    private LocalDate fechaVencimiento;
    public ProductoPerecedero(String codigo, String nombre, int cantidad, double precioCosto,
                              int stockMinimo, LocalDate fechaVencimiento) {
        super(codigo, nombre, cantidad, precioCosto, stockMinimo);
        setFechaVencimiento(fechaVencimiento);
    }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
        }
        this.fechaVencimiento = fechaVencimiento;
    }
    public long diasParaVencer() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    public boolean estaVencido() {
        return diasParaVencer() < 0;
    }

    public boolean estaProximoAVencer() {
        long dias = diasParaVencer();
        return dias >= 0 && dias <= DIAS_ALERTA;
    }
    @Override
    public double calcularPrecioVenta() {
        if (estaVencido()) {
            return 0.0;
        }
        double precio = getPrecioCosto() * (1 + MARGEN);
        if (estaProximoAVencer()) {
            precio = precio * (1 - DESCUENTO_PROXIMO);
        }
        return precio;
    }
    @Override
    public String getTipo() { return "Perecedero"; }
    @Override
    public String getDetalleEspecifico() {
        return "vence: " + fechaVencimiento + " (" + diasParaVencer() + " dias)";
    }
}
