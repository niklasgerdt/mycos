

##General
Mycos is trying to be dead simple and easy to use networking library. It provides internal DSL for creating clients and socket. It is built on top of ZeroMQ high-performance networking library. Using ZeroMQ can be little bit tedious at times. Many internal services are happy to work with basic settings networking. Many libraries make it very clear that you are using I/O-resources. Mycos hides away this complexity with simple internal DSL. 

###Building Clients and Servers
Clients and Servers are builded via SocketBuilder class. SocketBuilder class can be created only via  static factory method BuildSocket(). Static factory methods have names so they makes the creation of clients and sockets read nicer. More about static factory methods and their benefits can be found in [1]. Of course it is possible to create clients and servers by multiple different mechanisms.  

###About exceptions
Mycos doesn't throw exceptions. Return values are Java's Optionals (or booleans) where empty return value (or false) means that something went wrong. Error conditions are logged, but not passed to user. We could also use some other structure than Optionals to capture the information about the exceptions, but chose not, because there isn't probably lot to do for the error condition at runtime. All this puts some pressure on decent logging, because users of the API get specific information about the error only via logs.

###Later
Later wraps Java's Future and converts possible exceptions to Mycos exceptions. It also hides away thread cancelling and offers only limited view of Java Future. Cancelling is omitted, because issues related to timeouts should be handled when creating sockets.

[1] Effective Java 2. edition by Joshua Bloch.