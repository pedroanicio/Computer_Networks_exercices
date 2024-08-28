# Simple Peer-to-Peer Messaging System

This project is a simple peer-to-peer (P2P) messaging system implemented in Java. The system consists of a server and multiple clients. The server acts as a mediator, connecting clients and relaying information about their addresses, but it does not store or directly manage the messages themselves.

## Features

- **Client-to-Client Messaging**: Clients can send messages directly to each other without the server storing the message content.
- **Broadcast Messaging**: Clients can send messages to all connected clients by leaving the recipient field empty.
- **Dynamic Client Management**: The server dynamically manages connected clients, providing their IP addresses and ports to other clients upon request.
- **Message Logging**: Messages received by clients are displayed in the console.

## Project Structure

- **`Cliente`**: This class represents the client that connects to the server and communicates with other clients.
- **`Servidor`**: This class represents the server that handles incoming client connections and provides their addresses to other clients.
- **`Mensagem`**: A serializable class that represents the message object exchanged between clients.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- A network environment where clients can connect to the server

### Running the Server

To start the server, run the `Servidor` class. The server listens on port `5000` for incoming client connections.

```bash
javac org/example/Servidor.java
java org.example.Servidor
```

### Running the Client

To start a client, run the `Cliente` class. The client connects to the server and sends messages to other clients.

```bash
javac org/example/Cliente.java
java org.example.Cliente
```

### Sending Messages

1. Direct Message: Enter the recipient's name and your message. The message will be sent directly to that client.
2. Broadcast Message: Leave the recipient's name blank and enter your message. The message will be sent to all connected clients.

### Exiting

To exit the client or server, type `sair` and press `Enter`.