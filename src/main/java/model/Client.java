package model;

import org.springframework.beans.factory.annotation.Autowired;
import service.IClientRepository;
import service.WebSocketOperations;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.http.WebSocket;
import java.util.Objects;

/**
 * This class has been created to keep Clients those have contacted server.
 * Since num is unique for all Clients, we use it as primary key in this table.
 * Keeps Clients in H2 Database. DB operations are handled by IClientRepository
 * */

@Entity
public class Client {

    @Id
    private int num;
    private WebSocket webSocket;

    @Autowired
    private IClientRepository IClientRepository;

    public Client(int num) {
        this.num = num;
        webSocket = WebSocketOperations.getInstance().createWebSocket();

        // TODO: should not be here. would be better to have in ClientService
        IClientRepository.save(this);
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
