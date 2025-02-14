import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Order implements Comparable<Order> {
    enum Type { BUY, SELL }
    
    private static int idCounter = 1;
    private final int orderId;
    private final String trader;
    private final String stock;
    private final Type type;
    private final int quantity;
    private final double price;
    
    public Order(String trader, String stock, Type type, int quantity, double price) {
        this.orderId = idCounter++;
        this.trader = trader;
        this.stock = stock;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }
    
    public Type getType() { return type; }
    public String getStock() { return stock; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getTrader() { return trader; }
    
    @Override
    public int compareTo(Order other) {
        return Double.compare(other.price, this.price); // Higher price priority for BUY orders
    }
    
    @Override
    public String toString() {
        return "[Order " + orderId + ": " + trader + " " + type + " " + quantity + " shares of " + stock + " @ " + price + "]";
    }
}

class StockExchange {
    private final Map<String, PriorityBlockingQueue<Order>> buyOrders = new ConcurrentHashMap<>();
    private final Map<String, PriorityBlockingQueue<Order>> sellOrders = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();

    public void placeOrder(Order order) {
        lock.lock();
        try {
            String stock = order.getStock();
            if (order.getType() == Order.Type.BUY) {
                buyOrders.putIfAbsent(stock, new PriorityBlockingQueue<>());
                buyOrders.get(stock).add(order);
            } else {
                sellOrders.putIfAbsent(stock, new PriorityBlockingQueue<>());
                sellOrders.get(stock).add(order);
            }
            System.out.println(order);
            processOrders(stock);
        } finally {
            lock.unlock();
        }
    }

    private void processOrders(String stock) {
        PriorityBlockingQueue<Order> buyQueue = buyOrders.getOrDefault(stock, new PriorityBlockingQueue<>());
        PriorityBlockingQueue<Order> sellQueue = sellOrders.getOrDefault(stock, new PriorityBlockingQueue<>());
        
        while (!buyQueue.isEmpty() && !sellQueue.isEmpty()) {
            Order buyOrder = buyQueue.peek();
            Order sellOrder = sellQueue.peek();
            
            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                int tradedQty = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                System.out.println("Trade Executed: " + tradedQty + " shares of " + stock + " @ " + sellOrder.getPrice());
                buyQueue.poll();
                sellQueue.poll();
            } else {
                break;
            }
        }
    }
}


class Trader implements Runnable {
    private final String name;
    private final StockExchange exchange;
    private final Random random = new Random();
    private final String[] stocks = {"AAPL", "GOOG", "TSLA", "AMZN"};

    public Trader(String name, StockExchange exchange) {
        this.name = name;
        this.exchange = exchange;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            String stock = stocks[random.nextInt(stocks.length)];
            Order.Type type = random.nextBoolean() ? Order.Type.BUY : Order.Type.SELL;
            int quantity = random.nextInt(10) + 1;
            double price = 100 + random.nextDouble() * 50;
            Order order = new Order(name, stock, type, quantity, price);
            exchange.placeOrder(order);
            try { Thread.sleep(random.nextInt(200)); } catch (InterruptedException ignored) {}
        }
    }
}


public class StockMarketSimulation {
    public static void main(String[] args) {
        StockExchange exchange = new StockExchange();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        for (int i = 1; i <= 5; i++) {
            executor.submit(new Trader("Trader " + i, exchange));
        }
        
        executor.shutdown();
    }
}
