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
		//  Assign torrent's information.
		RTTorrent info = new RTTorrent("C:/rbtable.txt");
		
		// ------------------------------------------------------------------
		// Create new torrent.
		// ------------------------------------------------------------------
		
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
		}
		
		// ------------------------------------------------------------------
		// Read from new torrent.
		// ------------------------------------------------------------------
		
		try {
			// Variables.
			Path path = Paths.get("C:/rbtable.torrent");
			byte[] data = Files.readAllBytes(path);
			RTTorrent info_r = new RTTorrent();
			
			// Get back information from the torrent.
			Torrent torrent_r = new Torrent(data, true, info_r);
			
			// Display information.
			System.out.println("\nTorrent information :");
			System.out.println("-> Info : ");
			System.out.println("\t-> Name : ");
			System.out.println("\t-> Length : ");
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

		
	}
}