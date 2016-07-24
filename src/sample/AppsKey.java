package sample;

public class AppsKey {
    String oa0 = "oINvM4zJ3UUL21YM6Xb7mIF8k";
    String oas0 = "OPw7qrXj8v1zkMmgNYYOzkYSeKNRWo8s6hCaMSXI74mcSrLidE";

    String oa1 = "h2PgDpnMFRH59bvDMzoRSTIxO";
    String oas1 = "X4zXceDiNIJKQjmBMG7Nh9ZYOUvFO9ePapuHFuVWnHSjJ4OPFH";

    String oa2 = "EFVqRxMRtmJPARuLqq0GPj57d";
    String oas2 = "Dmoh2D8RVSVlJEJp2siyuk61xJtSNp46tSKqD1PfgrIbfaVgGU";
    public AppsKey(){

    }

    public String getoauthtoken(int i){
        if(i==0){
            return oa0;
        }
        if(i==1){
            return oa1;
        }
        if(i==2){
            return oa2;
        }
        return "Ошибка";
    }
    public String getoauthtokensecret(int i){
        if(i==0){
            return oas0;
        }
        if(i==1){
            return oas1;
        }
        if(i==2){
            return oas2;
        }
        return "Ошибка";
    }
}
