// Parte 3 - Siete tipos de problemas resueltos con funciones anonimas (Scala).
// Ejecutar: ver README (seccion "Como ejecutar").

object Parte3 {
  // Formato con 2 decimales y punto decimal (igual en los 3 lenguajes)
  def fmt(x: Double): String = "%.2f".formatLocal(java.util.Locale.US, x)

  def main(args: Array[String]): Unit = {
    import Datos._

    // 3.1 SELECCION: filter recibe una lambda Producto => Boolean
    println("=== 3.1 Seleccion (filter): precio > 100 ===")
    val caros = productos.filter(p => p.precio > 100)
    caros.foreach(p => println(s"${p.nombre} - ${fmt(p.precio)}"))

    // 3.2 TRANSFORMACION: map recibe una lambda Producto => B (aqui, un String)
    println("=== 3.2 Transformacion (map): precio con IVA 15% ===")
    val conIva = productos.map(p => s"${p.nombre} -> ${fmt(p.precio * 1.15)}")
    conIva.take(4).foreach(println)

    // 3.3 VALIDACION: exists / forall reciben Producto => Boolean y devuelven Boolean
    println("=== 3.3 Validacion (exists, forall) ===")
    val hayAgotados = productos.exists(p => p.stock == 0)
    val todosMasDe1 = productos.forall(p => p.precio > 1)
    println(s"Hay productos agotados: $hayAgotados")
    println(s"Todos cuestan mas de 1: $todosMasDe1")

    // 3.4 CONTEO: count recibe Producto => Boolean y devuelve Int
    println("=== 3.4 Conteo (count): stock < 20 ===")
    println(s"Productos con poco stock: ${productos.count(p => p.stock < 20)}")

    // 3.5 EXTREMOS: maxBy / minBy reciben Producto => criterio comparable
    println("=== 3.5 Extremos (maxBy, minBy) por precio ===")
    val masCaro   = productos.maxBy(p => p.precio)
    val masBarato = productos.minBy(p => p.precio)
    println(s"Mas caro: ${masCaro.nombre} (${fmt(masCaro.precio)})")
    println(s"Mas barato: ${masBarato.nombre} (${fmt(masBarato.precio)})")

    // 3.6 APLANAMIENTO: flatMap recibe Pedido => List[String] y concatena los resultados
    println("=== 3.6 Aplanamiento (flatMap): items de todos los pedidos ===")
    val todos = pedidos.flatMap(ped => ped.items)
    println(s"Todos los items: ${todos.mkString(", ")}")
    println(s"Productos distintos vendidos: ${todos.distinct.size}")

    // 3.7 ACUMULACION: foldLeft (resumen final) y scanLeft (evolucion paso a paso)
    println("=== 3.7 Acumulacion (foldLeft, scanLeft): valor del inventario ===")
    val total = productos.foldLeft(0.0)((acc, p) => acc + p.precio * p.stock)
    println(s"Valor total del inventario: ${fmt(total)}")
    val evolucion = productos.scanLeft(0.0)((acc, p) => acc + p.precio * p.stock).tail
    println(s"Acumulado (primeros 5): ${evolucion.take(5).map(fmt).mkString(", ")}")
  }
}
