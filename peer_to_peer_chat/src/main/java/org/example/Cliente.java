package org.example;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Cliente {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORTA = 5000;
    private String nome;
    private int portaLocal;
    private ObjectOutputStream saidaServidor;

    public Cliente(String nome, int portaLocal) {
        this.nome = nome;
        this.portaLocal = portaLocal;
    }

    public void iniciar() {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORTA);
             ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            this.saidaServidor = saida;
            saida.writeObject(nome);
            saida.writeObject(portaLocal);
            saida.flush();

            new Thread(new RecebeMensagensLocal()).start();

            String conteudo;
            String destinatario;

            do {
                destinatario = JOptionPane.showInputDialog(null, "Digite o nome do destinatário (ou deixe em branco para enviar a todos):", nome, JOptionPane.QUESTION_MESSAGE);
                conteudo = JOptionPane.showInputDialog(null, "Digite a mensagem (sair para finalizar):", nome , JOptionPane.QUESTION_MESSAGE);

                if (destinatario.isEmpty()) {
                    // Envia a mensagem para todos os outros clientes
                    saida.writeObject(null); // Destinatário em branco indica que a mensagem é para todos
                    saida.flush();

                    while (true) {
                        String ip = (String) entrada.readObject(); // Recebe o IP do cliente
                        int porta = (Integer) entrada.readObject(); // Recebe a porta do cliente

                        if (ip == null) break; // Se o IP for null, significa que não há mais clientes

                        try (Socket clienteSocket = new Socket(ip, porta);
                             ObjectOutputStream saidaCliente = new ObjectOutputStream(clienteSocket.getOutputStream())) {
                            Mensagem mensagem = new Mensagem(nome, destinatario, conteudo);
                            saidaCliente.writeObject(mensagem);
                            saidaCliente.flush();
                        }
                    }
                } else {
                    saida.writeObject(destinatario);
                    saida.flush();

                    String ip = (String) entrada.readObject();
                    int porta = (Integer) entrada.readObject();

                    if (ip != null) {
                        try (Socket clienteSocket = new Socket(ip, porta);
                             ObjectOutputStream saidaCliente = new ObjectOutputStream(clienteSocket.getOutputStream())) {
                            Mensagem mensagem = new Mensagem(nome, destinatario, conteudo);
                            saidaCliente.writeObject(mensagem);
                            saidaCliente.flush();
                        }
                    }
                }
            } while (!conteudo.equalsIgnoreCase("sair"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class RecebeMensagensLocal implements Runnable {
        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(portaLocal)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    new Thread(new RecebeMensagensHandler(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class RecebeMensagensHandler implements Runnable {
        private Socket socket;

        public RecebeMensagensHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
                while (true) {
                    try {
                        Mensagem mensagem = (Mensagem) entrada.readObject();
                        System.out.println("\nNova mensagem recebida:\n" + mensagem);
                    } catch (EOFException e) {
                        System.out.println("Conexão fechada pelo servidor " + e.getMessage());
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String nome = JOptionPane.showInputDialog("Digite seu nome:");
        int portaLocal = Integer.parseInt(JOptionPane.showInputDialog("Digite a porta local para ouvir mensagens:"));
        Cliente cliente = new Cliente(nome, portaLocal);
        cliente.iniciar();
    }
}