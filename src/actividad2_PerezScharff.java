
public class actividad2_PerezScharff {

    // Clase para representar las ubicaciones del campus
    static class Ubicacion {
        String nombre;
        int distancia;

        Ubicacion(String nombre, int distancia) {
            this.nombre = nombre;
            this.distancia = distancia;
        }
    }

    // Ordenamiento recursivo Merge Sort
    static void mergeSort(Ubicacion[] datos, int inicio, int fin) {

        // Caso base
        if (inicio >= fin) {
            return;
        }

        // Dividir el arreglo en dos mitades
        int medio = inicio + (fin - inicio) / 2;

        // Llamadas recursivas
        mergeSort(datos, inicio, medio);
        mergeSort(datos, medio + 1, fin);

        // Combinar las mitades ordenadas
        mezclar(datos, inicio, medio, fin);
    }

    // Metodo para combinar las mitades
    static void mezclar(Ubicacion[] datos, int inicio,
                        int medio, int fin) {

        Ubicacion[] auxiliar = new Ubicacion[fin - inicio + 1];

        int i = inicio;
        int j = medio + 1;
        int k = 0;

        // Comparar las distancias
        while (i <= medio && j <= fin) {

            if (datos[i].distancia <= datos[j].distancia) {
                auxiliar[k] = datos[i];
                i++;
            } else {
                auxiliar[k] = datos[j];
                j++;
            }

            k++;
        }

        // Copiar elementos restantes de la izquierda
        while (i <= medio) {
            auxiliar[k] = datos[i];
            i++;
            k++;
        }

        // Copiar elementos restantes de la derecha
        while (j <= fin) {
            auxiliar[k] = datos[j];
            j++;
            k++;
        }

        // Actualizar el arreglo original
        for (int x = 0; x < auxiliar.length; x++) {
            datos[inicio + x] = auxiliar[x];
        }
    }

    // Mostrar las ubicaciones del campus
    static void mostrar(Ubicacion[] datos) {

        for (int i = 0; i < datos.length; i++) {
            System.out.println(datos[i].nombre
                    + " - " + datos[i].distancia + " m");
        }
    }

    public static void main(String[] args) {

        Ubicacion[] campus = {
                new Ubicacion("Biblioteca", 350),
                new Ubicacion("Cafeteria", 120),
                new Ubicacion("Laboratorio", 280),
                new Ubicacion("Auditorio", 450),
                new Ubicacion("Administracion", 200),
                new Ubicacion("Entrada principal", 80)
        };

        System.out.println("ACTIVIDAD 2 - MERGE SORT");

        System.out.println("\nUBICACIONES SIN ORDENAR:");
        mostrar(campus);

        // Ejecutar el ordenamiento recursivo
        mergeSort(campus, 0, campus.length - 1);

        System.out.println("\nUBICACIONES ORDENADAS:");
        mostrar(campus);

        System.out.println("\nUBICACION MAS CERCANA:");
        System.out.println(campus[0].nombre
                + " - " + campus[0].distancia + " m");
    }
}
