package negocio;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
public final class Validador {
    private Validador() { }
    public static String textoObligatorio(String valor, String campo) throws DatosInvalidosException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatosInvalidosException("El campo '" + campo + "' es obligatorio y no puede estar vacio.");
        }
        return valor.trim();
    }
    public static int enteroNoNegativo(String valor, String campo) throws DatosInvalidosException {
        String texto = textoObligatorio(valor, campo);
        int numero;
        try {
            numero = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new DatosInvalidosException("El campo '" + campo + "' debe ser un numero entero.");
        }
        if (numero < 0) {
            throw new DatosInvalidosException("El campo '" + campo + "' no puede ser negativo.");
        }
        return numero;
    }
    public static int enteroPositivo(String valor, String campo) throws DatosInvalidosException {
        int numero = enteroNoNegativo(valor, campo);
        if (numero == 0) {
            throw new DatosInvalidosException("El campo '" + campo + "' debe ser mayor a 0.");
        }
        return numero;
    }
    public static double decimalPositivo(String valor, String campo) throws DatosInvalidosException {
        String texto = textoObligatorio(valor, campo).replace(',', '.');
        double numero;
        try {
            numero = Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            throw new DatosInvalidosException("El campo '" + campo + "' debe ser un numero valido (ej. 0.50).");
        }
        if (numero <= 0) {
            throw new DatosInvalidosException("El campo '" + campo + "' debe ser mayor a 0.");
        }
        return numero;
    }

    public static LocalDate fechaFutura(String valor, String campo) throws DatosInvalidosException {
        String texto = textoObligatorio(valor, campo);
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(texto);
        } catch (DateTimeParseException e) {
            throw new DatosInvalidosException("El campo '" + campo + "' debe tener formato AAAA-MM-DD (ej. 2026-12-31).");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha de vencimiento no puede ser anterior a hoy.");
        }
        return fecha;
    }
}
