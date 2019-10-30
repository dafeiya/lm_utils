package httprequest;  

import java.nio.charset.Charset;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.Test;


public class HttpClientTest {

    @Test
    public void test1(){
        JSONObject json=new JSONObject();
        json.put("test","123");
        HttpClientTest.httpPost(json,"http://flever.natapp1.cc/ebidding/services/rest/sso/verifyUserService");
       // HttpClientTest.httpPost(json,"http://baidu.com");
    }
	
    public static void main(String[] args) throws Exception {  
    	System.out.println("����-------");
    	JSONObject json=new JSONObject();
    	json.put("id", "123");
    	json.put("name", "liemng");
    	json.put("birthday", "");
//    	String url="http://localhost:9000/ws/jaxrs/customer/deal";
//    	String url="http://localhost:8185/SalesTrans/rest/customer/deal";
    	String url="http://localhost:8185/SalesTrans/rest/salestransaction/salestranslitev61";
    	String salesTrans="{ \"apiKey\":\"CRMINT\", \"signature\":\"076695F3E88BF829EEE62237E79F6F53\", \"docKey\":\"000600620161124000001\", \"salesItem\":[ { \"salesLineNumber\":1,	\"salesman\":null, \"itemCode\":\"0001\", \"itemOrgId\":\"000003\", \"itemLotNum\":\"*\", \"serialNumber\":null, \"inventoryType\":0, \"qty\":1.0, \"itemDiscountLess\":0.0, \"totalDiscountLess\":0.0, \"netAmount\":20.0, \"salesItemRemark\":null, \"extendParameter\":null } ], \"salesTotal\":{ \"cashier\":\"001\", \"vipCode\":\"100\", \"netQty\":0.0, \"netAmount\":20.0, \"extendParameter\":null, \"calculateVipBonus\":\"0\" }, \"transHeader\":{ \"txDate\":\"2016-11-24\", \"ledgerDatetime\":\"2016-11-24 09:18:09\", \"storeCode\":\"SC000002\", \"tillId\":\"01\", \"docNo\":\"000600620161124000001\", \"voidDocNo\":\"\", \"txAttrib\":\"\" }, \"salesTender\":[ { \"baseCurrencyCode\":\"CNY\", \"tenderCode\":\"CH\", \"payAmount\":20.0, \"baseAmount\":20.0, \"excessAmount\":0.00, \"extendParameter\":null },{ \"baseCurrencyCode\":\"CNY\", \"tenderCode\":\"CH\", \"payAmount\":20.0, \"baseAmount\":20.0, \"excessAmount\":0.00, \"extendParameter\":null },{ \"baseCurrencyCode\":\"CNY\", \"tenderCode\":\"CH\", \"payAmount\":20.0, \"baseAmount\":20.0, \"excessAmount\":0.00, \"extendParameter\":null } ], \"orgSalesMemo\":null }";
//    	String salesTrans="{ \"apiKey\":\"CRMINT\", \"signature\":\"076695F3E88BF829EEE62237E79F6F53\", \"docKey\":\"000600620161124000001\", \"salesItem\":[ { \"salesLineNumber\":1,	\"salesman\":null, \"itemCode\":\"0001\", \"itemOrgId\":\"000003\", \"itemLotNum\":\"*\", \"serialNumber\":null, \"inventoryType\":0, \"qty\":1.0, \"itemDiscountLess\":0.0, \"totalDiscountLess\":0.0, \"netAmount\":20.0, \"salesItemRemark\":null, \"extendParameter\":null } ], \"salesTotal\":{ \"cashier\":\"001\", \"vipCode\":\"100\", \"netQty\":0.0, \"netAmount\":20.0, \"extendParameter\":null, \"calculateVipBonus\":\"0\" }, \"transHeader\":{ \"txDate\":\"2016-11-24\", \"ledgerDatetime\":\"2016-11-24 09:18:09\", \"storeCode\":\"SC000002\", \"tillId\":\"01\", \"docNo\":\"000600620161124000001\", \"voidDocNo\":\"\", \"txAttrib\":\"\" }, \"salesTender\":[ { \"baseCurrencyCode\":\"CNY\", \"tenderCode\":\"CH\", \"payAmount\":20.0, \"baseAmount\":20.0, \"excessAmount\":0.00, \"extendParameter\":null } ], \"orgSalesMemo\":null } �ˆν���{  \"apiKey\":\"CRMINT\", \"signature\":\"715500AEAA160CF6948C89F566FCF4C3\", \"docKey\":\"000600620161124000002\", \"transHeader\":{  \"txDate\":\"2016-11-24\", \"ledgerDatetime\":\"2016-11-24 19:18:09\", \"storeCode\":\"SC000002\", \"tillId\":\"01\", \"docNo\":\"000600620161124000002\", \"voidDocNo\":\"000600620161124000001\", \"txAttrib\":\"\" }, \"salesTotal\":{  \"cashier\":\"001\", \"vipCode\":\"100\", \"netQty\":0.0, \"netAmount\":-20.0, \"extendParameter\":null, \"calculateVipBonus\":\"0\" }, \"salesItem\":[  {  \"salesLineNumber\":1, \"salesman\":null, \"itemCode\":\"0001\", \"itemOrgId\":\"000003\", \"itemLotNum\":\"*\", \"serialNumber\":null, \"inventoryType\":0, \"qty\":-1.0, \"itemDiscountLess\":0.0, \"totalDiscountLess\":0.0, \"netAmount\":-20.0, \"salesItemRemark\":null, \"extendParameter\":null } ], \"salesTender\":[  {  \"baseCurrencyCode\":\"CNY\", \"tenderCode\":\"CH\", \"payAmount\":-20.0, \"baseAmount\":-20.0, \"excessAmount\":0.00, \"extendParameter\":null } ], \"orgSalesMemo\":null }";
    	JSONObject salesTransJson=JSONObject.fromObject(salesTrans);
    	httpPost(salesTransJson,url);  
    	
//        post("http://localhost:9000/ws/jaxrs/customer/search?name=abc");  
    }  
      
    public static void httpPost(JSONObject jsonObj,String url){
        boolean isSuccess = false;
        System.out.println("url:"+url);
        HttpPost post = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            // ���ó�ʱʱ��
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
                
            post = new HttpPost(url);
            // ������Ϣͷ
            post.setHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Connection", "closed");
            
//            String sessionId = getSessionId();
//            post.setHeader("SessionId", sessionId);
//            post.setHeader("appid", appid);
                        
            // ������Ϣʵ��
            StringEntity entity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            // ����Json��ʽ����������
            entity.setContentType("application/json");
            post.setEntity(entity);

            System.out.println("sendRequest:");
            HttpResponse response = httpClient.execute(post);
            System.out.println("response:"+response.toString());
                
            // ���鷵����
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != HttpStatus.SC_OK){
            	
            	System.out.println("�������: "+statusCode);
                isSuccess = false;
            }else{
                int retCode = 0;
                String sessendId = "";
                // �������а���retCode���ỰId
                for(Header header : response.getAllHeaders()){
                    if(header.getName().equals("retcode")){
                        retCode = Integer.parseInt(header.getValue());
                    }
                    if(header.getName().equals("SessionId")){
                        sessendId = header.getValue();
                    }
                }
                

            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }finally{
            if(post != null){
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
  
}  