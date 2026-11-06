// Datos compartidos por todos los ejemplos en Scala (Parte 3 y Parte 4).

case class Producto(nombre: String, categoria: String, precio: Double, stock: Int)
case class Pedido(id: Int, items: List[String])

object Datos {
  val productos: List[Producto] = List(
    Producto("Laptop",     "Tecnologia", 850.00, 10),
    Producto("Mouse",      "Tecnologia",  15.00, 120),
    Producto("Teclado",    "Tecnologia",  45.00, 60),
    Producto("Monitor",    "Tecnologia", 220.00, 25),
    Producto("Silla",      "Muebles",    130.00, 15),
    Producto("Escritorio", "Muebles",    310.00, 8),
    Producto("Lampara",    "Muebles",     28.00, 40),
    Producto("Cuaderno",   "Papeleria",    4.00, 200),
    Producto("Lapiz",      "Papeleria",    0.80, 500),
    Producto("Marcador",   "Papeleria",    2.25, 150),
    Producto("Mochila",    "Accesorios",  35.00, 45),
    Producto("Audifonos",  "Tecnologia",  60.00, 0)
  )

  // Se usa solo en el problema de aplanamiento (flatMap)
  val pedidos: List[Pedido] = List(
    Pedido(1, List("Laptop", "Mouse")),
    Pedido(2, List("Mouse", "Teclado", "Mochila")),
    Pedido(3, List("Cuaderno", "Lapiz", "Mouse"))
  )
}
