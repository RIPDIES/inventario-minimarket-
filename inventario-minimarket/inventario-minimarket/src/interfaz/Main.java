package interfaz;

import modelo.Producto;
import modelo.ProductoPerecedero;
import negocio.DatosInvalidosException;
import negocio.InventarioService;
import negocio.ProductoNoEncontradoException;
import negocio.StockInsuficienteException;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private final InventarioService servicio = new InventarioService();
    private final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        new Main().ejecutar();
    }

    private void ejecutar() {
        String opcion;
        do {
            mostrarMenu();
            opcion = leer("Seleccione una opcion: ");
            if (opcion == null) {
                break;
            }
            procesar(opcion.trim());
        } while (!opcion.trim().equals("0"));
        System.out.println("Hasta pronto.");
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== INVENTARIO - MINIMARKET LOS ANDES =====");
        System.out.println("1. Registrar producto perecedero");
        System.out.println("2. Registrar producto no perecedero");
        System.out.println("3. Listar inventario");
        System.out.println("4. Buscar producto por codigo");
        System.out.println("5. Registrar entrada de stock");
        System.out.println("6. Registrar salida de stock");
        System.out.println("7. Eliminar producto");
        System.out.println("8. Ver alertas (stock bajo / por vencer)");
        System.out.println("9. Valor total del inventario");
        System.out.println("10. Cargar datos de demostracion");
        System.out.println("0. Salir");
    }

    private void procesar(String opcion) {
        try {
            switch (opcion) {
                case "1": registrarPerecedero(); break;
                case "2": registrarNoPerecedero(); break;
                case "3": listar(); break;
                case "4": buscar(); break;
                case "5": entradaStock(); break;
                case "6": salidaStock(); break;
                case "7": eliminar(); break;
                case "8": alertas(); break;
                case "9": valorTotal(); break;
                case "10": servicio.cargarDatosDemo(); System.out.println("Datos de demostracion cargados."); break;
                case "0": break;
                default: System.out.println("Opcion no valida.");
            }
        } catch (DatosInvalidosException | ProductoNoEncontradoException | StockInsuficienteException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void registrarPerecedero() throws DatosInvalidosException {
        String nombre = leer("Nombre: ");
        String cantidad = leer("Cantidad: ");
        String precio = leer("Precio de costo: ");
        String minimo = leer("Stock minimo (Enter = 5): ");
        String fecha = leer("Fecha de vencimiento (AAAA-MM-DD): ");
        Producto p = servicio.registrarPerecedero(nombre, cantidad, precio, minimo, fecha);
        System.out.println("Producto registrado con codigo " + p.getCodigo() + ".");
    }

    private void registrarNoPerecedero() throws DatosInvalidosException {
        String nombre = leer("Nombre: ");
        String cantidad = leer("Cantidad: ");
        String precio = leer("Precio de costo: ");
        String minimo = leer("Stock minimo (Enter = 5): ");
        String categoria = leer("Categoria: ");
        Producto p = servicio.registrarNoPerecedero(nombre, cantidad, precio, minimo, categoria);
        System.out.println("Producto registrado con codigo " + p.getCodigo() + ".");
    }

    private void listar() {
        List<Producto> lista = servicio.listar();
        if (lista.isEmpty()) {
            System.out.println("El inventario esta vacio.");
            return;
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private void buscar() throws ProductoNoEncontradoException {
        System.out.println(servicio.buscar(leer("Codigo: ")));
    }

    private void entradaStock() throws ProductoNoEncontradoException, DatosInvalidosException {
        String codigo = leer("Codigo: ");
        String unidades = leer("Unidades a ingresar: ");
        Producto p = servicio.registrarEntrada(codigo, unidades);
        System.out.println("Entrada registrada. Stock actual de " + p.getNombre() + ": " + p.getCantidad());
    }

    private void salidaStock() throws ProductoNoEncontradoException, DatosInvalidosException,
            StockInsuficienteException {
        String codigo = leer("Codigo: ");
        String unidades = leer("Unidades a retirar: ");
        Producto p = servicio.registrarSalida(codigo, unidades);
        System.out.println("Salida registrada. Stock actual de " + p.getNombre() + ": " + p.getCantidad());
    }

    private void eliminar() throws ProductoNoEncontradoException {
        Producto p = servicio.eliminar(leer("Codigo: "));
        System.out.println("Producto eliminado: " + p.getNombre());
    }

    private void alertas() {
        System.out.println("--- Productos con stock bajo ---");
        List<Producto> bajos = servicio.productosConStockBajo();
        if (bajos.isEmpty()) {
            System.out.println("Ninguno.");
        }
        for (Producto p : bajos) {
            System.out.println(p.getCodigo() + " " + p.getNombre()
                    + " (stock " + p.getCantidad() + ", minimo " + p.getStockMinimo() + ")");
        }
        System.out.println("--- Perecederos vencidos o por vencer ---");
        List<ProductoPerecedero> porVencer = servicio.productosPorVencer();
        if (porVencer.isEmpty()) {
            System.out.println("Ninguno.");
        }
        for (ProductoPerecedero p : porVencer) {
            String estado = p.estaVencido() ? "VENCIDO" : "vence en " + p.diasParaVencer() + " dias";
            System.out.println(p.getCodigo() + " " + p.getNombre() + " - " + estado);
        }
    }

    private void valorTotal() {
        System.out.println(String.format(Locale.US, "Valor total del inventario (a costo): $%.2f",
                servicio.valorTotalInventario()));
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        return entrada.hasNextLine() ? entrada.nextLine() : null;
    }
}
