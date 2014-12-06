

##General
Mycos is trying to be dead simple and easy to use networking library. It provides internal DSL for creating clients and socket. It is built on top of ZeroMQ high-performance networking library. Using ZeroMQ can be little bit tedious at times. Many internal services are happy to work with basic settings networking. Many libraries make it very clear that you are using I/O-resources. Mycos hides away this complexity with simple internal DSL. 

###About NetworkContextStateManager
NetworkContextStateManager is the core component of the library. It manages the creation of ZeroMQ sockets and ZeroMQ context. It creates new context when needed and destroys if there is not live sockets left. Optimising it's performance is little bit tricky and leaves quickly to hard to maintain code, since there is so many checks to make and almost every operation might throw exception.

NetworkContextStateManager provides following functionality: 

-It creates new sockets. If the requested socket is first, new context has to be created. There can be only one context active at any time. The new context has to be destroyed, if it was created, but the socket creation fails (since no live sockets).

-The context has to destroyed when the last live socket is released.

-Socket creation and releasing happens in parallel. 

This makes it somewhat difficult to manage the state of the context. For example when destroying context, client can request new socket, meaning that new context has to be created. Context state is somewhat easy to manage, by synchronizing access to socket creation and destroying methods. Synchronized blocks does not really limit the scope of the synchronization. There is still room for improvement here. Two main problems with synchronization are:

-new sockets are requested while we are closing the context
-socket queue the lock, while the locking is only needed for context creation. After the ctx is up it is ok to create sockets in parallel. ReadWriteLock does not help with this. CountdownLatch seems to be right, but it's state can not be reseted. Best guess is to go with mutable CountdownLatch, Semaphore or Phaser. Actually using Semaphore gives about 6 times better new socket creation time and almost halved the socket releasing time.   

###About exceptions
Mycos throws tree exception types that are NetworkException, ParseException and UnknownException. Are exception types are derived from RuntimeException, because the nature of exceptions is such that the client is probably better of by crashing.

NetworkException wraps more specific exception type that is thrown from ZeroMQ. ZeroMq does not document RuntimeExceptions it can throw. One has to go down the call hierarchy to track possible exception types. It is possible that some exception types can leak to user from ZeroMQ, because of former.

ParseException wraps Gson Json parser thrown exceptions.

UnknownException basically means a bug in the library.  

###Building Clients and Servers
Clients and Servers are builded via SocketBuilder class. SocketBuilder class can be created only via  static factory method BuildSocket(). Static factory methods have names so they makes the creation of clients and sockets read nicer. 

###About Optional
Return values are Java's Optional<T> where empty return value is possible.

###About Wait
Wait wraps Java's Future. It also hides away thread cancelling and offers only limited view of Java Future. Cancelling is omitted, because issues related to timeouts should be handled by sockets.
