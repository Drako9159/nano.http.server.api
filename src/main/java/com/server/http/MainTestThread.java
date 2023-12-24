package com.server.http;

public class MainTestThread {

    public static void main(String[] args) {
        // Crear dos instancias de la clase MyThread
        MyThread thread1 = new MyThread("Hilo 1");
        MyThread thread2 = new MyThread("Hilo 2");

        // Iniciar los hilos
        thread1.start();
        thread2.start();
    }
}

class MyThread extends Thread {
    private String mensaje;

    // Constructor que recibe un mensaje
    public MyThread(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public void run() {
        // Ejecutar el código en el hilo
        for (int i = 0; i < 5; i++) {
            System.out.println(mensaje + ": Iteración " + i);
            try {
                // Hacer que el hilo espere un momento para simular un proceso
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
