package tw.pan.utils.exception;


public class DatabaseOperateException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	
	public DatabaseOperateException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
