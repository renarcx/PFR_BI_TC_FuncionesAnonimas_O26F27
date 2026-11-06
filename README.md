# PFR_BI_TC_FuncionesAnonimas_O26F27
--------------------------------------------------------------------
## Parte 1 – Explorar: la programación funcional en el mundo real

### Caso 1: Apache Spark (Scala)

- **Problema que resuelve:** procesar grandes volúmenes de datos repartidos en un clúster de
  máquinas (conteo de palabras, limpieza de datos, análisis de logs, etc.).
- **Lenguaje / herramienta:** Spark está escrito en Scala y ofrece también API en Python y Java.
  La documentación de Cloudera muestra el mismo programa de conteo de palabras en Scala, Python y
  Java, y en las dos primeras versiones casi todo el trabajo lo hacen funciones anónimas.
- **Ventaja del enfoque funcional:** el programador describe *qué* transformación aplicar
  (map, filter, reduceByKey) pasando una función, y Spark decide cómo repartir ese trabajo
  entre los nodos. Como las funciones son valores, Spark puede enviar la lambda a cada máquina.
  Además, los datos (RDD) son inmutables: cada transformación produce un conjunto nuevo, así que
  no hay estado compartido que sincronizar.
- **Fragmento de codigo** :


val data = sc.textFile("hdfs://master:9000/books/*.txt")

val words = data.flatMap("""[a-zA-ZÀ-ÿ`'-]+""".r findAllIn _).map(word => (word, 1)).reduceByKey(_ + _).sortBy(_._2, ascending = false)


  **Qué hace:** flatMap convierte cada línea en sus palabras (el _ es la lambda
  línea => regex.findAllIn(línea)); map(word => (word, 1)) asigna un 1 a cada palabra;
  reduceByKey(_ + _) suma los unos de cada palabra (la lambda (a, b) => a + b)  ;
  sortBy(_._2, ascending = false) ordena de mayor a menor frecuencia.
- **Fuentes:**
  - <https://github.com/16pxdesign/scala-spark-wordcount>
  - <https://docs.cloudera.com/documentation/enterprise/5-9-x/topics/spark_develop_run.html>
  - <https://sparkbyexamples.com/spark/spark-word-count-example/>

------------------------------

### Caso 2: Jane Street (OCaml)

- **Problema que resuelve:** Jane Street es una firma de trading propietario. Necesita sistemas
  de trading donde un error cuesta mucho dinero y donde hay que reaccionar rápido a cambios del
  mercado.
- **Lenguaje:** OCaml, un lenguaje funcional con tipado estático. Según su artículo en el
  *Journal of Functional Programming* (2008), la empresa usa OCaml como plataforma principal de
  desarrollo, algo poco común en Wall Street; en ese momento tenía más de veinte programadores de
  OCaml y cientos de miles de líneas de código.
- **Ventaja del enfoque funcional:**
  - *Detección de errores:* Yaron Minsky, jefe de tecnología, explicó en QCon que buena parte de
    la programación diaria es análisis de casos y que cualquier ayuda del sistema para atrapar
    errores vale mucho (cobertura de Computerworld).
  - *Menos mutación de estado:* los programas funcionan evaluando expresiones, no modificando
    celdas dentro de las estructuras de datos.
  - *Rapidez:* según Minsky, el primer prototipo de la reescritura de 2005 estuvo listo en tres
    meses y operando en el mercado tres meses después.
- **Fragmento de código:** 


let ordenes = [ 50.0; 120.0; 300.0; 80.0 ]

let resultado =

  ordenes
  |> List.filter (fun precio -> precio > 100.0)   (fun ... -> ... es la lambda de OCaml )
  
  |> List.map (fun precio -> precio *. 1.02)      (comisión del 2%)
(resultado = [122.4; 306.] )


  **Qué hace:** filtra las órdenes mayores a 100, les suma una comisión del 2% y encadena los
  pasos con |>.
- **Fuentes:**
  - <https://www.cambridge.org/core/product/02F18023B4C43BF6E53512AA7062A9A5>
  - <https://www.computerworld.com/?p=1395355>
  - <https://research.tedneward.com/languages/ml/ocaml-for-the-masses/index.html>

---

------------------------------------------------------

## Parte 2 – Entender: ¿qué es una función anónima?

### ¿Qué es y de dónde viene el nombre «lambda»?

Es una función sin nombre, que se escribe justo donde se necesita. En lista.filter(p => p.precio > 10)
la parte p => p.precio > 10 es la función anónima.


val cuadrado = (x: Int) => x * x       // la función se guarda en una variable

println(((x: Int) => x * x)(4))        // 16, sin darle nombre a la función


El nombre viene del cálculo lambda, un sistema matemático que Alonzo Church planteó en los
años 30 para estudiar qué se puede calcular. Allí una función se escribe λx. x*x. Lenguajes
como LISP tomaron la idea y algunos conservaron la palabra (lambda en Python); otros usan una
flecha (=> en Scala, -> en Java).

### Diferencia con una función nombrada (misma lógica)


def esPar(n: Int): Boolean = n % 2 == 0          // nombrada

val pares1 = List(1, 2, 3, 4, 5, 6).filter(esPar)


val pares2 = List(1, 2, 3, 4, 5, 6).filter(n => n % 2 == 0)   // anónima

val pares3 = List(1, 2, 3, 4, 5, 6).filter(_ % 2 == 0)        // anónima abreviada

// Las tres dan List(2, 4, 6)


Hacen lo mismo. La anónima conviene para lógica corta que se usa una sola vez. Si es larga o
se reutiliza, conviene darle nombre para poder probarla y documentarla.

### Funciones de primera clase

Significa que una función se trata como cualquier otro valor: se puede guardar en una variable,
pasar como argumento, devolver desde otra función y guardar en estructuras de datos.


def aplicar[A, B](f: A => B, x: A): B = f(x)     // f es un parámetro más

def crearDoblador(): Int => Int = x => x * 2     // devuelve una función


println(aplicar((s: String) => s.length, "hola"))  // 4

println(List(1, 2, 3).map(x => x + 1))             // List(2, 3, 4)


map y filter aceptan una lambda porque declaran un parámetro de tipo función (A => B). Si
las funciones no fueran valores, no se podría pasar una como argumento.

### Closure

Es una función que recuerda las variables del lugar donde fue creada, incluso cuando ese
lugar ya terminó de ejecutarse.

scala
def crearMultiplicador(factor: Int): Int => Int =

  x => x * factor            // 'factor' está definido fuera de la lambda
  

val porTres = crearMultiplicador(3)

println(porTres(5))          // 15: sigue recordando que factor = 3




var umbral = 10

val f = (x: Int) => x > umbral

umbral = 100

println(f(50))               // false: f usa el umbral actual (100), no el que había al crearla



---------------------------------------
Parte 3 – Aplicar: ¿en que problemas sirven las funciones anonimas?
-

Esta es la parte central del trabajo. Vas a identificar los tipos de problemas que
se resuelven con funciones anónimas y a resolver cada uno con lambdas en Scala, Java y
Python. La comparación entre los lenguajes se hace en la Parte 5.

3.1 Catalogo de tipos de problemas
-
Cada operacion vista en clase resuelve un tipo de problema. Documenta al menos 4
de los 7 tipos de la siguiente tabla:

<img width="551" height="263" alt="image" src="https://github.com/user-attachments/assets/75c4d75a-2043-4973-bf06-ac02c3dc409c" />



### 3.1 Selección: ¿qué elementos cumplen una condición?

**Situación real:** un banco necesita detectar las transacciones mayores a $1 000. Aquí: los productos con precio mayor a 100.

### scala

productos.filter(p => p.precio > 100)  

<img width="467" height="110" alt="image" src="https://github.com/user-attachments/assets/2f875e9e-5c5e-4c03-82cc-6bf270879801" />


### java

productos.stream().filter(p -> p.precio() > 100).toList();

<img width="434" height="101" alt="image" src="https://github.com/user-attachments/assets/f389de9d-b101-4ae2-a900-80402e979d5a" />


### python

list(filter(lambda p: p.precio > 100, productos)) 

<img width="651" height="107" alt="image" src="https://github.com/user-attachments/assets/79f71bda-92ed-45c6-b167-255e7aa90060" />


**Salida:** Laptop - 850.00, Monitor - 220.00, Silla - 130.00, Escritorio - 310.00


### 3.2 Transformación: ¿cómo convierto cada elemento en otro valor?

**Situación real:** una tienda debe imprimir en la etiqueta el precio final con impuesto (aquí, un IVA de ejemplo del 15%).

### scala

productos.map(p => s"${p.nombre} -> ${fmt(p.precio * 1.15)}")

<img width="443" height="98" alt="image" src="https://github.com/user-attachments/assets/ed312d80-77dd-4591-b554-a59a5a6eb400" />


### java

productos.stream().map(p -> p.nombre() + " -> " + fmt(p.precio() * 1.15))

<img width="434" height="84" alt="image" src="https://github.com/user-attachments/assets/ce8f5be9-216c-47f1-8ff7-4487912868a3" />


### python

map(lambda p: f"{p.nombre} -> {fmt(p.precio * 1.15)}", productos)

<img width="651" height="91" alt="image" src="https://github.com/user-attachments/assets/e087330e-9a82-41e3-ab7b-2f2a92a12b7a" />


**Salida (primeros 4):** Laptop -> 977.50, Mouse -> 17.25, Teclado -> 51.75, Monitor -> 253.00


### 3.3 Validación: ¿existe alguno? ¿todos cumplen?

**Situación real:** el sistema de inventario debe avisar si hay productos agotados y verificar que todos superen un precio mínimo.

### scala

productos.exists(p => p.stock == 0)        productos.forall(p => p.precio > 1)

<img width="443" height="98" alt="image" src="https://github.com/user-attachments/assets/eb14ccab-07e9-42d6-9451-57bef3f878b4" />

 
### java

productos.stream().anyMatch(p -> p.stock() == 0)    productos.stream().allMatch(p -> p.precio() > 1)

  <img width="429" height="54" alt="image" src="https://github.com/user-attachments/assets/61cab736-1016-4442-946c-98cc0beeb47a" />

 
### python

any(map(lambda p: p.stock == 0, productos))         all(map(lambda p: p.precio > 1, productos))

<img width="651" height="54" alt="image" src="https://github.com/user-attachments/assets/a9d6cc3d-59e3-43dc-8d08-ad263842d48f" />

 
**Salida:** 

- Hay productos agotados: true
  
- Todos cuestan mas de 1: false (el lápiz cuesta 0.80)



### 3.4 Conteo: ¿cuántos cumplen una condición?

**Situación real:** el encargado de compras quiere saber cuántos productos necesitan reposición (stock menor a 20).

### scala

productos.count(p => p.stock < 20)

<img width="210" height="75" alt="image" src="https://github.com/user-attachments/assets/987a9f09-c2e8-43db-9f41-842a4f1737cf" />

 
### java

productos.stream().filter(p -> p.stock() < 20).count()      // no existe count(condición)

<img width="429" height="38" alt="image" src="https://github.com/user-attachments/assets/8ce8713e-18ef-4c4e-a7c0-dadccca4e856" />

 
### python

len(list(filter(lambda p: p.stock < 20, productos)))        # no existe count(condición)

<img width="467" height="33" alt="image" src="https://github.com/user-attachments/assets/14fa28e3-a55e-46d4-9e17-11727763f76a" />

 
**Salida:** Productos con poco stock: 4



### 3.5 Extremos: ¿cuál es el mejor o el peor según un criterio?

**Situación real:** un reporte debe mostrar el producto más caro y el más barato del catálogo.

### scala

productos.maxBy(p => p.precio)      productos.minBy(p => p.precio)

<img width="202" height="75" alt="image" src="https://github.com/user-attachments/assets/fc5773b8-3a7d-4ac6-80b1-af3e0abef1c6" />

 
### java

productos.stream().max((a, b) -> Double.compare(a.precio(), b.precio())).get()   // lambda de DOS parámetros

  <img width="429" height="51" alt="image" src="https://github.com/user-attachments/assets/e6f96e18-a9b9-435f-a8e4-5a91bd5ceb81" />

 
### python

max(productos, key=lambda p: p.precio)      min(productos, key=lambda p: p.precio)

<img width="467" height="53" alt="image" src="https://github.com/user-attachments/assets/e831011d-4659-4d34-9e77-cf758441c191" />


**Salida:** 

- Mas caro: Laptop (850.00) 
- Mas barato: Lapiz (0.80)`



### 3.6 Aplanamiento: juntar en una sola lista los resultados de cada elemento

**Situación real:** cada pedido trae su propia lista de productos; para el reporte de ventas se necesita una lista única de todos los productos vendidos.

### scala

pedidos.flatMap(ped => ped.items)

<img width="754" height="75" alt="image" src="https://github.com/user-attachments/assets/49878b16-5889-4435-8a0e-2907acb62a69" />

 
### java

pedidos.stream().flatMap(ped -> ped.items().stream()).toList()

<img width="651" height="54" alt="image" src="https://github.com/user-attachments/assets/82ad6858-777d-4c7e-bfe7-5631b3e31573" />

 
### python

list(chain.from_iterable(map(lambda ped: ped.items, pedidos)))      # Python no tiene flatMap

<img width="467" height="86" alt="image" src="https://github.com/user-attachments/assets/1e1137d5-c886-4371-b07d-e9ad7de7916d" />

 
**Salida:** Laptop, Mouse, Mouse, Teclado, Mochila, Cuaderno, Lapiz, Mouse y Productos distintos vendidos: 6



### 3.7 Acumulación: un valor que resume la lista y cómo evoluciona

**Situación real:** contabilidad necesita el valor total del inventario (precio × stock) y ver cómo crece al sumar producto por producto.

### scala

productos.foldLeft(0.0)((acc, p) => acc + p.precio * p.stock)

productos.scanLeft(0.0)((acc, p) => acc + p.precio * p.stock).tail

<img width="486" height="75" alt="image" src="https://github.com/user-attachments/assets/ce11bc68-6b8f-4a96-99c6-a2a3ef82a1ba" />


### java

productos.stream().reduce(0.0, (acc, p) -> acc + p.precio() * p.stock(), Double::sum)

// scanLeft no existe: se improvisa con un arreglo de 1 elemento (ver java/Parte3.java)

<img width="651" height="69" alt="image" src="https://github.com/user-attachments/assets/f7b1085a-cdae-4fd1-bdf2-c49fdaf55521" />

 
### python

reduce(lambda acc, p: acc + p.precio * p.stock, productos, 0.0)

list(accumulate(productos, lambda acc, p: acc + p.precio * p.stock, initial=0.0))[1:]

<img width="467" height="110" alt="image" src="https://github.com/user-attachments/assets/d2bc7dc6-fd8b-48c4-95fd-1c855d29ea5a" />

 
**Salida:** 

- Valor total del inventario: 27162.50
- 
- Acumulado (primeros 5): 8500.00, 10300.00, 13000.00, 18500.00, 20450.00



-------------------
## Parte 4 – Ampliar: cuatro operaciones nuevas


### Ficha 1: sortBy
1. **Qué hace:** ordena la lista según un criterio que yo elijo.
2. **Lambda:** recibe un Producto y devuelve el criterio de orden (un valor comparable, aquí Double). El signo - invierte el orden.
3. **Ejemplo:**

scala

productos.sortBy(p => -p.precio).take(3)

// Laptop - 850.00 / Escritorio - 310.00 / Monitor - 220.00

4. **Problema que resuelve:** un catálogo web que muestra «los más caros primero». Es un tipo nuevo (Ordenamiento), emparentado con Extremos, porque maxBy solo devuelve el primero y sortBy devuelve todos en orden.

### Ficha 2: groupBy
1. **Qué hace:** separa los elementos en grupos según una llave y devuelve un Map de llave → lista.
2. **Lambda:** recibe un Producto y devuelve la llave del grupo (aquí, la categoría).
3. **Ejemplo:**

scala

productos.groupBy(p => p.categoria).toList.sortBy(par => par._1)

// Accesorios: 1 / Muebles: 3 / Papeleria: 3 / Tecnologia: 5

4. **Problema que resuelve:** un reporte de inventario por categoría. Es un tipo nuevo (Agrupación); es la base de los «resúmenes por grupo» (equivale al GROUP BY de SQL).

### Ficha 3: partition
1. **Qué hace:** divide la lista en dos según una condición y conserva **ambos** grupos.
2. **Lambda:** recibe un Producto y devuelve Boolean; el resultado es una pareja (cumplen, no cumplen).
3. **Ejemplo:**
   
scala

val (conStock, pocoStock) = productos.partition(p => p.stock > 20)

// conStock (8): Mouse, Teclado, Monitor, Lampara, Cuaderno, Lapiz, Marcador, Mochila

// pocoStock (4): Laptop, Silla, Escritorio, Audifonos

5. **Problema que resuelve:** separar los pedidos listos para enviar de los que esperan reposición. Se parece a **Selección** (filter), pero sin descartar el resto: recorre la lista una sola vez y devuelve las dos mitades.

### Ficha 4: zipWithIndex
1. **Qué hace:** agrega a cada elemento su posición, devolviendo parejas (elemento, índice).
2. **Lambda:** **no recibe ninguna**. Se encadena con map, que sí recibe una que desarma la pareja.
3. **Ejemplo:**
   
scala

productos.sortBy(p => -p.precio).zipWithIndex.take(3).map { case (p, i) => s"${i + 1}. ${p.nombre}" }

// 1. Laptop / 2. Escritorio / 3. Monitor

5. **Problema que resuelve:** numerar un ranking o una lista en pantalla. Se parece a Transformación, porque el paso final es un map.

### 4.3 Combinar operaciones

**Pregunta:** ¿cuáles son las 3 categorías con mayor valor de inventario (precio × stock), de mayor a menor?

scala

productos
  .groupBy(p => p.categoria)                
  .map { case (cat, ps) => (cat, ps.map(p => p.precio * p.stock).sum) }   
  .toList
  .sortBy(par => -par._2)                         
  .take(3)
// Tecnologia: 18500.00 / Muebles: 5550.00 / Accesorios: 1575.00


1. groupBy(p => p.categoria) reúne los productos de cada categoría en un Map.
2. map { case (cat, ps) => ... } convierte cada grupo en una pareja (categoría, valor), sumando precio * stock de sus productos.
3. .toList pasa el Map a lista, porque sortBy es un método de listas.
4. sortBy(par => -par._2) ordena por el valor, de mayor a menor.

## Parte 5 – Comparar y concluir

### 5.1 Scala, Java y Python frente a frente


| Criterio | Scala | Java | Python |
|---|---|---|---|
| **Sintaxis de la lambda** | p => p.precio > 100 o _.precio > 100 | p -> p.precio() > 100 | lambda p: p.precio > 100 |
| **Cómo se declara el tipo** | Se infiere; opcional: (p: Producto) => ... | Se infiere del contexto: el compilador sabe que filter espera un Predicate<Producto> | No se declara (tipado dinámico) |
| **Operaciones con listas disponibles** | Métodos directos en la colección: filter, map, exists, forall, count, maxBy, minBy, flatMap, foldLeft, scanLeft | Streams (.stream() + operaciones + .toList()): filter, map, anyMatch, allMatch, max, min, flatMap, reduce. No hay count(condición) ni `scanLeft | Funciones sueltas: filter, map, any, all, max/min con key, reduce, accumulate. No hay flatMap (se usa chain.from_iterable) ni count(condición) |
| **Restricción del lenguaje** | Ninguna que me afectara en estos ejemplos | La lambda solo puede usar variables locales *final* o *efectivamente final*; no puede modificar una variable externa | Una lambda solo admite **una expresión**: sin sentencias ni asignaciones |
| **Legibilidad** | La más limpia: la cadena se lee de izquierda a derecha sin pasos extra | Clara, pero más larga por .stream(), .toList() y los accesores p.precio() | Muy corta para casos simples; se complica al anidar map/filter hacia adentro |

**¿En qué lenguaje fue más natural resolver los problemas?** En Scala. Cada uno de los siete
tipos de problema tiene una operación con nombre propio en la colección (count, maxBy,
flatMap, scanLeft...), así que la solución se escribe casi como se dice la pregunta. En Java
y Python hubo que adaptar varias soluciones.

**¿En qué tipo de problema hubo más diferencias?** En Acumulación Scala resuelve el
valor final y la evolución con foldLeft y scanLeft. Python tiene reduce y accumulate,
pero repartidos entre dos módulos. Java no tiene scanLeft, y para la evolución paso a paso
tuve que usar un arreglo de un elemento como «caja» mutable, precisamente porque la lambda no puede
modificar una variable local. Además de ser menos elegante, ese truco solo es seguro con streams
secuenciales. 

### 5.2 Conclusión

Las funciones anónimas son la pieza que hace posibles las operaciones con listas: filter,
map o foldLeft solo definen el esqueleto del recorrido, y la lambda aporta el criterio que
cambia en cada problema. Gracias a eso se puede resolver con una línea y sin bucles manuales
cada uno de los siete tipos de problema de la Parte 3, y combinarlos en cadenas sin
variables intermedias que mutar.

El caso de Apache Spark muestra la importancia a gran escala: en
reduceByKey(_ + _) la lambda es lo único que escribe el programador; Spark la envía a los
nodos del clúster y decide cómo repartir el trabajo. Es la misma idea que foldLeft de mi
ejemplo, pero ejecutada sobre muchas máquinas. Que las funciones sean valores de primera clase es
lo que permite pasarlas de un lugar a otro, tanto dentro de mi programa como a un clúster.

## Referencias

- <https://github.com/16pxdesign/scala-spark-wordcount>
- <https://docs.cloudera.com/documentation/enterprise/5-9-x/topics/spark_develop_run.html>
-  <https://sparkbyexamples.com/spark/spark-word-count-example/>
-  <https://www.cambridge.org/core/product/02F18023B4C43BF6E53512AA7062A9A5>
- <https://www.computerworld.com/?p=1395355>
- <https://research.tedneward.com/languages/ml/ocaml-for-the-masses/index.html>
- <https://docs.scala-lang.org>
- <https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html>
-  <https://docs.python.org/3/howto/functional.html>



