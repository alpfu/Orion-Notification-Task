package notification.strategy;

import model.Call;
import org.springframework.beans.factory.annotation.Autowired;
import service.CallRepository;
import service.WebSocketOperations;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * This class is used to create
 *      MissingCallNotification and AvailabilityNotification
 * To create MissingCallNotification:
 *      it is stimulated by ClientListener. Right after a new client
 *      has connected, server sends MissingCallNotification for that number.
 *
 * To create AvailabilityNotification:
 *      system waits for MissingCallNotification. If there is any call to new
 *      client, then server sends AvailabilityNotification to those called
 *      new client in the past.
 */

public class NotificationCreator {

    @Autowired
    private CallRepository callRepository;
    @Autowired
    private WebSocketOperations webSocketOperations;

    private AbstractNotification abstractNotification;

    private int numNewClient;
    private Hashtable<Integer, Integer> callerNumCountTable;
    private Hashtable<Integer, Date> callerNumDateTable;

    public NotificationCreator(int numNewClient) {

        this.numNewClient = numNewClient;
        callerNumCountTable = new Hashtable<Integer, Integer>();
        callerNumDateTable = new Hashtable<Integer, Date>();
    }

    public boolean createNotification() {

        boolean bIsCreated = true;

        List<Call> callToNewClientList =
                callRepository.getCallByCalledNum(numNewClient);

        if(callToNewClientList.isEmpty()) {
            bIsCreated = false;
        } else {

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
            webSocketOperations.sendNotificationOverWebSocket(keyNum, notificationMessage);
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
            webSocketOperations.sendNotificationOverWebSocket(keyNum, notificationMessage);
            bIsCreated = true; // could be false. answer from WebSocket
        }

        return bIsCreated;
    }

    /**
     * counts call made by same caller.
     * first param of Hashtable: CallerNum
     * second param of Hashtable: count of call
     * */
    private void getCallCountByCaller(List<Call> callToNewClientList) {

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
    private void getLastCallDateByCaller(List<Call> callToNewClientList) {

        for (Call call: callToNewClientList) {
            int callerNum = call.getCallerNum();
            Date timestamp = call.getTimestamp();

            if(!callerNumDateTable.containsKey(callerNum)) {
                callerNumDateTable.put(callerNum, timestamp);
            } else {
                Date newTimestamp = callerNumDateTable.get(callerNum);

                if(newTimestamp.after(timestamp)) {
                    callerNumDateTable.put(callerNum, newTimestamp);
                }
            }
        }

    }

}
