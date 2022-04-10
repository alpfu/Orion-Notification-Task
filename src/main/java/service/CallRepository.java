package service;

import model.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * This interface helps us to make a query by using unique pair of (callerNum, calledNum)
 * to get all the calls between Clients
 */

@Repository
public interface CallRepository extends JpaRepository<Call, Integer> {

    // returns particular Calls between two Clients
    public List<Call> getCallByCallerNumAndCalledNum(int callerNum, int calledNum);

    // return Calls according to CalledNum Method is used while sending Notification. Assuming multiple caller.
    public List<Call> getCallByCalledNum(int calledNum);

}
