package openDart;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser {
    static final String SERVICEKEY = "9bd7d00ee1eca94f1facac3c3486130a5b4eb0cf";    // service key

    private static void extractBizrNo(ArrayList<String> corpCodeList, ArrayList<String> corpNameList) throws IOException {
        System.out.println("corpCodeList length = " + corpCodeList.size()); // Total number of record for "고유번호"
        for (int i = 0; i < 100; i++) {
            SSLTrust.sslTrustAllCerts();    // 보안 우회    
            StringBuilder urlBuilder = new StringBuilder("https://opendart.fss.or.kr/api/company.xml");
            try {
                urlBuilder.append(
                        "?" + URLEncoder.encode("crtfc_key", "UTF-8") + "=" + URLEncoder.encode(SERVICEKEY, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("corp_code", "UTF-8") + "="
                        + URLEncoder.encode(corpCodeList.get(i), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");
            
            //System.out.println("conn.getResponseCode() = " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                rd = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

                // xml을 파싱해주는 객체를 생성
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder;
                try {
                    documentBuilder = factory.newDocumentBuilder(); // Convert xml string to InputStream
                    InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
                    Document doc = documentBuilder.parse(is);   // 파싱 시작
                    Element element = doc.getDocumentElement(); // 최상위 노드 찾기
                    NodeList items = element.getElementsByTagName("bizr_no");   // 원하는 태그 데이터 찾아오기
                    Node node = items.item(0);
                    if (node == null) {
                        System.out.println("node name is null");
                    } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element ele = (Element) node;
                        String nodeName = ele.getTextContent();
                        System.out.println("고유번호: " + corpCodeList.get(i));
                        System.out.println("기업체명: " + corpNameList.get(i));
                        System.out.println("사업자번호: " + nodeName);
                    }
                } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                continue;
            }
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        // XML 문서 파싱
        Path currentRelativePath = Paths.get("");
        String currentAbsoultePath = currentRelativePath.toAbsolutePath().toString();   // absolute path 

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  // build factory
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(currentAbsoultePath + "/CORPCODE.xml");   // parse document

        Element root = document.getDocumentElement();   // get a root
        NodeList childeren = root.getChildNodes(); // get a children node list

        ArrayList<String> corpNameList = new ArrayList<String>(); // Create an ArrayList object
        ArrayList<String> corpCodeList = new ArrayList<String>(); // Create an ArrayList object
        for (int i = 0; i < childeren.getLength(); i++) {
            Node node = childeren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) { // 해당 노드의 종류 판정(Element일 때)
                Element ele = (Element) node;
                NodeList grandChilderen = ele.getChildNodes();
                for (int a = 0; a < grandChilderen.getLength(); a++) {
                    Node node2 = grandChilderen.item(a);
                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element ele2 = (Element) node2;
                        String nodeName2 = ele2.getNodeName();
                        if (nodeName2.equals("corp_code")) {
                            corpCodeList.add(ele2.getTextContent());
                        } else if (nodeName2.equals("corp_name")) {
                            corpNameList.add(ele2.getTextContent());
                        }
                    }
                }
            }
        }
        extractBizrNo(corpCodeList, corpNameList);    // "CORPCODE.xml"에서 추출된 corp_code(고유번호)값에 해당하는 사업자번호를 찾기위해 함수 호출
    }
}