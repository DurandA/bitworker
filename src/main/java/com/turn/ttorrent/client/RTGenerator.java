/**
 * 
 */
package com.turn.ttorrent.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Mikael Gasparian
 * @author Arnaud Durand
 * 
 */
public class RTGenerator implements Runnable {

	public static final String rtgenPath = "C:\\Users\\mg\\Desktop\\rainbowcrack-1.5-win64\\";

	String hash_algorithm = "md5";
	String charset = "numeric";
	int plaintext_len_min = 1;
	int plaintext_len_max = 7;

	/*
	 * The table_index parameter selects the reduction function.
	 * Rainbow table with different table_index parameter uses
	 * different reduction function.
	 */
	int table_index = 0;

	/*
	 * This is the rainbow chain length. Longer rainbow chain stores
	 * more plaintexts and requires longer time to generate.
	 */
	int chain_len = 2400;

	// piece length by 16
	int chain_num = 1048576 / 16;
	int part_index = 1;
	
	private SharedTorrent torrent;
	
	public RTGenerator(SharedTorrent torrent) {
		this.torrent=torrent;
	}
	
	public void run() {
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
