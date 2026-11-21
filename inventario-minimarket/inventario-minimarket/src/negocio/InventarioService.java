package negocio;

import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class InventarioService {
    public static final int STOCK_MINIMO_POR_DEFECTO = 5;
    private final Map<String, Producto> productos = new LinkedHashMap<>();
    private int contador = 0;
    private String generarCodigo() {
        contador++;
        return String.format("P%03d", contador);
    }
    public Producto registrarPerecedero(String nombre, String cantidad, String precio,
                                        String stockMinimo, String fechaVencimiento)
            throws DatosInvalidosException {
        String n = Validador.textoObligatorio(nombre, "nombre");
        int c = Validador.enteroNoNegativo(cantidad, "cantidad");
        double p = Validador.decimalPositivo(precio, "precio");
        int s = leerStockMinimo(stockMinimo);
        LocalDate f = Validador.fechaFutura(fechaVencimiento, "fecha de vencimiento");
        Producto nuevo = new ProductoPerecedero(generarCodigo(), n, c, p, s, f);
        productos.put(nuevo.getCodigo(), nuevo);
        return nuevo;
    }
    public Producto registrarNoPerecedero(String nombre, String cantidad, String precio,
                                          String stockMinimo, String categoria)
            throws DatosInvalidosException {
        String n = Validador.textoObligatorio(nombre, "nombre");
        int c = Validador.enteroNoNegativo(cantidad, "cantidad");
        double p = Validador.decimalPositivo(precio, "precio");
        int s = leerStockMinimo(stockMinimo);
        String cat = Validador.textoObligatorio(categoria, "categoria");
        Producto nuevo = new ProductoNoPerecedero(generarCodigo(), n, c, p, s, cat);
        productos.put(nuevo.getCodigo(), nuevo);
        return nuevo;
    }
    private int leerStockMinimo(String valor) throws DatosInvalidosException {
        if (valor == null || valor.trim().isEmpty()) {
            return STOCK_MINIMO_POR_DEFECTO;
        }
        return Validador.enteroNoNegativo(valor, "stock minimo");
    }
    public List<Producto> listar() {
        return Collections.unmodifiableList(new ArrayList<>(productos.values()));
    }
    public Producto buscar(String codigo) throws ProductoNoEncontradoException {
        String clave = codigo == null ? "" : codigo.trim().toUpperCase();
        Producto p = productos.get(clave);
        if (p == null) {
            throw new ProductoNoEncontradoException(clave);
        }
        return p;
    }
    public Producto registrarEntrada(String codigo, String unidades)
            throws ProductoNoEncontradoException, DatosInvalidosException {
        Producto p = buscar(codigo);
        p.aumentarStock(Validador.enteroPositivo(unidades, "unidades"));
        return p;
    }
    public Producto registrarSalida(String codigo, String unidades)
            throws ProductoNoEncontradoException, DatosInvalidosException, StockInsuficienteException {
        Producto p = buscar(codigo);
        int u = Validador.enteroPositivo(unidades, "unidades");
        if (u > p.getCantidad()) {
            throw new StockInsuficienteException(p.getNombre(), p.getCantidad(), u);
        }
        p.disminuirStock(u);
        return p;
    }
    public Producto eliminar(String codigo) throws ProductoNoEncontradoException {
        Producto p = buscar(codigo);
        productos.remove(p.getCodigo());
        return p;
    }
    public List<Producto> productosConStockBajo() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos.values()) {
            if (p.tieneStockBajo()) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    public List<ProductoPerecedero> productosPorVencer() {
        List<ProductoPerecedero> resultado = new ArrayList<>();
        for (Producto p : productos.values()) {
            if (p instanceof ProductoPerecedero) {
                ProductoPerecedero pp = (ProductoPerecedero) p;
                if (pp.estaVencido() || pp.estaProximoAVencer()) {
                    resultado.add(pp);
                }
            }
        }
        return resultado;
    }
    public double valorTotalInventario() {
        double total = 0;
        for (Producto p : productos.values()) {
            total += p.calcularValorInventario();
        }
        return total;
    }
    public void cargarDatosDemo() throws DatosInvalidosException {
        LocalDate hoy = LocalDate.now();
        registrarPerecedero("Leche entera 1L", "40", "0.95", "10", hoy.plusDays(10).toString());
        registrarPerecedero("Yogurt natural", "12", "0.60", "10", hoy.plusDays(2).toString());
        registrarPerecedero("Queso fresco", "8", "2.10", "5", hoy.plusDays(1).toString());
        registrarNoPerecedero("Tornillo", "200", "0.50", "50", "Ferreteria");
        registrarNoPerecedero("Martillo", "4", "8.75", "5", "Herramientas");
        registrarNoPerecedero("Detergente 1kg", "30", "3.20", "10", "Limpieza");
    }
}
