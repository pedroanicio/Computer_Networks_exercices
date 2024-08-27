package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Servidor {
    private static final int PORTA = 5000;
    private static final Map<String, ClienteInfo> clientes = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor iniciado na porta " + PORTA);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClienteHandler implements Runnable {
        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream())) {

                String nomeCliente = (String) entrada.readObject();
                String enderecoIP = socket.getInetAddress().getHostAddress();
                int porta = (Integer) entrada.readObject();

                clientes.put(nomeCliente, new ClienteInfo(nomeCliente, enderecoIP, porta));
                System.out.println("Cliente conectado: " + nomeCliente + " [" + enderecoIP + ":" + porta + "]");

                while (true) {
                    String destinatario = (String) entrada.readObject();
                    if (destinatario == null || destinatario.isEmpty()) {
                        for (ClienteInfo cliente : clientes.values()) {
                            if (!cliente.getNome().equals(nomeCliente)) { // Evita enviar mensagem de volta ao remetente
                                saida.writeObject(cliente.getEnderecoIP());
                                saida.writeObject(cliente.getPorta());
                                saida.flush(); // Envia endereço e porta do destinatário
                            }
                        }
                    } else {
                        ClienteInfo destinatarioInfo = clientes.get(destinatario);
                        if (destinatarioInfo != null) {
                            saida.writeObject(destinatarioInfo.getEnderecoIP());
                            saida.writeObject(destinatarioInfo.getPorta());
                            saida.flush();
                        } else {
                            saida.writeObject(null); // Destinatário não encontrado
                            saida.flush();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClienteInfo {
        private String nome;
        private String enderecoIP;
        private int porta;

        public ClienteInfo(String nome, String enderecoIP, int porta) {
            this.nome = nome;
            this.enderecoIP = enderecoIP;
            this.porta = porta;
        }

        public String getNome() {
            return nome;
        }

        public String getEnderecoIP() {
            return enderecoIP;
        }

        public int getPorta() {
            return porta;
        }
    }
}
