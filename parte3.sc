// Parte 3 - Siete tipos de problemas con funciones anonimas (Scala). Un solo archivo.

  case class Producto(nombre: String, categoria: String, precio: Double, stock: Int)

  val productos = List(
    Producto("Laptop",     "Tecnologia", 850.0, 10),
    Producto("Mouse",      "Tecnologia",  15.0, 120),
    Producto("Teclado",    "Tecnologia",  45.0, 60),
    Producto("Monitor",    "Tecnologia", 220.0, 25),
    Producto("Silla",      "Muebles",    130.0, 15),
    Producto("Escritorio", "Muebles",    310.0, 8),
    Producto("Lampara",    "Muebles",     28.0, 40),
    Producto("Cuaderno",   "Papeleria",    4.0, 200),
    Producto("Lapiz",      "Papeleria",    0.8, 500),
    Producto("Marcador",   "Papeleria",   2.25, 150),
    Producto("Mochila",    "Accesorios",  35.0, 45),
    Producto("Audifonos",  "Tecnologia",  60.0, 0)
  )
  // Cada pedido es una lista de nombres de productos (para flatMap)
  val pedidos = List(
    List("Laptop", "Mouse"),
    List("Mouse", "Teclado", "Mochila"),
    List("Cuaderno", "Lapiz", "Mouse")
  )


    // 3.1 Seleccion: filter
    println("3.1 precio > 100: " + productos.filter(p => p.precio > 100).map(p => p.nombre).mkString(", "))

    // 3.2 Transformacion: map (precio con descuento de 10%)
    println("3.2 con descuento: " + productos.map(p => p.precio * 0.9).take(4).mkString(", "))

    // 3.3 Validacion: exists y forall
    println("3.3 hay agotados: " + productos.exists(p => p.stock == 0))
    println("3.3 todos cuestan mas de 1: " + productos.forall(p => p.precio > 1))

    // 3.4 Conteo: count
    println("3.4 con stock < 20: " + productos.count(p => p.stock < 20))

    // 3.5 Extremos: maxBy y minBy
    println("3.5 mas caro: " + productos.maxBy(p => p.precio).nombre)
    println("3.5 mas barato: " + productos.minBy(p => p.precio).nombre)

    // 3.6 Aplanamiento: flatMap
    val todos = pedidos.flatMap(pedido => pedido)
    println("3.6 todos los items: " + todos.mkString(", "))
    println("3.6 productos distintos: " + todos.distinct.size)

    // 3.7 Acumulacion: foldLeft (total) y scanLeft (paso a paso)
    println("3.7 valor del inventario: " + productos.foldLeft(0.0)((acc, p) => acc + p.precio * p.stock))
    println("3.7 acumulado: " + productos.scanLeft(0.0)((acc, p) => acc + p.precio * p.stock).tail.take(5).mkString(", "))

