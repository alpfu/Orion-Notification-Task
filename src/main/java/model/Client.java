package model;

import org.springframework.beans.factory.annotation.Autowired;
import service.ClientRepository;
import service.WebSocketOperations;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.http.WebSocket;
import java.util.Objects;

/**
 * This class has been created to keep Clients those have contacted server.
 * */

@Entity
public class Client {

    @Id
    private int num;
    private WebSocket webSocket;

    @Autowired
    private ClientRepository clientRepository;

    private WebSocketOperations webSocketOperations = WebSocketOperations.getInstance();

    public Client(int num) {
        this.num = num;
        webSocket = webSocketOperations.createWebSocket();

        // TODO: should not be here. would be better to have ClientService
        clientRepository.save(this);
    }

    public int getNum() {
        return num;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return getNum() == client.getNum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNum());
    }
}
