package bluetooth;

/**
 * This class enumerates a special reply code that should be used when adding
 * something to the reply. The first number of the array is the code, which
 * should always be negative to announce a special reply. The second number is
 * the length of the extra info.
 */
public interface SpecialReplyCode {
	public int getSpecialReplyLength(byte SpecialReply);
	public static final byte ADDBARCODE=-2;
	public static final int ADDBARCODELENGHT=4;
	public  void addBarcode(int[] barcodeInfo);
}
