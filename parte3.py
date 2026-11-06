"""Parte 3 - Siete tipos de problemas resueltos con lambda (Python 3.8+).
Ejecutar: python python/parte3.py
"""
from dataclasses import dataclass
from functools import reduce
from itertools import accumulate, chain


@dataclass(frozen=True)
class Producto:
    nombre: str
    categoria: str
    precio: float
    stock: int


@dataclass(frozen=True)
class Pedido:
    id: int
    items: list


productos = [
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
    Producto("Audifonos",  "Tecnologia",  60.00, 0),
]
pedidos = [
    Pedido(1, ["Laptop", "Mouse"]),
    Pedido(2, ["Mouse", "Teclado", "Mochila"]),
    Pedido(3, ["Cuaderno", "Lapiz", "Mouse"]),
]


def fmt(x):
    return f"{x:.2f}"


def bool_txt(b):
    return str(b).lower()   # Python imprime True/False; aqui true/false como Scala y Java


# 3.1 SELECCION: filter recibe una lambda que devuelve bool
print("=== 3.1 Seleccion (filter): precio > 100 ===")
caros = filter(lambda p: p.precio > 100, productos)
for p in caros:
    print(f"{p.nombre} - {fmt(p.precio)}")

# 3.2 TRANSFORMACION: map recibe una lambda Producto -> str
print("=== 3.2 Transformacion (map): precio con IVA 15% ===")
con_iva = list(map(lambda p: f"{p.nombre} -> {fmt(p.precio * 1.15)}", productos))
for linea in con_iva[:4]:
    print(linea)

# 3.3 VALIDACION: any / all (aqui combinadas con map + lambda)
print("=== 3.3 Validacion (any, all) ===")
hay_agotados = any(map(lambda p: p.stock == 0, productos))
todos_mas_de_1 = all(map(lambda p: p.precio > 1, productos))
print(f"Hay productos agotados: {bool_txt(hay_agotados)}")
print(f"Todos cuestan mas de 1: {bool_txt(todos_mas_de_1)}")

# 3.4 CONTEO: no existe count(pred); se filtra y se mide la longitud
print("=== 3.4 Conteo (filter + len): stock < 20 ===")
poco_stock = len(list(filter(lambda p: p.stock < 20, productos)))
print(f"Productos con poco stock: {poco_stock}")

# 3.5 EXTREMOS: max / min con key=lambda
print("=== 3.5 Extremos (max, min) por precio ===")
mas_caro = max(productos, key=lambda p: p.precio)
mas_barato = min(productos, key=lambda p: p.precio)
print(f"Mas caro: {mas_caro.nombre} ({fmt(mas_caro.precio)})")
print(f"Mas barato: {mas_barato.nombre} ({fmt(mas_barato.precio)})")

# 3.6 APLANAMIENTO: Python no tiene flatMap; se usa chain.from_iterable + map
print("=== 3.6 Aplanamiento (chain.from_iterable + map): items de todos los pedidos ===")
todos = list(chain.from_iterable(map(lambda ped: ped.items, pedidos)))
print(f"Todos los items: {', '.join(todos)}")
print(f"Productos distintos vendidos: {len(set(todos))}")

# 3.7 ACUMULACION: reduce (foldLeft) y accumulate (scanLeft)
print("=== 3.7 Acumulacion (reduce, accumulate): valor del inventario ===")
total = reduce(lambda acc, p: acc + p.precio * p.stock, productos, 0.0)
print(f"Valor total del inventario: {fmt(total)}")
evolucion = list(accumulate(productos, lambda acc, p: acc + p.precio * p.stock, initial=0.0))[1:]
print(f"Acumulado (primeros 5): {', '.join(map(fmt, evolucion[:5]))}")
