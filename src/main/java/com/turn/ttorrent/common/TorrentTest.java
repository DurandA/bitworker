package com.turn.ttorrent.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
			
			// Display information.
			System.out.println("\nTorrent information :");
			System.out.println("-> Info : ");
			System.out.println("\t-> length : ");
			System.out.println("\t-> Name : " + info_r.getParent());
			System.out.println("\t-> Piece Length : ");
			System.out.println("\t-> Pieces : ");
			
			System.out.println("-> Announce : " + info_r.getAnnounce().toString());
			System.out.println("-> Hash algorithm : " + info_r.getHashAlgorithm());
			System.out.println("-> Charset : " + info_r.getCharset());
			System.out.println("-> Plain text len min : " + info_r.getPlaintextLenMin());
			System.out.println("-> Plain text len max : " + info_r.getPlaintextLenMax());
			System.out.println("-> Chain length : " + info_r.getChainLen());
			System.out.println("-> Creation date : " + info_r.getCreationDate());
			System.out.println("-> Comment : " + info_r.getComment());
			System.out.println("-> Created by : " + info_r.getCreatedBy());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// ------------------------------------------------------------------
		// Create new torrent.
		// ------------------------------------------------------------------
		
		/*  Assign torrent's information.
		RTTorrent info = new RTTorrent("C:/rbtable.txt");
		
		try {
			info.setPieceLength(512);
			info.setAnnounce(new URI("udp://tracker.test.com:80"));
			info.setHashAlgorithm("md5");
			info.setCharset("loweralpha-numeric");
			info.setPlaintextLenMin(1);
			info.setPlaintextLenMax(5);
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
		}*/
	}
}