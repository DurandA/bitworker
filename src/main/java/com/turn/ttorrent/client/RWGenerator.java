package com.turn.ttorrent.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.turn.ttorrent.common.TorrentFileDescriptor;

/**
 * @author Arnaud Durand
 * 
 */
public class RWGenerator implements Runnable {
	
	private static final Logger logger =
			LoggerFactory.getLogger(Client.class);
	private static Random rand = new Random();
	
	private SharedTorrent torrent;

	private Set<RWGenerationListener> listeners;
	private Thread thread;

	public RWGenerator(SharedTorrent torrent) {
		this.torrent = torrent;

		this.listeners = new HashSet<RWGenerationListener>();
		this.thread = null;
	}

	public void start() {
		if (this.thread == null || !this.thread.isAlive()) {
			this.thread = new Thread(this);
			this.thread.setName("rw-generator");
			this.thread.start();
		}
	}
	
	public Piece generatePiece(int pieceIndex) throws InterruptedException,
			IOException, Exception {
		TorrentFileDescriptor descriptor = null;
		
		long offset = 0L;
		List<? extends TorrentFileDescriptor> descriptors = torrent
				.getFileDescriptors();
		for (TorrentFileDescriptor d : descriptors) {
			if (pieceIndex * torrent.getPieceLength() < offset) {
				break;
			}
			descriptor=d;
			offset += d.getLength();
		}
		int partIndex = (int) (offset!=0 ? pieceIndex % offset : offset);
		
		ProcessBuilder pb =
				new ProcessBuilder("/bin/bash","-c", torrent.getCommand());
		Map<String, String> env = pb.environment();
		env.put("PIECE_IDX", Integer.toString(pieceIndex));
		env.put("FILE_IDX", Integer.toString(descriptor.getFileIndex()));
		env.put("PART_IDX", Integer.toString(partIndex));
		
		Process pr = pb.start();
		
		Piece p = torrent.getPiece(pieceIndex);
		logger.info("Generating {} with command {}",
				new Object[] {p, torrent.getCommand()});
		
		/*InputStream is = pr.getInputStream();
		ByteArrayOutputStream buff = new ByteArrayOutputStream();
		int buffOffset = 0;
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buff.write(data, 0, nRead);
			p.record(ByteBuffer.wrap(buff.toByteArray()), buffOffset);
			buffOffset+=data.length;
		}
		buff.flush();*/
		
		//IOUtils.copy(pr.getInputStream(), System.out);
		
		ByteBuffer generatdPieceAsByteBuffer=ByteBuffer
				.wrap(IOUtils.toByteArray(pr.getInputStream()));
		p.record(generatdPieceAsByteBuffer, 0);
		
		BufferedReader bre = new BufferedReader
				(new InputStreamReader(pr.getErrorStream()));
		String line;
		while ((line = bre.readLine()) != null) {
			System.err.println(line);
		}
		bre.close();
		
		int exitVal;
		if ((exitVal = pr.waitFor()) != 0)
			throw new Exception("command exited with error code " + exitVal);
		return p;
	}

	public void run() {
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
	public void register(RWGenerationListener listener) {
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
		for (RWGenerationListener listener : this.listeners) {
			listener.handlePieceGenerationCompleted(piece);
		}
	}

}
