# Example showing a leak in hhu-bsinfo/hadroNIO
A leak occurs when many sockets are created.
It is related to the specific behaviour of JavaNIO DirectBuffer.
