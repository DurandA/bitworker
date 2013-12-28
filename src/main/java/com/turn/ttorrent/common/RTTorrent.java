package com.turn.ttorrent.common;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.turn.ttorrent.bcodec.BEValue;
import com.turn.ttorrent.common.Torrent.TorrentFile;

/**
 * 
 * @author Thomas Rouvinez
 * @creationDate : 29.10.2013
 * @lastModified : 29.10.2013
 * 
 * @description : information wrapper to create a torrent file that contains information
 * 				  about rainbow tables.
 *
 */
public class RTTorrent{

	// -------------------------------------------------------------------------
	// Variables.
	// -------------------------------------------------------------------------

	/**
	 * Torrent file parent and path.
	 */
	private File parent = null;

	/**
	 * A list of dictionaries, one dictionary for each file in the torrent, which is described in the next table.
	 */
	private List<TorrentFile> files = null;

	/**
	 * The length of the file, in bytes. Should be multiple of 16 (bytes). Used to calculate chain_num. Should be 
	 * a multiple of piece length.
	 */
	@SuppressWarnings("unused")
	private long length = 0;

	/**
	 * The length of the file, in bytes. Should be multiple of 16 (bytes). Used to calculate chain_num. Should be 
	 * a multiple of piece length.
	 */
	private Date creationDate = null;

	/**
	 * A list of strings representing the relative path to the file. parent of the rainbow table should match rtgen
	 * naming scheme. For example, 536,870,912 md5_loweralpha-numeric#1-7_0_3800x33554432_0.rt
	 */
	private LinkedList<BEValue> filePath = new LinkedList<BEValue>();

	/**
	 * The table_index parameter selects the reduction function. Rainbow table with different table_index parameter
	 * uses different reduction function.
	 */
	private int tableIndex = 0;

	/**
	 * The URL of the tracker for the torrent.
	 */
	private URI announce = null;

	/**
	 * A listing of the URLs of alternate trackers for the torrent. The URLs 
	 * are divided into groups (each is a list), trackers in each group may 
	 * be shuffled, and groups are processed in the order they appear. 
	 * Optional.
	 */
	private List<List<URI>> announceList = null;

	/**
	 * Rainbow table is hash algorithm specific. Rainbow table for a certain 
	 * hash algorithm only helps to crack hashes of that type. The rtgen program 
	 * natively support lots of hash algorithms like lm, ntlm, md5, sha1, mysqlsha1, 
	 * halflmchall, ntlmchall, oracle.
	 */
	private String hashAlgorithm = null;

	/**
	 * The charset includes all possible characters for the plaintext. 
	 * "loweralpha-numeric" stands for "abcdefghijklmnopqrstuvwxyz0123456789", 
	 * which is defined in configuration file charset.txt
	 */
	private String charset = null;

	/**
	 * These two parameters limit the plaintext length range of the rainbow table.
	 */
	private int plaintextLenMin = 0;

	/**
	 * These two parameters limit the plaintext length range of the rainbow table.
	 */
	private int plaintextLenMax = 0;

	/**
	 * This is the rainbow chain length. Longer rainbow chain stores more plaintexts 
	 * and requires longer time to generate.
	 */
	private int chainLen = 0;

	/**
	 * Any user comment for the torrent. Optional.
	 */
	private String comment = null;

	/**
	 * Application-generated string that may include its parent, version, etc. Optional.
	 */
	private String createdBy = null;

	/**
	 * Piece length in kB.
	 */
	private long pieceLength = 0; 

	// -------------------------------------------------------------------------
	// Constructor.
	// -------------------------------------------------------------------------

	/**
	 * Default constructors.
	 */
	public RTTorrent(){}

	public RTTorrent(String Parent){
		setParent(Parent);
	}

	// -------------------------------------------------------------------------
	// Getter - Setters.
	// -------------------------------------------------------------------------

	public String getParent() {
		return parent.getPath();
	}

	public void setParent(String parent) {
		this.parent = new File(parent);
	}

	public List<TorrentFile> getFiles() {
		return files;
	}

	public void setFiles(List<TorrentFile> files) {
		this.files = files;
	}

	public long getLength() {
		return this.parent.length();
	}

	public LinkedList<BEValue> getFilePath() {
		return filePath;
	}

	public void setFilePath(LinkedList<BEValue> filePath) {
		this.filePath = filePath;
	}

	public int getTableIndex() {
		return tableIndex;
	}

	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}

	public URI getAnnounce() {
		return announce;
	}

	public void setAnnounce(URI uri) {
		this.announce = uri;
	}

	public List<List<URI>> getAnnounceList() {
		return announceList;
	}

	public void setAnnounceList(List<List<URI>> announceList) {
		this.announceList = announceList;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getPlaintextLenMin() {
		return plaintextLenMin;
	}

	public void setPlaintextLenMin(int plaintextLenMin) {
		this.plaintextLenMin = plaintextLenMin;
	}

	public int getPlaintextLenMax() {
		return plaintextLenMax;
	}

	public void setPlaintextLenMax(int plaintextLenMax) {
		this.plaintextLenMax = plaintextLenMax;
	}

	public int getChainLen() {
		return chainLen;
	}

	public void setChainLen(int chainLen) {
		this.chainLen = chainLen;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getPieceLength() {
		return pieceLength;
	}

	public void setPieceLength(long piece_length) {
		this.pieceLength = piece_length * 1024;
	};

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setLength(long length) {
		this.length = length;
	}
}