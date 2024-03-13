README

#### compile: 
- cd into the source folder, and run `javac handlers/*.java Peer.java`


#### run: 
- run peer as `java Peer <host> <port>` . For example `java Peer 127.0.0.1 9999`

If the peer is the first instance, it will run as the server. 
Subsequent peers that connect to this port will run as peers
