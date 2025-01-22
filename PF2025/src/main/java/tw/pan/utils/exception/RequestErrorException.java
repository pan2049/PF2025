package tw.pan.utils.exception;


public class RequestErrorException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	
	public RequestErrorException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
