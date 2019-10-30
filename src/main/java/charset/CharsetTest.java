package charset;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class CharsetTest {

    @Test
    public void test1(){
        String a="焦洋大人haha";
        byte[] b=a.getBytes(Charset.forName("utf-8"));
        System.out.println(b.toString());
    }

    @Test
    public void charsetTest2(){
        String a="haha焦洋大人";
        Charset charset=Charset.forName("utf-8");
        ByteBuffer byteBuffer=charset.encode(a);
        System.out.println(Arrays.toString(byteBuffer.array()));
        CharBuffer charBuffer=charset.decode(byteBuffer);
        System.out.println(charBuffer);
    }

    @Test
    public void charsetTest3() throws UnsupportedEncodingException {
        String a="I am 君山";
        System.out.println(a.toCharArray());
//        printBytes(a,"utf-8");
        String defalut=bytesToHexString(a.getBytes());
        System.out.println("default:"+defalut);

        String utf8=strToHex(a,"utf-8");
        System.out.println("utf8:"+utf8);

        String utf16=strToHex(a,"utf-16");
        System.out.println("utf16:"+utf16);

        String iso8859Hex=strToHex(a,"iso-8859-1");
        System.out.println("iso8859Hex:"+iso8859Hex);

        String gbk=strToHex(a,"gbk");
        System.out.println("gbk:"+gbk);

        String ascii=strToHex(a,"ascii");
        System.out.println("ascii:"+ascii);
    }

    @Test
    public  void  charset4(){
        String a="I am 君山";
        System.out.println("GBK:"+new String(a.getBytes(),Charset.forName("GBK")));
        System.out.println("GBK2:"+new String(a.getBytes(Charset.forName("GBK")),Charset.forName("GBK")));

        System.out.println("ISO-8869-1:"+new String(a.getBytes(),Charset.forName("ISO-8859-1")));
        System.out.println("utf-8:"+new String(a.getBytes(),Charset.forName("utf-8")));
        System.out.println("utf-16:"+new String(a.getBytes(),Charset.forName("utf-16")));
        System.out.println("utf-16_2:"+new String(a.getBytes(Charset.forName("utf-16")),Charset.forName("utf-16")));

        System.out.println("ascii:"+new String(a.getBytes(),Charset.forName("ascii")));
        System.out.println("ascii2:"+new String(a.getBytes(Charset.forName("ascii")),Charset.forName("ascii")));

    }

    public void printBytes(String str,String charsetName){
        Charset charset=Charset.forName(charsetName);
        ByteBuffer byteBuffer=charset.encode(str);
        System.out.println(charsetName+":"+Arrays.toString(byteBuffer.array()));
    }

//    二进制字节转十六进制时，将字节高位与0xF0做"&"操作,然后再左移4位，
//      得到字节高位的十六进制A;将字节低位与0x0F做"&"操作，得到低位的十六进制B，
//      将两个十六进制数拼装到一块AB就是该字节的十六进制表示。
    public String BinaryToHexString(byte[] bytes,String hexStr){
        String result = "";
        String hex = "";
        for(int i=0;i<bytes.length;i++){
            //字节高4位
	             hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4)); 
            //字节低4位
	            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F)); 
                    result +=hex;
        }
        return result;

    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String strToHex(String str,String charsetName){
        Charset charset=Charset.forName(charsetName);
        ByteBuffer byteBuffer=charset.encode(str);
        StringBuilder stringBuilder = new StringBuilder("");
        byte[] src =byteBuffer.array();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
