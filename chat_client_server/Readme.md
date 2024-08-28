# Simple Java Chat Application

This is a simple Java-based client-server chat application where clients can communicate with each other through a central server. The server keeps track of connected clients and forwards messages to the appropriate recipients.

## Features

- **Multiple Clients**: The server can handle multiple clients simultaneously.
- **Direct Messaging**: Clients can send messages directly to a specific recipient or broadcast to all connected clients.
- **Console Output**: Messages received by the clients are displayed in the console.
- **JOptionPane**: Clients use `JOptionPane` for user input.

## How It Works

### Server

The server listens on a specified port for incoming client connections. When a client connects, the server assigns a thread to handle communication with that client. The server stores information about connected clients and routes messages between them.

### Client

The client connects to the server by specifying the server's IP address and port. The user is prompted to enter their name, which is used to identify them in the chat. The client can send messages to a specific recipient or broadcast to all clients. Incoming messages are displayed in the console.

## Usage

### Running the Server

1. Compile and run the `Servidor` class to start the server.
2. The server listens on port `5000` by default.
3. The server logs client connections and disconnections.

```bash
javac org/example/Servidor.java
java org.example.Servidor
```

### Running the Client

1. Compile and run the `Cliente` class to start a client.
2. Enter the server's IP address and port (default is `5000`).
3. Enter your name to join the chat.
4. Send messages to other clients by entering their name or broadcast to all clients.
5. Type `sair` to disconnect from the server.

```bash
javac org/example/Cliente.java
java org.example.Cliente
```



