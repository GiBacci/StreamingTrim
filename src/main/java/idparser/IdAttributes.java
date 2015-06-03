package idparser;

/**
 * Id attributes
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 *
 */
public enum IdAttributes {

	// Fastq Attributes

	/**
	 * The type of id
	 */
	ID_TYPE("id_type"),

	/**
	 * Fastq Attribute. The fastq type
	 */
	FASTQ_TYPE("fastq_type"),

	/**
	 * Fastq Attribute. Unique instrument name that sequenced this sequence
	 */
	INSTRUMENT_NAME("instrument_name"),

	/**
	 * Fastq Attribute. Flowcell lane number.
	 */
	FLOWCELL_LANE("flowcell_lane"),

	/**
	 * Fastq Attribute. X coordinate of the sequencing spot.
	 */
	X_COORD("x_coord"),

	/**
	 * Fastq Attribute. Y coordinate of the sequencing spot.
	 */
	Y_COORD("y_coord"),

	/**
	 * Fastq Attribute. Specified only in paired end sequence. This number is
	 * the identifier of the couple.
	 */
	PAIR_MEMBER("pair_member"),

	/**
	 * Fastq Attribute. Index id of the sequence.
	 */
	INDEX_ID("index_id"),

	/**
	 * Fastq Attribute. The run unique id.
	 */
	RUN_ID("run_id"),

	/**
	 * Fastq Attribute. Flowcell unique id.
	 */
	FLOWCELL_ID("flowcell_id"),

	/**
	 * Fastq Attribute. Number of tile.
	 */
	TILE_NUMBER("tile_number"),

	/**
	 * Fastq Attribute. If the sequence failed the quality test this attribute
	 * is set to <code>Y</code>; otherwise it is set to <code>N</code>.
	 */
	FAILS_FILTER("fails_filter"),

	/**
	 * Fastq Attribute. A control number.
	 */
	CONTROL_BITS("control_bits"),

	/**
	 * Fastq Attribute. The unique id given by NCBI SRA database (only for SRA
	 * sequences)
	 */
	NCBI_INDEX("ncbi_index"),
	
	/**
	 * Fastq Attribute. The unique id given by NCBI SRA database (only for SRA
	 * sequences)
	 */
	NCBI_DESCRIPTION("ncbi_descript"),

	/**
	 * Fastq Attribute. The quality offset of the fastq sequence.
	 */
	QUALITY_OFFSET("quality_offset");

	public String tag = null;

	IdAttributes(String tag) {
		this.tag = tag;
	}
}
