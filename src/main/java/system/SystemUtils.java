package system;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class SystemUtils {

    /**
     * 打开OS当前的默认浏览器并访问指定的url
     * @param url
     */
    public static void openBrowse(String url){
        boolean result=false;
        // 获取操作系统的名字
        String osName = System.getProperty("os.name", "");
        Process proc=null;
        try{
            if (osName.startsWith("Mac OS")) {
                // mac的打开方式
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL",
                        new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });
            } else if (osName.startsWith("Windows")) {// windows的打开方式。
                proc=Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {// Unix or Linux的打开方式
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany","mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    // 执行代码，在brower有值后跳出，
                    // 这里是如果进程创建成功了，==0是表示正常结束。
                    if (Runtime.getRuntime()
                            .exec(new String[] { "which", browsers[count] })
                            .waitFor() == 0)
                        browser = browsers[count];
                if (browser == null)
                    throw new Exception("Could not find web browser");
                else
                    // 这个值在上面已经成功的得到了一个进程。
                    Runtime.getRuntime().exec(new String[] { browser, url });
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理Process进程，打印内容，判断是否执行成功等
     * @param proc
     * @return
     */
    public static boolean handlerProc(Process proc)  {
        boolean execResult=false;
        InputStream stderr = proc.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        try {
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            System.out.println("Exec Log:"+stringBuilder);
            int exitVal = proc.waitFor();
            execResult=exitVal==0;
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(stderr);
            IOUtils.closeQuietly(isr);
            IOUtils.closeQuietly(br);
        }
        return execResult;
    }

    public String getMacAddress() {
        StringBuffer sb = new StringBuffer("");
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

            if (mac!=null&&mac.length>0){
                for(int i=0; i<mac.length; i++) {
                    if(i!=0) {
                        sb.append("-");
                    }
                    //字节转换为整数
                    int temp = mac[i]&0xff;
                    String str = Integer.toHexString(temp);
                    if(str.length()==1) {
                        sb.append("0"+str);
                    }else {
                        sb.append(str);
                    }
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return sb.toString().toUpperCase();
    }

    //通过cmd命令获取Mac地址
    public String getMacAddress2(){
        StringBuilder sb=new StringBuilder();
        String address="";
        try{
            Process ps = Runtime.getRuntime().exec("cmd /c ipconfig /all");
            BufferedReader br = new BufferedReader(new InputStreamReader(  ps.getInputStream(), Charset.forName("GBK")));
            String line="";
            while (null != (line = br.readLine())) {
                sb.append(line);
                sb.append(new String(line.trim().getBytes(),"UTF-8"));
            }
            address=getMacStr(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    private String getMacStr(String outStr){
        String address="";
        String splitStr=outStr.contains("物理地址")?"物理地址":"Physical Address";
        try{
            String[] arr=outStr.split(splitStr);
            for (int j = 0; j < arr.length; j++) {
                System.out.println(arr[j]);
                if(arr[j].contains("子网掩码")||arr[j].contains("Mask Address")){//局域网中通常配置掩码
                    int index = arr[j].indexOf(":");
                    index += 2;
                    address = arr[j].substring(index,index+17);//截取输出中的mac地址
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return address;
    }


}
