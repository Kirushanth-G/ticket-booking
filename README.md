### Comparison of different Locking mechanisms in Distributed Systems to avoid High concurrency issues
In distributed systems, managing concurrent access to shared resources is crucial to ensure data consistency and integrity. Various locking mechanisms have been developed to address high concurrency issues. This project comapares different locking mechanisms used in distributed systems.

Locking in local systems is often straightforward, but in distributed systems, it becomes more complex due to the involvement of multiple nodes that may not have a shared database.
#### Locking Mechanisms Compared:
1. **Pessimistic Locking(Database Lock)**:
   - Description: In pessimistic locking, a resource is locked for exclusive access by a single process until it is released. Other processes must wait until the lock is released before they can access the resource. Done in databases using "SELECT ... FOR UPDATE" statements.
   - Use Cases: Suitable for scenarios with high contention where conflicts are likely to occur.
   - Pros: Prevents conflicts and ensures data integrity. No double booking issues.
   - Cons: Relatively slow since all the process is going on with database level locks, can lead to deadlocks.
2. **Optimistic Locking(Version control)**:
   - Description: In optimistic locking, multiple processes can access the resource simultaneously. Before committing changes, each process checks if the resource has been modified by another process. If a conflict is detected, the process must retry or abort.
   - Use Cases: Suitable for scenarios with low contention where conflicts are rare.
   - Pros: Higher concurrency and performance since locks are not held for long periods.
   - Cons: Requires conflict detection and resolution mechanisms, which can add complexity. Also lot of retries can lead to performance degradation.
3. **Distributed Locking with Redis(Redission)**:
    - Description: Redis can be used as a distributed locking mechanism using libraries like Redisson. It allows multiple nodes to acquire and release locks on shared resources using Redis' atomic operations.
    - Use Cases: Suitable for distributed systems where multiple nodes need to coordinate access to shared resources.
    - Pros: Fast and scalable, leverages Redis' in-memory capabilities.
    - Cons: Requires a running Redis instance, potential single point of failure if not configured with high availability.
     
#### Comparison Table:
| Feature            | Pessimistic Lock | Optimistic Lock             | Distributed Lock (Redis)     |
|--------------------|------------------|-----------------------------|------------------------------|
| Lock Location      | Database Disk    | Application Logic           | Redis Memory                 |
| Performance        | Low (Blocking)   | High (Non-blocking)         | Very High (In-Memory)        |
| High Load Behavior | DB Bottleneck    | Retry Storm (High Failures) | Efficient Queuing            |
| Deadlock Risk      | High             | None                        | Handled via Timeouts \(TTL\) |
