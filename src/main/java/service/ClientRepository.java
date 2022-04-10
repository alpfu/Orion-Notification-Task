package service;

import model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface helps us to get Client by its unique number.
 * */

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    public Client findByNum(int num);
}
