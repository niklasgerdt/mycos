

##General
Mycos is trying to be dead simple and easy to use networking library. It provides internal DSL for creating clients and socket. It is built on top of ZeroMQ high-performance networking library. Using ZeroMQ can be little bit tedious at times. Many internal services are happy to work with basic settings networking. Many libraries make it very clear that you are using I/O-resources. Mycos hides away this complexity with simple internal DSL. 

###Building Clients and Servers
Clients and Servers are builded via SocketBuilder class. SocketBuilder class can be created only via  static factory method BuildSocket(). Static factory methods have names so they makes the creation of clients and sockets read nicer. More about static factory methods and their benefits can be found in [1]. Of course it is possible to create clients and servers by multiple different mechanisms.  

###About exceptions
Mycos does throw one exception type, namely MycosNetworkException. This class wraps more specific exception type that is either exception  from ZeroMQ or Gson Json parser. ZeroMq does not document RuntimeExceptions it can throw. One has to go down the call hierarchy to track possible exception types. It is possible that some exception types can leak to user from ZeroMQ, because of former.

###About Optionals
Return values are Java's Optional<T> where empty return value is possible.

###Wait
Wait wraps Java's Future and converts possible exceptions to Mycos exceptions. It also hides away thread cancelling and offers only limited view of Java Future. Cancelling is omitted, because issues related to timeouts should be handled with sockets.

[1] Effective Java 2. edition by Joshua Bloch.