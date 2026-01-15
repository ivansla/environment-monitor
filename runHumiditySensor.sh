#!/bin/bash
i=0
while true
do
  echo "sensor_id=h1; value=50; event_id=${i}" | nc -u -w1 127.0.0.1 3355
  ((i=i+1))
  sleep 1s
done