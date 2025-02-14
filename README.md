Stock Market Trading Simulation

📌 Project Overview

This project is a multithreaded stock market simulation where multiple traders place buy and sell orders concurrently. The system uses priority-based order matching and ensures thread safety using Java's concurrency utilities.

🛠 Technologies Used

Java (Multithreading, Concurrency API)

PriorityBlockingQueue (For order matching)

ReentrantLock (For thread synchronization)

JUnit (For unit testing)

GitHub Actions (For CI/CD automation)

🚀 Features

✅ Supports concurrent trading by multiple users
✅ Implements order matching engine using priority queues
✅ Ensures thread safety with locks
✅ Simulates real-time stock transactions
✅ Uses unit tests to validate trade operations

🔧 How to Run the Project

Prerequisites:

Install Java (JDK 11+)

Clone the repository:

git clone https://github.com/yourusername/StockMarketSimulation.git
cd StockMarketSimulation

Compile and Run:

javac StockMarketSimulation.java
java StockMarketSimulation

📝 Example Output

User 1 bought 10 shares of AAPL at $150.5
User 2 sold 5 shares of TSLA at $700.2
Final Account Balance: $10,000

🔄 Future Enhancements

📈 Integrate MySQL to store transactions

🌐 Build a REST API using Spring Boot

⏳ Enhance performance with Kafka for event streaming

💡 Contributing

Feel free to fork this repository, make improvements, and submit a pull request!
