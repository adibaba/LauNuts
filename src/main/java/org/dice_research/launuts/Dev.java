package org.dice_research.launuts;

/**
 * LauNuts - Development options.
 *
 * @author Adrian Wilke
 */
public abstract class Dev {

	// Default: false.
	public static final boolean DEV = false;

	// Development mode to set arguments inside code.
	public static final String[] DEV_ARGS_SINGLE = new String[] { "-ids", "lau2018-nuts2016", "csv" };
	public static final String[] DEV_ARGS_ALL_DL = new String[] { "dl" };
	public static final String[] DEV_ARGS_ALL_CSV = new String[] { "csv" };
	public static final String[] DEV_ARGS = DEV_ARGS_ALL_CSV;

	// Default: false. Set true to only move already created CSV files.
	public static final boolean DEV_SKIP_SHEET_CREATION = false;

}