#mycos
Mycos is simple to use messaging library for code to code communications. With mycos you pass plain old Java objects (POJOs) between components. It supports different core interaction models, namely client-server-model, observer-model and pubsub-model are supported. It also supports local and remote communication.

Mycos is built on top of ZeroMQ high performance library (actually it's java port JeroMQ) and GSON object-json parser. It hides away resource management issues common in network programming.
###simple client-server example
Maybe simple example is the most effective way to describe mycos. You can create new socket (for example Client-socket) quite easily with immutable SocketBuilder. Following lines builds a new client that is connected to server at localhost:8000. The server does not have to be in place for this to succeed. Actually you can even send objects to the server without the actual server in place.

        final Client client = 
        SocketBuilder
        .buildSocket()
        .asClientOf("localhost:8000");
Creating server is equally simple:

        final Server server = 
        SocketBuilder
        .buildSocket()
        .asServerAt("localhost:8000");
Client can send requests to server and receives Wait, that is sort of limited Java Future. This allows the client keep on working while server processes request. The second parameter (String.class) implies what kind of result is expected from the server. Client can use method askAndWait that blocks until reply is received, if synchronous communication is preferred.

        Wait<String> waitReply = client.ask("REQUEST", String.class);

For server you implement the serving function. It takes the request as input parameter. You need to also spesify the end-function. This function returns simple boolean, where true indicates continue and false indicates that serving should be ended.

        server.onRequest(TestObject.class, 
                (Optional<TestObject> v) ->
                {
                        assertEquals(REQUEST, v.get().getData());
                        return new TestObject(RESPOND);
                }, 
                () -> true);

Client can read the reply from the previously created Wait-object:

        Optional<String> reply = waitReply.get();
There is nothing special about Strings. The object on the wire can be any POJO. For example

        Wait<String> waitReply = client.ask(new Person(name, age), Person.class);
Works just like the example with strings. The object on the wire can not contain generic fields though!

More examples can be found from the test libraries src/test/java/mycos/systemtests/
####use cases
Mycos could be useful, if you need fast and easy setup. For example when building web services in trusted network you may end up having web server(1), object to xml/json parser (2), web service client (3) and web service controller (4).  You could setup similar environment with just mycos. On the other hand, when your application grows you might prefer more complex and versatile setup.
####software engineering
Mycos is written in Java 8. The preferred build and dependency tool is Maven 3. Formatter for eclipse, preferred sonarqube rules are provided in the root folder of the project.
####limitations
The object on the wire can not contain generic fields. This is because the object parser (gson) do not know how to parse generics without the user giving information about the generic types. This is because of Java's type erasure.

This is work in progress so the limitation list is quite extensive..
