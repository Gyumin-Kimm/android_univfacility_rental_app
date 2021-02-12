package facility;

//<나의 신청 내역 조회> 리스트 항목에 나타낼 데이터 
public class My_FacBook {
	String FAC_MAIN; //건물 
	String FAC_CODE; //시설코드
	String FAC_NAME; //시설명 
	String BOOK_STATE; //진행상태 
	String BOOK_NUM; //
	

	public String getFAC_MAIN() {
		return FAC_MAIN;
	}
	public void setFAC_MAIN(String fAC_MAIN) {
		FAC_MAIN = fAC_MAIN;
	}
	public String getFAC_CODE() {
		return FAC_CODE;
	}
	public void setFAC_CODE(String fAC_CODE) {
		FAC_CODE = fAC_CODE;
	}
	public String getFAC_NAME() {
		return FAC_NAME;
	}
	public void setFAC_NAME(String fAC_NAME) {
		FAC_NAME = fAC_NAME;
	}
	public String getBOOK_STATE() {
		return BOOK_STATE;
	}
	public void setBOOK_STATE(String bOOK_STATE) {
		BOOK_STATE = bOOK_STATE;
	}

	public String getBOOK_NUM() {
		return BOOK_NUM;
	}
	public void setBOOK_NUM(String bOOK_NUM) {
		BOOK_NUM = bOOK_NUM;
	}
}
