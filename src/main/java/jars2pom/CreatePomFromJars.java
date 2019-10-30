package jars2pom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;

public class CreatePomFromJars {

	public static void main(String[] args) throws Exception {
//		Map map =loadProperties("jars2pom.properties");//该文件用于配置lib文件的路径
//		String libFullPath = (String) map.get("libFullPath");
//		String libFullPath="D:\\workspace\\workspace_yzb\\tenderClient_hebei\\tenderClient\\lib";
		String libFullPath="D:\\workspace\\workspace_yzb\\caservice\\caservice\\WebContent\\WEB-INF\\lib";
		System.out.println("libFullPath:" + libFullPath);
		if (libFullPath == null || "".equals(libFullPath)) {
			System.out.println("路径配置错误");
			return;
		}
		if (!new File(libFullPath).exists()) {
			System.out.println("路径" + libFullPath + "不存在");
			return;
		}
		if (!new File(libFullPath).isDirectory()) {
			System.out.println(libFullPath + "不是有效路径");
			return;
		}
		String dependcies=parseJar(libFullPath);
		System.out.println("dependcies:"+dependcies);
	}
	
	public static String parseJar(String libFullPath){
		File dir = new File(libFullPath);
		int successNum = 0;//统计成功的个数
		int failNum = 0;//统计失败的个数
		File[] jars = dir.listFiles(new JarFileFilter());
		System.out.println("该目录下共有jar文件" + jars.length + "个");
		Element dependencies = new DOMElement("dependencies");
		Manifest manifest=null;
		for (File jar : jars) {
			JarInputStream jis=null;
			Element ele = null;
			try {
				jis = new JarInputStream(new FileInputStream(jar));
				manifest = jis.getManifest();// 返回此 JAR 文件的 Manifest；如果没有，则返回null。
				jis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (manifest == null) {
				System.out.println("----------该jar包无Manifest信息:" + jar.getName()+"------------");
				failNum++;
				continue;
			}

			String bundleName = manifest.getMainAttributes().getValue(
					"Bundle-Name");//artifactId
			String bundleVersion = manifest.getMainAttributes().getValue(
					"Bundle-Version");//version
		
			StringBuffer sb = new StringBuffer(jar.getName());
			if (bundleName != null) {// jar包中存在bundleName属性时，直接通过该属性到maven网站上查找
				bundleName = bundleName.toLowerCase().replace(" ", "-");
				sb.append(bundleName+"\t").append(bundleVersion);
				ele = getDependices(bundleName, bundleVersion);
				if (ele != null && ele.elements().size() != 0)	successNum++;
				System.out.println(jar.getName()+"=> groupId:"+ele.elementText("groupId")+";artifactId:"+ele.elementText("artifactId")+";version:"+ele.elementText("version"));
			}
			
//			System.out.println("jar:" + jar.getName());
			if (ele == null || ele.elements().size() == 0) {// 若以上查找不到，则将命名格式处理后再查找一遍
				bundleName = "";
				bundleVersion = "";
				String[] ns = jar.getName().replace(".jar", "").split("-");
				for (String s : ns) {
					if (Character.isDigit(s.charAt(0))) {
						bundleVersion += s + "-";
					} else {
						bundleName += s + "-";
					}
				}
				if (bundleVersion.endsWith("-")) {
					bundleVersion = bundleVersion.substring(0,
							bundleVersion.length() - 1);
				}
				if (bundleName.endsWith("-")) {
					bundleName = bundleName.substring(0,
							bundleName.length() - 1);
				}
				ele = getDependices(bundleName, bundleVersion);
				if(ele != null && ele.elements().size() != 0){
					successNum++;
					sb.setLength(0);
					System.out.println(jar.getName()+"=> groupId:"+ele.elementText("groupId")+";artifactId:"+ele.elementText("artifactId")+";version:"+ele.elementText("version"));
					sb.append(bundleName + "\t").append(bundleVersion);
				}
			}
//			ele = getDependices(bundleName, bundleVersion);

			if (ele.elements().size() == 0) {// 若依然找不到，则将该jar名称置为#not
												// found,由使用者自己处理
				ele.add(new DOMElement("groupId").addText("#not found"));
				ele.add(new DOMElement("artifactId").addText(bundleName));
				ele.add(new DOMElement("version").addText(bundleVersion));
				failNum++;
			}
			dependencies.add(ele);

		}
		System.out.println("共计"+jars.length+"成功解析"+successNum+";解析失败"+failNum);
		return dependencies.asXML();
	}

	public static Element getDependices(String key, String ver) {
		Element dependency = new DOMElement("dependency");
		// 设置代理
		// System.setProperty("http.proxyHost", "127.0.0.1");
		// System.setProperty("http.proxyPort", "8090");
		try {
			String url = "http://search.maven.org/solrsearch/select?q=a%3A%22"
					+ key + "%22%20AND%20v%3A%22" + ver + "%22&rows=3&wt=json";
			// System.out.println("searchUrl:"+url);
			org.jsoup.nodes.Document doc = Jsoup.connect(url)
					.ignoreContentType(true).timeout(30000).get();
			String elem = doc.body().text();
			JSONObject response = JSONObject.parseObject(elem).getJSONObject(
					"response");
			if (response.containsKey("docs")
					&& response.getJSONArray("docs").size() > 0) {
				JSONObject docJson = response.getJSONArray("docs")
						.getJSONObject(0);
				Element groupId = new DOMElement("groupId");
				Element artifactId = new DOMElement("artifactId");
				Element version = new DOMElement("version");
				groupId.addText(docJson.getString("g"));
				artifactId.addText(docJson.getString("a"));
				version.addText(docJson.getString("v"));
//				System.out.println("groupId:"+docJson.getString("g")+";artifactId:"+docJson.getString("a")+";version:"+docJson.getString("v"));
				dependency.add(groupId);
				dependency.add(artifactId);
				dependency.add(version);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dependency;
	}
	
	public static Map loadProperties(String filePath) throws Exception{
		Map map=new HashMap();
		File file=new File(filePath);
		if(filePath==null || "".equals(filePath)) {
			throw new Exception("-------输入路径为空---------");
		}
		if(!file.exists()) {
			throw new Exception("-------文件"+filePath+"不存在------");
			
		}
		Properties prop = new Properties();     
        InputStream in =null;
        try {
        	in=new BufferedInputStream (new FileInputStream( filePath));
			prop.load(in);
			Iterator<String> it=prop.stringPropertyNames().iterator();
		    while(it.hasNext()){
		        String key=it.next();
//		        System.out.println(key+":"+prop.getProperty(key));
		        map.put(key, prop.getProperty(key));
		    }
		    in.close();
        } catch (IOException e) {
			e.printStackTrace();
		}    
        return map;
	}

}

class JarFileFilter implements FileFilter {

	public boolean accept(File file) {
		if (file.isFile() && file.getName().endsWith("jar")) {
			return true;
		}
		return false;
	}

}