package RSA;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

public class RSATest {

    String encryptStr="mPu+FgmuPOF3MWfIlgY7tySP4LgtAn3jyD3Q6Zz+agIQhPwxHyHCrBEi4fcgLkXvPs652xllh0rlOeiVxr+J4WX6beGJRdeAFgNJ47CpbGbcf7KKDApK5shf4N2HKh3Uq8ckJb/E5Ga41asF2MoDQFpL3yV1IKPKX5EQZGUzliA=";

    @Test
    public void test0() throws Exception {
        String urlcode = "BZBfe7eeff3cf4ce505a0c52f90044ebf6b6f05ebb61c84070a464bf013af59869f20190903";

        String encodeStr = new String(RSAUtils.encryptByPublicKey(new String(java.util.Base64.getEncoder().encode(urlcode.getBytes()),"utf-8").getBytes()),"utf-8");
        System.out.println("密文："+encodeStr);

        byte[] decryData=RSAUtils.decryptByPrivateKey(encryptStr.getBytes("utf-8"));
        String deccryBase64=new String(decryData,"utf-8");
        System.out.println("解密Base64:"+deccryBase64);

        String decryptResource=new String(Base64.getDecoder().decode(deccryBase64),"utf-8");
        System.out.println("原文："+decryptResource);
    }

    @Test
    public void  test1() throws Exception {
        String urlcode = "BZBfe7eeff3cf4ce505a0c52f90044ebf6b6f05ebb61c84070a464bf013af59869f20190903";
        System.out.println("urlcode:"+urlcode);
        String base64Str=new String(java.util.Base64.getEncoder().encode(urlcode.getBytes()),"utf-8");
        System.out.println("base64Str:"+base64Str);
        byte[] data=base64Str.getBytes();
        byte[] encryData=RSAUtils.encryptByPublicKey(data);
        urlcode = new String(encryData,"utf-8");
        System.out.println(urlcode);

        byte[] decryData=RSAUtils.decryptByPrivateKey(urlcode.getBytes("utf-8"));
        String str=new String(decryData,"utf-8");
        System.out.println(str);

    }

    @Test
    public void test2() throws Exception {
        Map keyMap =RSAUtils.genKeyPair();
        System.out.println(keyMap);

        System.out.println("getPublicKey: "+RSAUtils.getPublicKey(keyMap));;
        System.out.println("getPrivateKey: "+RSAUtils.getPrivateKey(keyMap));;

    }
}
