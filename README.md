# Trabajo Práctico Opcional - Matemática Discreta

Implementación y análisis de dos algoritmos sobre grafos ponderados:

- **Dijkstra:** cálculo de caminos mínimos mediante una cola de prioridad.
- **Kruskal:** construcción de un árbol de expansión mínima mediante Union-Find.

---

# 1) Algoritmo de Dijkstra con cola de prioridad

## Qué resuelve

Dado un grafo con pesos **no negativos** y un vértice de origen, calcula la **distancia mínima** desde el origen hasta cada vértice.

**Idea:** siempre se avanza desde el vértice pendiente **más cercano** al origen, y desde ahí se intenta mejorar la distancia de sus vecinos. La cola de prioridad sirve para obtener rápido ese vértice más cercano.

## Piezas

- **`Arista(destino, peso)`**: ruta que sale de un vértice. `peso` es el costo de **esa ruta sola**. El vértice de salida lo indica la lista donde está guardada: `grafo.get(u)` son las aristas que salen de `u`.
- **`dist[]`**: el **índice es el vértice**. `dist[v]` guarda la mejor distancia conocida desde el origen hasta `v`. Hay una sola por vértice, y se actualiza cada vez que hay una mejora.
- **`Nodo(vertice, distancia)`**: anotación que se guarda en la cola. Significa *"encontré una forma de llegar a `vertice` con costo total `distancia`"*. Implementa `Comparable` para que la cola saque siempre primero el de menor distancia.

## `dist` vs `Nodo`

Cuando se encuentra un camino mejor hasta `v`, pasan dos cosas juntas: se actualiza `dist[v]` y se mete un `Nodo(v, nueva)` en la cola. En ese momento las dos distancias coinciden.

La diferencia es que `dist[v]` **cambia**, mientras que un `Nodo` **queda fijo**. Si después aparece un camino todavía mejor a `v`, `dist[v]` baja, y el Nodo anterior sigue en la cola con el valor viejo. Por eso puede haber varios Nodos del mismo vértice, y solo uno coincide con `dist`.

Cuando sale un Nodo con distancia mayor a `dist[v]`, está **desactualizado** y se ignora:

```java
if (actual.distancia > dist[u]) continue;
```

## Por qué inicializar en infinito

`Integer.MAX_VALUE` (el `int` más grande) representa *"todavía no sé cómo llegar"*. Como el algoritmo solo actualiza cuando `nueva < dist[destino]`, el primer camino real que se encuentre siempre va a ser menor y va a reemplazarlo. Si al final un vértice sigue en infinito, es inalcanzable.

El origen empieza en 0 porque llegar a él no cuesta nada.

## Por qué `nueva = dist[u] + a.peso`

`dist[u]` es el costo del origen a `u`, y `a.peso` es el costo de `u` al destino. La suma es el costo **total** del origen al destino pasando por `u`. `dist` siempre guarda totales desde el origen, nunca tramos sueltos.
Para que se ejecute esta línea, `u` tiene que haber salido de la cola. Es decir, debe ser un posible mejor camino.

## Pasos

1. `dist` en infinito para todos, y 0 para el origen. Meter `Nodo(origen, 0)` en la cola.
2. Mientras la cola no esté vacía:
   1. Sacar el Nodo de menor distancia. Su vértice es `u`.
   2. Si está desactualizado, ignorarlo.
   3. Para cada arista que sale de `u`, calcular `nueva = dist[u] + peso`. Si es menor que `dist[destino]`, actualizar `dist[destino]` y meter `Nodo(destino, nueva)` en la cola.
3. Devolver `dist`.

## Por qué funciona

Cuando un vértice sale de la cola con la menor distancia, esa distancia ya es definitiva. Cualquier otro camino hacia él pasaría por un vértice que está más lejos, y como los pesos no son negativos, sumar más tramos nunca abarata el camino. Por eso Dijkstra **no funciona con pesos negativos**.

## Complejidad

En cada paso hay que encontrar el vértice pendiente más cercano. Con V = cantidad de vértices y E = cantidad de aristas:

- **Sin cola de prioridad:** buscar el mínimo recorriendo todos los vértices cuesta O(V), y se hace V veces. Total: **O(V²)**.
- **Con cola de prioridad:** `poll` y `add` cuestan O(log V), y cada arista provoca como mucho una inserción. Total: **O((V + E) log V)**.

La mejora es grande en grafos ralos (pocas aristas por vértice, E ≈ V), donde queda O(V log V) contra O(V²). En grafos muy densos (E ≈ V²) la versión sin cola puede convenir más.

## Conclusión

Dijkstra permite calcular los caminos de menor costo desde un vértice de origen hacia todos los demás vértices de un grafo con pesos no negativos.

El algoritmo utiliza una estrategia voraz, seleccionando en cada iteración el vértice con menor distancia conocida desde el origen.

La utilización de una **PriorityQueue** permite obtener eficientemente el siguiente vértice que debe procesarse, evitando tener que recorrer todos los vértices para encontrar la distancia mínima.

Además, el arreglo `dist[]` permite mantener actualizadas las mejores distancias conocidas, mientras que la cola de prioridad organiza los posibles caminos pendientes.

Una particularidad de esta implementación es que pueden existir varios objetos `Nodo` asociados al mismo vértice. Cuando una anotación queda desactualizada, se descarta para evitar procesamientos innecesarios.

En cuanto a su complejidad, la implementación con cola de prioridad resulta especialmente conveniente para grafos dispersos, donde reduce significativamente el costo respecto de una implementación basada en la búsqueda lineal del mínimo.

Por lo tanto, la utilización de una cola de prioridad constituye una optimización importante para resolver eficientemente el problema de caminos mínimos.

---

# 2) Algoritmo de Kruskal con Union-Find

## Qué resuelve

Dado un grafo no dirigido, conexo y ponderado, Kruskal permite encontrar un **árbol de expansión mínima (MST)**.

Un árbol de expansión mínima conecta todos los vértices del grafo utilizando la menor suma posible de pesos, sin formar ciclos.

**Idea:** ordenar todas las aristas de menor a mayor peso e ir seleccionando las más económicas, siempre que no generen un ciclo.

Para detectar los ciclos de manera eficiente, utilizamos la estructura **Union-Find**.

## Piezas

- **`Arista(origen, destino, peso)`**: representa una conexión entre dos vértices y su costo. Implementa `Comparable` para ordenar las aristas de menor a mayor peso.
- **`UnionFind`**: estructura que permite mantener agrupados los vértices que ya están conectados.
- **`padre[]`**: arreglo que representa los conjuntos mediante árboles. Cada conjunto tiene una raíz que funciona como representante.
- **`rango[]`**: arreglo que permite optimizar las uniones, evitando generar árboles innecesariamente profundos.
- **`mst`**: lista que almacena las aristas seleccionadas para construir el árbol de expansión mínima.

## Cómo funciona Union-Find

Inicialmente, cada vértice pertenece a su propio conjunto.

Por ejemplo, para cuatro vértices:

```text
{0}, {1}, {2}, {3}
```

Cuando Kruskal selecciona una arista entre dos vértices, Union-Find une sus conjuntos.

Si seleccionamos las aristas (0,1) y (1,2), obtenemos:

```text
{0,1,2}, {3}
```

Si después consideramos la arista (0,2), debemos descartarla porque ambos vértices ya pertenecen al mismo conjunto.

Agregarla generaría un ciclo.

Union-Find utiliza dos operaciones principales:

- **`find(x)`**: encuentra la raíz del conjunto al que pertenece un vértice. Utiliza compresión de caminos para acelerar futuras búsquedas.
- **`union(x,y)`**: intenta unir los conjuntos de dos vértices. Si ya pertenecen al mismo conjunto, devuelve `false`. Si son diferentes, los une por rango y devuelve `true`.

## Pasos

1. Copiar y ordenar todas las aristas de menor a mayor peso.
2. Inicializar Union-Find, considerando inicialmente cada vértice como un conjunto independiente.
3. Crear una lista vacía para almacenar las aristas del MST.
4. Recorrer las aristas ordenadas:
   1. Comprobar mediante `union()` si sus extremos pertenecen a conjuntos diferentes.
   2. Si son diferentes, unirlos y agregar la arista al MST.
   3. Si ya están conectados, descartar la arista para evitar ciclos.
5. Terminar cuando el MST tenga V-1 aristas o cuando se hayan recorrido todas las aristas.
6. Devolver la lista de aristas seleccionadas.

## Por qué funciona

Kruskal es un algoritmo voraz (*greedy*), porque en cada paso considera la arista de menor peso disponible.

La elección es segura porque, al conectar dos componentes diferentes mediante una arista de peso mínimo entre ellas, existe un árbol de expansión mínima que incluye esa conexión.

Al descartar las aristas que generarían ciclos, el algoritmo construye un árbol que conecta todos los vértices con el menor costo total posible.

## Complejidad

Considerando V como la cantidad de vértices y E como la cantidad de aristas:

- **Copiar las aristas:** O(E).
- **Ordenar las aristas:** O(E log E).
- **Inicializar Union-Find:** O(V).
- **Operaciones de Union-Find:** O(E α(V)) en total, donde α(V) es la inversa de Ackermann, que crece extremadamente despacio.

La complejidad total es:

**O(V + E log E + E α(V))**

Para grafos conexos, se expresa habitualmente como:

**O(E log E)**

El costo principal está en ordenar las aristas.

La compresión de caminos y la unión por rango permiten que las operaciones de Union-Find sean muy eficientes.

## Conclusión

Kruskal permite construir un árbol de expansión mínima mediante una estrategia voraz, seleccionando las aristas de menor peso sin generar ciclos.

La utilización de Union-Find resulta fundamental para determinar eficientemente qué vértices ya están conectados.

Además, las optimizaciones de compresión de caminos y unión por rango permiten reducir el costo de las operaciones sobre los conjuntos.

Por lo tanto, la implementación resulta eficiente para encontrar un árbol de expansión mínima, especialmente cuando el grafo tiene relativamente pocas aristas.