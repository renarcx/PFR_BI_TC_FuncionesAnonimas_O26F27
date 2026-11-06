// Parte 4 - Cuatro operaciones nuevas: sortBy, groupBy, partition, zipWithIndex
// y una cadena que las combina con una operacion vista en clase (map).
// Requiere scala/Datos.scala (ver README, "Como ejecutar").

object Operaciones {
  def fmt(x: Double): String = "%.2f".formatLocal(java.util.Locale.US, x)

  def main(args: Array[String]): Unit = {
    import Datos._

    // ---------- Ficha 1: sortBy ----------
    // La lambda recibe un Producto y devuelve el CRITERIO de orden (aqui un Double).
    // El signo "-" invierte el orden (de mayor a menor).
    println("=== sortBy: los 3 productos mas caros ===")
    productos.sortBy(p => -p.precio).take(3)
             .foreach(p => println(s"${p.nombre} - ${fmt(p.precio)}"))

    // ---------- Ficha 2: groupBy ----------
    // La lambda recibe un Producto y devuelve la LLAVE del grupo (aqui, la categoria).
    // Resultado: Map[String, List[Producto]]
    println("=== groupBy: cantidad de productos por categoria ===")
    productos.groupBy(p => p.categoria).toList.sortBy(par => par._1)
             .foreach { case (cat, ps) => println(s"$cat: ${ps.size}") }

    // ---------- Ficha 3: partition ----------
    // La lambda devuelve Boolean; el resultado es una pareja (cumplen, no cumplen).
    println("=== partition: stock > 20 ===")
    val (conStock, pocoStock) = productos.partition(p => p.stock > 20)
    println(s"Con buen stock (${conStock.size}): ${conStock.map(p => p.nombre).mkString(", ")}")
    println(s"Con poco stock (${pocoStock.size}): ${pocoStock.map(p => p.nombre).mkString(", ")}")

    // ---------- Ficha 4: zipWithIndex ----------
    // NO recibe lambda: agrega la posicion a cada elemento -> (elemento, indice).
    // Se encadena con map (que si recibe lambda) para construir un ranking.
    println("=== zipWithIndex + map: ranking de los 3 mas caros ===")
    productos.sortBy(p => -p.precio).zipWithIndex.take(3)
             .map { case (p, i) => s"${i + 1}. ${p.nombre}" }
             .foreach(println)

    // ---------- 4.3 Combinacion ----------
    // Pregunta: "Cuales son las 3 categorias con mayor valor de inventario
    //            (precio x stock), de mayor a menor?"
    println("=== 4.3 Combinacion: groupBy -> map -> sortBy ===")
    val top3 = productos
      .groupBy(p => p.categoria)                                  // (nueva) agrupa por categoria
      .map { case (cat, ps) =>                                    // (vista en clase) resume cada grupo
        (cat, ps.map(p => p.precio * p.stock).sum) }
      .toList                                                     // Map -> List (sortBy es de listas)
      .sortBy(par => -par._2)                                     // (nueva) mayor valor primero
      .take(3)
    top3.foreach { case (cat, valor) => println(s"$cat: ${fmt(valor)}") }
  }
}
