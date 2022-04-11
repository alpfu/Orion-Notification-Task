package notification.strategy;

import com.sun.istack.NotNull;
import model.Call;
import org.springframework.beans.factory.annotation.Autowired;
import service.ICallRepository;
import service.WebSocketOperations;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * This class is used to create
 *      MissingCallNotification and AvailabilityNotification
 * To create MissingCallNotification:
 *      it is stimulated by ClientListener. Right after a new client(numNewClient)
 *      has connected, server sends MissingCallNotification for that number.
 *
 * To create AvailabilityNotification:
 *      If there is any call to new
 *      client, then server sends AvailabilityNotification to those who
 *      called new client in the past.
 */

public class NotificationCreator {

    @Autowired
    private ICallRepository ICallRepository;

    private AbstractNotification abstractNotification;

    private int numNewClient;
    private Hashtable<Integer, Integer> callerNumCountTable;
    private Hashtable<Integer, Date> callerNumDateTable;

    public NotificationCreator(int numNewClient) {

        this.numNewClient = numNewClient;
        callerNumCountTable = new Hashtable<Integer, Integer>();
        callerNumDateTable = new Hashtable<Integer, Date>();
    }

    // this method is stimulated from ClientListener to create multiple
    // notifications when new Client is connected
    public boolean createNotification() {

        boolean bIsCreated = true;

        List<Call> callToNewClientList =
                ICallRepository.getCallByCalledNum(numNewClient);

        if(callToNewClientList.isEmpty()) {
            bIsCreated = false;
        } else {

            // fills the list which keeps MissingCalls to newly joined Client
            fillCallCountByCaller(callToNewClientList);
            //
            fillLastCallDateByCaller(callToNewClientList);

            bIsCreated =
                    createMissingCallNotification() &&
                    createAvailabilityNotification(numNewClient);
        }

        return bIsCreated;
    }

    private boolean createMissingCallNotification() {

        boolean bIsCreated = true;

        Enumeration<Integer> numIterator = callerNumCountTable.keys();

        while (numIterator.hasMoreElements()) {
            int keyNum = numIterator.nextElement();
            abstractNotification =
                    new MissingCallNotification(
                            "TR",
                            keyNum,
                            callerNumDateTable.get(keyNum),
                            callerNumCountTable.get(keyNum));

            String notificationMessage = abstractNotification.createNotificationMessage();
            WebSocketOperations.getInstance().
                    sendNotificationOverWebSocket(keyNum, notificationMessage);
            bIsCreated = true; // could be false. answer from WebSocket
        }

        return bIsCreated;
    }

    private boolean createAvailabilityNotification(int numNewClient) {

        boolean bIsCreated = true;

        Enumeration<Integer> numIterator = callerNumDateTable.keys();

        while(numIterator.hasMoreElements()) {
            int keyNum = numIterator.nextElement();
            abstractNotification =
                    new AvailabilityNotification(
                            "ENG",
                            numNewClient,
                            callerNumDateTable.get(keyNum));

            String notificationMessage = abstractNotification.createNotificationMessage();
            WebSocketOperations.getInstance().
                    sendNotificationOverWebSocket(keyNum, notificationMessage);
            bIsCreated = true; // could be false. answer from WebSocket
        }

        return bIsCreated;
    }

    /**
     * counts call made by same caller.
     * first param of Hashtable: CallerNum
     * second param of Hashtable: count of call
     * */
    private void fillCallCountByCaller(@NotNull List<Call> callToNewClientList) {

        for (Call call : callToNewClientList) {
            int callerNum = call.getCallerNum();

            if(!callerNumCountTable.contains(callerNum)) {
                callerNumCountTable.put(callerNum, 1);
            } else {
                int callerNumCount = callerNumCountTable.get(call.getCallerNum());
                callerNumCountTable.put(callerNum, ++callerNumCount);
            }
        }

    }

    /**
     * gets the timestamp of last call made by particular caller
     * first param of Hashtable: CallerNum
     * second param of Hashtable: count of call
     *
     * fills callerNumDateTable by Caller and its last call
     * */
    private void fillLastCallDateByCaller(@NotNull List<Call> callToNewClientList) {

        // iterate over Calls to find latest call for each Caller
        for (Call call: callToNewClientList) {
            int callerNum = call.getCallerNum();
            Date timestamp = call.getTimestamp();

            if(!callerNumDateTable.contains(callerNum)) {
                callerNumDateTable.put(callerNum, timestamp);
            } else {
                // timestamp already in the table for callerNum
                Date currentTimestamp = callerNumDateTable.get(callerNum);

                if(timestamp.after(currentTimestamp)) {
                    callerNumDateTable.put(callerNum, timestamp);
                }
            }
        }

    }

}
