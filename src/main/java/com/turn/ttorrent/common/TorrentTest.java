package com.turn.ttorrent.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TorrentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//  Assign torrent's information.
		TorrentInfo info = new TorrentInfo("C:/rbtable.txt");
		
		try {
			info.setPieceLength(512);
			info.setAnnounce(new URI("udp://tracker.test.com:80"));
			info.setHashAlgorithm("md5");
			info.setCharset("loweralpha-numeric");
			info.setPlaintextLenMin(1);
			info.setPlaintextLenMax(12);
			info.setChainLen(7);
			info.setComment("Test for comments");
			info.setCreatedBy("Thomas Rouvinez");

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		// Create torrent.
		Torrent torrent = Torrent.create(info);

		// Save torrent to HDD.
		try {
			File file = new File("C:/rbtable.torrent");
			FileOutputStream fos = new FileOutputStream(file);
			torrent.save(fos);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}