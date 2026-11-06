// Parte 4 - sortBy, groupBy, partition, zipWithIndex y una cadena que las combina.

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


    // sortBy: la lambda devuelve el criterio de orden (el "-" ordena de mayor a menor)
    println("sortBy: " + productos.sortBy(p => -p.precio).take(3).map(p => p.nombre))

    // groupBy: la lambda devuelve la llave del grupo
    println("groupBy: " + productos.groupBy(p => p.categoria).map { case (cat, ps) => (cat, ps.size) }.toList.sorted)

    // partition: la lambda devuelve Boolean; el resultado es una pareja de listas
    val (buenStock, pocoStock) = productos.partition(p => p.stock > 20)
    println("partition buen stock: " + buenStock.map(p => p.nombre))
    println("partition poco stock: " + pocoStock.map(p => p.nombre))

    // zipWithIndex: no recibe lambda; se encadena con map para armar un ranking
    println("zipWithIndex: " + productos.sortBy(p => -p.precio).zipWithIndex.take(3)
      .map { case (p, i) => s"${i + 1}. ${p.nombre}" })

    // 4.3 Combinacion: las 3 categorias con mayor valor de inventario
    val top3 = productos
      .groupBy(p => p.categoria)                                                  // nueva
      .map { case (cat, ps) => (cat, ps.map(p => p.precio * p.stock).sum) }       // vista en clase
      .toList
      .sortBy(par => -par._2)                                                     // nueva
      .take(3)
    println("4.3 top 3 categorias: " + top3)


