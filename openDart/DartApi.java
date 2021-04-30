package openDart;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DartApi {
    // Open Dart에서 부여 받은 서비스키
    static final String ServiceKey = "9bd7d00ee1eca94f1facac3c3486130a5b4eb0cf";

    private static void copy(InputStream input, FileOutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int len = input.read(buf);    // Reads some number of bytes from the input stream and stores them into the buffer array "buf"
                                      // the number of bytes actually read is returned 
        while (len >= 0) {
            output.write(buf, 0, len);  // Writes 'len' bytes from the specified byte array 'buf' starting at offset '0' to this file output stream
            len = input.read(buf);
        }
        output.flush();
    }

    /**
     * Goal : Connect to Open Dart, and download unique code table(고유번호 table) with extention ".zip"
    */ 
    public static void main(String[] args) throws IOException {
        SSLTrust.sslTrustAllCerts();    // 보안정책 우회
        StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/corpCode.xml"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("crtfc_key","UTF-8") + "=" + ServiceKey); /*Service Key*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  // open connection
        conn.setRequestMethod("GET");
        InputStream in = conn.getInputStream();

        FileOutputStream out = new FileOutputStream("download.zip");    // File output stream object
        copy(in, out, 1024);    // copy download data to download.zip
        out.close();
    }
}
