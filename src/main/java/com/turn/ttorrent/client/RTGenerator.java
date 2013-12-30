/**
 * 
 */
package com.turn.ttorrent.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.BitSet;
import java.util.Random;

/**
 * @author Arnaud Durand
 * @author Mikael Gasparian
 * 
 */
public class RTGenerator implements Runnable {

	public static final String rtgenPath = "C:\\Users\\mg\\Desktop\\rainbowcrack-1.5-win64\\";
	private static Random rand = new Random();

	
	private SharedTorrent torrent;
	
	public RTGenerator(SharedTorrent torrent) {
		this.torrent=torrent;
		
	}
	
	public void run() {
		String hash_algorithm = torrent.getHashAlgorithm();
		String charset = torrent.getCharset();
		int plaintext_len_min = torrent.getPlaintextLenMin();
		int plaintext_len_max = torrent.getPlaintextLenMax();
		/*
		 * This is the rainbow chain length. Longer rainbow chain stores
		 * more plaintexts and requires longer time to generate.
		 */
		int chain_len = torrent.getChainLength();
		/*
		 * Number of rainbow chains to generate.
		 * Rainbow table is simply an array of rainbow chains. Size of each
		 * rainbow chain is 16 bytes.
		 */
		long chain_num = torrent.getPieceLength() / 16;
		
		BitSet unavailablePieces = torrent.getUnavailablePieces();
		
		while(!unavailablePieces.isEmpty()){
			// pick a random unavalable piece for generation
		    int n = unavailablePieces.cardinality();
		    int[] indices = new int[n];
		    // collect indices:
		    for (int i = 0, j = 0; i < n; i++) {
		        j=torrent.getUnavailablePieces().nextSetBit(j);
		        indices[i] =j++;
		    }
		    
			int pieceIndex=indices[rand.nextInt(indices.length)];
			

			/*
			 * The table_index parameter selects the reduction function.
			 * Rainbow table with different table_index parameter uses
			 * different reduction function.
			 */
			int table_index = 0;		
			
			int part_index = 1;
			
			// UnavailablePieces is updated because race conditions can
			// change.
			unavailablePieces = torrent.getUnavailablePieces();
		}

		
		try {
			Runtime rt = Runtime.getRuntime();
			// Process pr = rt.exec("cmd /c dir");
			Process pr = rt.exec("cmd /c " + rtgenPath + "rtgen.exe " + hash_algorithm
					+ " " + charset + " " + plaintext_len_min + " "
					+ plaintext_len_max + " " + table_index + " " + chain_len
					+ " " + chain_num + " " + part_index);
			// System.out.println("C:\\Users\\mg\\Desktop\\rainbowcrack-1.5-win64\\rtgen.exe  "+algo+" "+charset+" "+plaintext_len_min+" "+plaintext_len_max+" "+table_index+" "+chain_len+" "+chain_num+" "+part_index);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));

			String line = null;

			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}

			int exitVal = pr.waitFor();
			System.out.println("Exited with error code " + exitVal);

			System.out.println("Begin sorting");
			String rtname = hash_algorithm + "_" + charset + "#" + plaintext_len_min
					+ "-" + plaintext_len_max + "_" + table_index + "_"
					+ chain_len + "x" + chain_num + "_" + part_index + ".rt";

			pr = rt.exec(rtgenPath + "rtsort.exe " + rtname);
			input = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}

			System.out.println("Exited with error code " + exitVal);

		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

}
