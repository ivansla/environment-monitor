#!/bin/bash
i=0
while true
do
  echo "sensor_id=t1; value=30; event_id=${i}" | nc -u -w1 127.0.0.1 3344
  ((i=i+1))
  sleep 1s
done