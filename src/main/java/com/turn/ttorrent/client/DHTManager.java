package com.turn.ttorrent.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.script.ScriptEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import com.oracle.avatar.js.Loader;
import com.oracle.avatar.js.Server;
import com.oracle.avatar.js.eventloop.ThreadPool;
import com.oracle.avatar.js.log.Logging;
import com.turn.ttorrent.client.announce.AnnounceResponseListener;
import com.turn.ttorrent.client.announce.TrackerClient;
import com.turn.ttorrent.common.Peer;

/**
 * @author Thomas Rouvinez
 * @author Arnaud Durand
 * 
 */
public class DHTManager implements Runnable{
	
	private static final Logger logger =
			LoggerFactory.getLogger(ConnectionHandler.class);
	
	private Thread thread;
	
	private static ScriptEngine se;
	final String[] options = new String[] {
			"-scripting", // shebangs in modules
			"--const-as-var" // until const is fully
	};

	private SharedTorrent torrent;
	private static Set<AnnounceResponseListener> listeners=new HashSet<AnnounceResponseListener>();
	
	private static Server server;
	private static String infoHash;
	private static String dhtLibraryPath = "dht.js";

	private static final NashornScriptEngineFactory ENGINE_FACTORY = new NashornScriptEngineFactory();

	/**
	 * Constructor for DHT Manager
	 * @param client Bittorrent client.
	 */
	public DHTManager(SharedTorrent torrent){
		this.torrent = torrent;
		infoHash = torrent.getHexInfoHash();
		
		// Initialize NashHorn.
		initializeNashHorn();

		// Fetch the DHT script.
		dhtLibraryPath = new File(dhtLibraryPath).getAbsolutePath();	
	}

	/**
	 * Method to initialize all requirements for Nashorn.
	 */
	private void initializeNashHorn(){

		try {
			se = ENGINE_FACTORY.getScriptEngine(options);
		} catch (IllegalArgumentException iae) {
			se = ENGINE_FACTORY.getScriptEngine();
		}

		Loader loader = new Loader.Core();

		try {
			server = new Server(se, loader, new Logging(false), System.getProperty("user.dir"), se.getContext(), 0, ThreadPool.newInstance(), null, null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter for the Info Hash of the torrent.
	 * @return This torrent Info Hash.
	 */
	public static String getHash(){
		logger.info("Info Hash requested from torrent.");
		return infoHash;
	}

	/**
	 * Receive the DHT Peers data and add them to the Peer list.
	 * @param result Result of the DHT lookup in Javascript (peer address).
	 */
	public static void lookupCallback(Object result){
		// Process the new peer.
		String[] splitArray = new String[2];
		String peer = (String) result;

		splitArray[0] = peer.substring(0, peer.indexOf(":"));
		splitArray[1] = peer.substring(peer.indexOf(":")+1, peer.length());
		
		Peer dhtPeer = new Peer(splitArray[0], Integer.parseInt(splitArray[1]));

		for (AnnounceResponseListener listener : listeners)
			listener.handleDiscoveredPeers(new ArrayList<Peer>(Collections.singletonList(dhtPeer)));
	}
	
	/**
	 * Register a new announce response listener.
	 *
	 * @param listener The listener to register on this announcer events.
	 */
	public void register(AnnounceResponseListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Start the dhtClient request thread.
	 */
	public void start() {
		if (this.thread == null || !this.thread.isAlive()) {
			this.thread = new Thread(this);
			this.thread.setDaemon(true);
			this.thread.setName("bt-dht-client");
			this.thread.start();
		}
	}

	/**
	 * Stop the dhtClient thread.
	 */
	public void stop() {
		if (this.thread != null && this.thread.isAlive()){
			this.thread.interrupt();
		}
		this.thread = null;
	}

	@Override
	public void run() {
		try {
			// Launch the DHT.
			server.run(dhtLibraryPath);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}
}