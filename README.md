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

# 2) Algoritmo de Kruskal