import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: emil
 * Date: 2013-08-30
 * Time: 18:59
 */
public class JobQueueTest {

    JobQueue q;

    @Before
    public void init() {
        q = new JobQueue();
    }

    @Test
    public void testConsumeReturnsNullIfQueueEmpty() {
        long now = System.currentTimeMillis();
        Assert.assertEquals(null, q.consume());

        Assert.assertTrue("Didn't wait for 1 sec before returning null", System.currentTimeMillis() - now > 998);
    }

    @Test
    public void testThatOldestMessageIsConsumedFirst() {
        q.enqueue("oldMessage");
        q.enqueue("newMessage");
        Assert.assertEquals("oldMessage", q.consume());
        Assert.assertEquals("newMessage", q.consume());
  }

    @Test
    public void testThatDuplicateMessagesAreIgnored() {
        q.enqueue("a message");
        q.enqueue("a message");
        Assert.assertEquals("a message", q.consume());
        Assert.assertEquals(null, q.consume());
    }

    @Test
    public void testThatAMessageCanBeEnqueuedAgain() {
        q.enqueue("a message");
        Assert.assertEquals("a message", q.consume());
        q.enqueue("a message");
        Assert.assertEquals("a message", q.consume());
    }



    @Test
    public void testConsumeSimple() {
        String aMessage = "testConsumeSimple";
        q.enqueue(aMessage);
        Assert.assertEquals("Wrong message returned", aMessage, q.consume());
    }


    @Test
    public void testConsumeBeforeEnqueue() throws Exception {
        final String aMessage = "hello";

        ExecutorService tp = Executors.newFixedThreadPool(2);
        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                String message = q.consume();
                Assert.assertEquals("Wrong message returned", aMessage, message);
            }
        };

        Runnable publisher = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                q.enqueue(aMessage);
            }
        };

        tp.execute(consumer);
        tp.execute(publisher);

        tp.shutdown();
        Assert.assertTrue("Shutdown complete", tp.awaitTermination(900, TimeUnit.MILLISECONDS));
    }
}
