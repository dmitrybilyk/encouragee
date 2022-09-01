JMS is Java Message Service and it's tightly connected to java.
AMQP - Advanced Message Queuing Protocol - protocol describes how messages should be constructed and 
delivered.
RabbitMQ - the Message Broker which implements AMQP.

Exchange - it's the 'mailbox' which received the messages and decided where to redirect them.
Types of Exchange:
- Direct Exchange - message is redirected to queue based on routing key;
- Fanout Exchange - message is sent to all queues bound to it;
- Topic Exchange - Routes message to multiple queues by matching a routing key to a pattern;
- Headers Exchange - Routes messages based on message headers;
Queues are bound to exchange using a routing key.
Messages are sent to an exchange with a routing key. The exchange then distributes copies of messages to 
the queues.

