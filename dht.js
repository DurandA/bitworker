var DHT    = require('bittorrent-dht')

var dht = new DHT()

var DHTManager = Java.type('com.turn.ttorrent.client.DHTManager')

var minutes = 5, announce_interval = minutes * 60 * 1000;
var info_hash = DHTManager.getHash()
var port = DHTManager.getPort()

dht.listen(20000, function () {
  console.log('now listening')
})

dht.on('ready', function () {
  // DHT is ready to use (i.e. the routing table contains at least K nodes, discovered
  // via the bootstrap nodes)

  // find peers for the given torrent info hash
  dht.lookup(info_hash, setInterval(function() {
    console.log("announce")
    dht.announce(info_hash, port)
  }, announce_interval))
})

dht.on('peer', function (addr, hash, from) {
  console.log('found potential peer ' + addr + ' through ' + from)
  DHTManager.lookupCallback(addr)
})

