package org.example;

public class Main {

    // Suma los folios de cada expediente desde la posición actual hasta el final
    public static int sumarFoliosRecursivo(int[] foliosPorExpediente, int indice) {
        // Si ya pasamos el último elemento, no hay nada más que sumar
        if (indice >= foliosPorExpediente.length) {
            return 0;
        }
        // Tomo los folios de este expediente y llamo a la función con el siguiente índice
        return foliosPorExpediente[indice] + sumarFoliosRecursivo(foliosPorExpediente, indice + 1);
    }

    // Imprime los códigos de los expedientes uno por uno de forma recursiva
    public static void mostrarExpedientesRecursivo(String[] codigos, int indice) {
        // Condición para detenernos cuando se acabe el arreglo
        if (indice >= codigos.length) {
            return;
        }
        System.out.println("Expediente: " + codigos[indice]);
        // Avanza al siguiente expediente
        mostrarExpedientesRecursivo(codigos, indice + 1);
    }

    public static void main(String[] args) {
        // Datos de prueba de expedientes y cantidad de folios
        String[] expedientes = {"EXP-001", "EXP-002", "EXP-003", "EXP-004"};
        int[] folios = {3, 5, 2, 4};

        System.out.println("--- RECORRIDO RECURSIVO DE EXPEDIENTES ---");
        mostrarExpedientesRecursivo(expedientes, 0);

        // Calculamos el total usando el método recursivo
        int totalFolios = sumarFoliosRecursivo(folios, 0);
        System.out.println("Total de folios calculados recursivamente: " + totalFolios);
    }
}