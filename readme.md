Execute command to run rabbitMQ:
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management

Runnable application classes are:
WarehouseApp.java
CentralApp.java

Run runHumiditySensor.sh to simulate humidity sensor.
Run runTemperatureSensor.sh to simulate temperature sensor sensor.

In order to trigger alarm execute this command from terminal:
echo "sensor_id=h1; value=90" | nc -u -w1 127.0.0.1 3355

you can stop Warehouse service by sending:
"end" | nc -u -w1 127.0.0.1 3366