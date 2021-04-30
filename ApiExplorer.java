import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.BufferedReader;
import java.io.IOException;

public class ApiExplorer {
    //final static String SERVICEKEY = "6No4JLBZPafGmvs0YQ%2FWiHSNqVrvqqAox0frrHQJbt%2Fvc6QT4xzf2Idnc9CaktyAdlI3FYGILmdhVIH9u5ck2Q%3D%3D";
    final static String SERVICEKEY = "9bd7d00ee1eca94f1facac3c3486130a5b4eb0cf";

    public static void sslTrustAllCerts(){ 
        TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() { 
                public X509Certificate[] getAcceptedIssuers() { 
                    return null; 
                } 
                public void checkClientTrusted(X509Certificate[] certs, String authType) { } 
                public void checkServerTrusted(X509Certificate[] certs, String authType) { } 
            } 
        }; 
            
        SSLContext sc; 
            
        try { 
            sc = SSLContext.getInstance("SSL"); 
            sc.init(null, trustAllCerts, new SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 
        } catch(Exception e) { 
            e.printStackTrace(); 
        } 
    }

    public static void main(String[] args) throws IOException {
        sslTrustAllCerts();

        // StringBuilder urlBuilder = new StringBuilder("http://api.seibro.or.kr/openapi/service/StockSvc/getKDRIssuLmtDetailsN1"); /*URL*/
        // urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + SERVICEKEY); /*Service Key*/
        // urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode(SERVICEKEY, "UTF-8")); /*공공데이터포털에서 받은 인증키*/
        // urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        // urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        // urlBuilder.append("&" + URLEncoder.encode("isin","UTF-8") + "=" + URLEncoder.encode("KR8392070007", "UTF-8")); /*표준코드*/

        StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/list.json");
        urlBuilder.append("?" + URLEncoder.encode("crtfc_key","UTF-8") + "=" + URLEncoder.encode(SERVICEKEY, "UTF-8")); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("bgn_de","UTF-8") + "=" + URLEncoder.encode("20200117", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("end_de","UTF-8") + "=" + URLEncoder.encode("20200117", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("corp_cls","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("page_no","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("page_count","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));

        

        // StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/corpCode.xml");   
        // urlBuilder.append("?" + URLEncoder.encode("crtfc_key","UTF-8") + "=" + URLEncoder.encode(SERVICEKEY, "UTF-8")); /*Service Key*/     	
        
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/zip");
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response code: " + conn.getResponseCode());
         
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());        
    }
}