

import java.util.*;
import java.util.stream.*;

public class Parte3 {

    static String fmt(double x) { return String.format(Locale.US, "%.2f", x); }

    public static void main(String[] args) {
        List<Producto> productos = List.of(
            new Producto("Laptop",     "Tecnologia", 850.00, 10),
            new Producto("Mouse",      "Tecnologia",  15.00, 120),
            new Producto("Teclado",    "Tecnologia",  45.00, 60),
            new Producto("Monitor",    "Tecnologia", 220.00, 25),
            new Producto("Silla",      "Muebles",    130.00, 15),
            new Producto("Escritorio", "Muebles",    310.00, 8),
            new Producto("Lampara",    "Muebles",     28.00, 40),
            new Producto("Cuaderno",   "Papeleria",    4.00, 200),
            new Producto("Lapiz",      "Papeleria",    0.80, 500),
            new Producto("Marcador",   "Papeleria",    2.25, 150),
            new Producto("Mochila",    "Accesorios",  35.00, 45),
            new Producto("Audifonos",  "Tecnologia",  60.00, 0)
        );
        List<Pedido> pedidos = List.of(
            new Pedido(1, List.of("Laptop", "Mouse")),
            new Pedido(2, List.of("Mouse", "Teclado", "Mochila")),
            new Pedido(3, List.of("Cuaderno", "Lapiz", "Mouse"))
        );

        // 3.1 SELECCION: filter recibe un Predicate<Producto> (lambda Producto -> boolean)
        System.out.println("=== 3.1 Seleccion (filter): precio > 100 ===");
        productos.stream()
                 .filter(p -> p.precio() > 100)
                 .forEach(p -> System.out.println(p.nombre() + " - " + fmt(p.precio())));

        // 3.2 TRANSFORMACION: map recibe una Function<Producto, String>
        System.out.println("=== 3.2 Transformacion (map): precio con IVA 15% ===");
        productos.stream()
                 .map(p -> p.nombre() + " -> " + fmt(p.precio() * 1.15))
                 .limit(4)
                 .forEach(System.out::println);

        // 3.3 VALIDACION: anyMatch / allMatch (equivalentes de exists / forall)
        System.out.println("=== 3.3 Validacion (anyMatch, allMatch) ===");
        boolean hayAgotados = productos.stream().anyMatch(p -> p.stock() == 0);
        boolean todosMasDe1 = productos.stream().allMatch(p -> p.precio() > 1);
        System.out.println("Hay productos agotados: " + hayAgotados);
        System.out.println("Todos cuestan mas de 1: " + todosMasDe1);

        // 3.4 CONTEO: no hay count(pred); se filtra y luego se cuenta
        System.out.println("=== 3.4 Conteo (filter + count): stock < 20 ===");
        long pocoStock = productos.stream().filter(p -> p.stock() < 20).count();
        System.out.println("Productos con poco stock: " + pocoStock);

        // 3.5 EXTREMOS: max / min reciben un Comparator (lambda de DOS parametros)
        System.out.println("=== 3.5 Extremos (max, min) por precio ===");
        Producto masCaro   = productos.stream()
                .max((a, b) -> Double.compare(a.precio(), b.precio())).get();
        Producto masBarato = productos.stream()
                .min((a, b) -> Double.compare(a.precio(), b.precio())).get();
        System.out.println("Mas caro: " + masCaro.nombre() + " (" + fmt(masCaro.precio()) + ")");
        System.out.println("Mas barato: " + masBarato.nombre() + " (" + fmt(masBarato.precio()) + ")");

        // 3.6 APLANAMIENTO: flatMap recibe una lambda que devuelve un Stream
        System.out.println("=== 3.6 Aplanamiento (flatMap): items de todos los pedidos ===");
        List<String> todos = pedidos.stream()
                .flatMap(ped -> ped.items().stream())
                .toList();
        System.out.println("Todos los items: " + String.join(", ", todos));
        System.out.println("Productos distintos vendidos: " + todos.stream().distinct().count());

        // 3.7 ACUMULACION: reduce (equivale a foldLeft). No existe scanLeft en Streams.
        System.out.println("=== 3.7 Acumulacion (reduce, acumulado manual): valor del inventario ===");
        double total = productos.stream()
                .reduce(0.0, (acc, p) -> acc + p.precio() * p.stock(), Double::sum);
        System.out.println("Valor total del inventario: " + fmt(total));

        // Para la evolucion paso a paso (scanLeft) hay que improvisar: una lambda NO puede
        // modificar una variable local (debe ser "efectivamente final"), asi que se usa
        // un arreglo de 1 elemento como "caja" mutable. Solo es seguro en streams secuenciales.
        double[] acumulado = {0.0};
        List<Double> evolucion = productos.stream()
                .map(p -> acumulado[0] += p.precio() * p.stock())
                .toList();
        String primeros5 = evolucion.stream().limit(5).map(Parte3::fmt).collect(Collectors.joining(", "));
        System.out.println("Acumulado (primeros 5): " + primeros5);
    }
}

record Producto(String nombre, String categoria, double precio, int stock) {}
record Pedido(int id, List<String> items) {}
