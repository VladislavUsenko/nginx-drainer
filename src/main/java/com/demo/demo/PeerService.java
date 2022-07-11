package com.demo.demo;

import java.util.List;

public interface PeerService {
    
    List<Peer> getAllPeers();

    Upstream getUpstream(String name);

    void downPeer(Peer peer, String upstreamName);

    void drainPeer(Peer peer, String upstreamName);

}
