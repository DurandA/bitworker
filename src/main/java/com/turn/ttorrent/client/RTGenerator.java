package com.turn.ttorrent.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.turn.ttorrent.common.RTTorrentFileDescriptor;

/**
 * @author Arnaud Durand
 * 
 */
public class RTGenerator implements Runnable {

	public static final String rtgenPath = "C:\\rainbowcrack-1.5-win64\\";
	private static Random rand = new Random();

	private String hashAlgorithm;
	private String charset;
	private int plaintextLenMin;
	private int plaintextLenMax;
	/*
	 * This is the rainbow chain length. Longer rainbow chain stores more
	 * plaintexts and requires longer time to generate.
	 */
	private int chainLength;
	/*
	 * Number of rainbow chains to generate. Rainbow table is simply an array of
	 * rainbow chains. Size of each rainbow chain is 16 bytes.
	 */
	long chainNum;

	private SharedTorrent torrent;

	private Set<RTGenerationListener> listeners;
	private ExecutorService executor;
	private Thread thread;

	public RTGenerator(SharedTorrent torrent) {
		this.torrent = torrent;

		this.listeners = new HashSet<RTGenerationListener>();
		this.thread = null;

	}

	public void start() {
		if (this.thread == null || !this.thread.isAlive()) {
			this.thread = new Thread(this);
			this.thread.setName("rt-generator");
			this.thread.start();
		}
	}

	/**
	 * @author Arnaud Durand
	 * @author Mikael Gasparian
	 * 
	 */
	public Piece generatePiece(int pieceIndex) throws InterruptedException,
			IOException, Exception {
		RTTorrentFileDescriptor descriptor = null;

		long offset = 0L;
		List<? extends RTTorrentFileDescriptor> descriptors = torrent
				.getFileDescriptors();

		for (RTTorrentFileDescriptor d : descriptors) {
			if (pieceIndex * torrent.getPieceLength() < offset) {
				break;
			}
			descriptor=d;
			offset += d.getLength();
		}

		/*
		 * The table_index parameter selects the reduction function. Rainbow
		 * table with different table_index parameter uses different reduction
		 * function.
		 */
		int tableIndex = descriptor.getTableIndex();

		/*
		 * To store a large rainbow table in many smaller files, use different
		 * number in this parameter for each part and keep all other parameters
		 * identical.
		 */
		int partIndex = (int) (offset!=0 ? pieceIndex % offset : offset);

		Runtime rt = Runtime.getRuntime();
		Process pr;

		pr = rt.exec(new String[] { rtgenPath + "rtgen.exe", hashAlgorithm,
				charset, Integer.toString(plaintextLenMin),
				Integer.toString(plaintextLenMax),
				Integer.toString(tableIndex), Integer.toString(chainLength),
				Long.toString(chainNum), Integer.toString(partIndex) });
		int exitVal;
		if ((exitVal = pr.waitFor()) != 0)
			throw new Exception("rtgen exited with error code " + exitVal);
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line=null;
        while((line=input.readLine()) != null) {
            System.out.println(line);
        }
        
		/*pr = rt.exec(new String[] { rtgenPath + "rtsort.exe",
				descriptor.getPath() });
		if ((exitVal = pr.waitFor()) != 0)
			throw new Exception("rtsort exited with error code " + exitVal);
		
		while((line=input.readLine()) != null) {
            System.out.println(line);
        }*/
		
		Piece p = torrent.getPiece(pieceIndex);
		String rtFilename=hashAlgorithm+"_"+charset+"#"+plaintextLenMin+"-"+plaintextLenMax+"_"+tableIndex+"_"+chainLength+"x"+chainNum+"_"+partIndex+".rt";

		System.out.println(Paths.get(rtFilename));
		ByteBuffer generatdFileAsByteBuffer=ByteBuffer.wrap((Files.readAllBytes(Paths.get(rtFilename))));
		p.record(generatdFileAsByteBuffer, 0);

		return p;
	}

	public void run() {
		hashAlgorithm = torrent.getHashAlgorithm();
		charset = torrent.getCharset();
		plaintextLenMin = torrent.getPlaintextLenMin();
		plaintextLenMax = torrent.getPlaintextLenMax();
		chainLength = torrent.getChainLength();
		chainNum = torrent.getPieceLength() / 16;

		BitSet unavailablePieces = torrent.getUnavailablePieces();

		while (!unavailablePieces.isEmpty()) {
			// Choose a random unavailable piece for generation.
			int n = unavailablePieces.cardinality();
			int[] indices = new int[n];

			for (int i = 0, j = 0; i < n; i++) {
				j = torrent.getUnavailablePieces().nextSetBit(j);
				indices[i] = j++;
			}
			// Pick random unavailable piece.
			int pieceIndex = indices[rand.nextInt(indices.length)];

			Piece p;
			
			try {
				p=generatePiece(pieceIndex);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

			try {
				p.validate();
				this.firePieceCompleted(p);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// UnavailablePieces should be updated because race conditions can
			// change.
			unavailablePieces = torrent.getUnavailablePieces();
		}
	}

	/**
	 * Register a new incoming connection listener.
	 * 
	 * @param listener
	 *            The listener who wants to receive connection notifications.
	 */
	public void register(RTGenerationListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * @author Arnaud Durand
	 * 
	 *         Fire the piece completion event to all registered listeners.
	 * 
	 *         <p>
	 *         The event contains the piece number that was completed.
	 *         </p>
	 * 
	 * @param piece
	 *            The completed piece.
	 */
	private void firePieceCompleted(Piece piece) throws IOException {
		for (RTGenerationListener listener : this.listeners) {
			listener.handlePieceGenerationCompleted(piece);
		}
	}

}
