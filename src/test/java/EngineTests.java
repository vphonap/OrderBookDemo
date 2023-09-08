import com.ib.core.Engine;
import com.ib.core.SimpleEngine;
import com.messages.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EngineTests {

    public Engine setupOrders()
    {
        Engine e = new SimpleEngine();
        Order m1 = new Order();
        m1.setOrderId("a1");
        m1.setSide("B");
        m1.setPrice(1.3);
        m1.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m1));
        Assertions.assertEquals(1.3,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.3,e.getBottomPriceLevel("B").getLevelPrice());
        Assertions.assertNull(e.getTopPriceLevel("S"));
        Assertions.assertNull(e.getBottomPriceLevel("S"));

        Order m2 = new Order();
        m2.setOrderId("a2");
        m2.setSide("S");
        m2.setPrice(1.4);
        m2.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m2));
        Assertions.assertEquals(1.4,e.getTopPriceLevel("S").getLevelPrice());
        Assertions.assertEquals(1.4,e.getBottomPriceLevel("S").getLevelPrice());
        Assertions.assertEquals(1.3,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.3,e.getBottomPriceLevel("B").getLevelPrice());

        Order m3 = new Order();
        m3.setOrderId("a3");
        m3.setSide("B");
        m3.setPrice(1.4);
        m3.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m3));
        Assertions.assertEquals(1.3,e.getBottomPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());

        Order m4 = new Order();
        m4.setOrderId("a4");
        m4.setSide("S");
        m4.setPrice(1.2);
        m4.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m4));
        Assertions.assertEquals(1.4,e.getBottomPriceLevel("S").getLevelPrice());
        Assertions.assertEquals(1.2,e.getTopPriceLevel("S").getLevelPrice());

        Order m5 = new Order();
        m5.setOrderId("a5");
        m5.setSide("B");
        m5.setPrice(1.4);
        m5.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m5));
        Assertions.assertEquals(1.3,e.getBottomPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());

        return e;
    }
    @Test
    public void addOrderTest()
    {
        Engine e = new SimpleEngine();
        Order m1 = new Order();
        m1.setOrderId("Y");
        m1.setSide("B");
        m1.setPrice(1.3);
        m1.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m1));
        //duplicate orderid for insertion -
        Assertions.assertFalse(e.addOrder(m1));
    }

    @Test
    public void replaceOrderIdTest()
    {
        Engine e = new SimpleEngine();
        Order m1 = new Order();
        m1.setOrderId("Y");
        m1.setSide("B");
        m1.setPrice(1.3);
        m1.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m1));

        Order m2 = new Order();
        m2.setOrderId("Y");
        m2.setSide("B");
        m2.setPrice(1.4);
        m2.setQuantity(60);
        Assertions.assertTrue(e.replaceOrder(m2));
        Assertions.assertEquals(1.4,e.getOrder("Y").getPrice());
        Assertions.assertEquals(60,e.getOrder("Y").getQuantity());
    }

    @Test
    public void orderBookCrossWithCancelTest()
    {
        Engine e = setupOrders();
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.2,e.getTopPriceLevel("S").getLevelPrice());
        Assertions.assertTrue(e.isOrderBookCrossed());
        Order m1 = new Order();
        m1.setOrderId("x1");
        m1.setSide("S");
        m1.setPrice(1.3);
        m1.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m1));
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.2,e.getTopPriceLevel("S").getLevelPrice());
        Assertions.assertTrue(e.isOrderBookCrossed());
        Assertions.assertTrue(e.removeOrder("x1"));
        Assertions.assertTrue(e.isOrderBookCrossed());
        Assertions.assertTrue(e.removeOrder("a2"));
        Assertions.assertTrue(e.isOrderBookCrossed());
        Assertions.assertTrue(e.removeOrder("a4"));
        Assertions.assertNull(e.getTopPriceLevel("S"));
        Assertions.assertNull(e.getBottomPriceLevel("S"));
        Assertions.assertFalse(e.isOrderBookCrossed());
        m1 = new Order();
        m1.setOrderId("x2");
        m1.setSide("S");
        m1.setPrice(1.6);
        m1.setQuantity(50);
        Assertions.assertTrue(e.addOrder(m1));
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.6,e.getTopPriceLevel("S").getLevelPrice());
        Assertions.assertFalse(e.isOrderBookCrossed());
        e.iterOrdersByTimeStamp();
    }

    @Test
    public void orderBookCrossWithReplaceTest()
    {
        Engine e = setupOrders();
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.2,e.getTopPriceLevel("S").getLevelPrice());
        Assertions.assertTrue(e.isOrderBookCrossed());
        Order m1 = new Order();
        m1.setOrderId("a4"); //original: sell @ 1.2
        m1.setPrice(1.6);
        Assertions.assertTrue(e.replaceOrder(m1));
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.4,e.getTopPriceLevel("S").getLevelPrice());
        Assertions.assertTrue(e.isOrderBookCrossed());
        m1.setOrderId("a2"); //original: sell @ 1.4
        m1.setPrice(1.5);
        Assertions.assertTrue(e.replaceOrder(m1));
        Assertions.assertEquals(1.4,e.getTopPriceLevel("B").getLevelPrice());
        Assertions.assertEquals(1.5,e.getTopPriceLevel("S").getLevelPrice());
        Assertions.assertFalse(e.isOrderBookCrossed());
    }

}
