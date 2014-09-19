package com.turn.ttorrent.common;

public interface TorrentFileDescriptor {
	public long getLength();
	public String getPath();
	public int getFileIndex();
}
