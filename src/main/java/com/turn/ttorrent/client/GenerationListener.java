package com.turn.ttorrent.client;

import java.io.IOException;
import java.util.EventListener;

/**
 * EventListener interface for objects that want to handle rainbow tables
 * generation.
 *
 * @author Arnaud Durand
 */

public interface GenerationListener extends EventListener {
	
	public void handlePieceGenerationCompleted(Piece piece) throws IOException;

}
