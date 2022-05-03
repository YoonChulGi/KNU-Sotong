package chul.knu_sotong;

/**
 * Created by hong on 2017-03-01.
 */

public class Notice {
    private String user;
    private String content;
    private  int index1, index2;
    private boolean authority = false;
    private String noticePw;

    public void setNoticePw(String noticePw) {
        this.noticePw = noticePw;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public  void setUser(String user) {
        this.user = user;
    }

    public  void setIndex1(int index1) {
        this.index1 = index1;
    }

    public  void setIndex2(int index2) {
        this.index2 = index2;
    }

    public void setAuthority(boolean authority) {
        this.authority = authority;
    }

    /************************************************************************************/

    public String getNoticePw() {
        return noticePw;
    }


    public String getUser() {
        return user;
    }

    public  int getIndex1(){
        return index1;
    }

    public  int getIndex2() {
        return index2;
    }

    public boolean getAuthority() {return authority;}

    private static Notice instance = null;

    public static synchronized Notice getInstance(){
        if(null == instance){
            instance = new Notice();
        }
        return instance;
    }
}