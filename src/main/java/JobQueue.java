import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A queue that ignores duplicate messages.
 *
 * When calling enqueue, the message is only added if the message
 * does not already exist within the queue.
 */
public class JobQueue {


    /**
     * @return the oldest message or if no message has arrived within 1sec returns null
     */
    public String consume() {
        return null;
    }

    public void enqueue(String message) {

    }
}
