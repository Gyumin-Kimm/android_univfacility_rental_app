package facility;

public class Fac_Book {
	
	String BOOK_STATE; //진행상태
	String BOOK_TIME; //대여시간 
	String BOOK_PERSON; //대여자 
	String BOOK_REASON; //사유
	
	
	public String getBOOK_STATE() {
		return BOOK_STATE;
	}
	public void setBOOK_STATE(String bOOK_STATE) {
		BOOK_STATE = bOOK_STATE;
	}
	public String getBOOK_TIME() {
		return BOOK_TIME;
	}
	public void setBOOK_TIME(String bOOK_TIME) {
		BOOK_TIME = bOOK_TIME;
	}
	public String getBOOK_PERSON() {
		return BOOK_PERSON;
	}
	public void setBOOK_PERSON(String bOOK_PERSON) {
		BOOK_PERSON = bOOK_PERSON;
	}
	public String getBOOK_REASON() {
		return BOOK_REASON;
	}
	public void setBOOK_REASON(String bOOK_REASON) {
		BOOK_REASON = bOOK_REASON;
	}

}
