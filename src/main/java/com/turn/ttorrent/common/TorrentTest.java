package com.turn.ttorrent.common;

import java.io.IOException;

public class TorrentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// ------------------------------------------------------------------
		// Read from new torrent.
		// ------------------------------------------------------------------
		
		try {			
			// Get back information from the torrent.
			RTTorrent info_r = new RTTorrent();
			Torrent torrent_r = new Torrent(info_r, true, "C:/rbtable.torrent");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}