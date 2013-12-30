package com.turn.ttorrent.common;

public interface RTTorrentFileDescriptor {
	public long getLength();
	public String getPath();
	public int getTableIndex();
}
