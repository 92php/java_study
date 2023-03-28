package com.imooc.mgallery.utils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.imooc.mgallery.entity.Painting;



/**
 * 将xml文件数据转换成java对象
 * @author Administrator
 *
 */
public class XmlDataSource {
		//通过static静态关键字保证数据全局唯一
		private static List<Painting> data = new ArrayList();
		private static String dataFile;
		static {
			//得到painting.xml完整物理路径
			dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
			reload();
			
		}
		
		
		private static void reload() {
			URLDecoder decoder = new URLDecoder();
			try {
				dataFile = decoder.decode(dataFile,"UTF-8");
				//System.out.println(dataFile);
				//利用Dom4j对xml进行解析
				SAXReader reader = new SAXReader();
				//1.获取document文档对象
				Document document = reader.read(dataFile);
				//2.Xpath得到xml节点集合
				List<Node> nodes = document.selectNodes("/root/painting");
				data.clear();
				for(Node node : nodes) {
					Element element = (Element)node;
					String id = element.attributeValue("id");
					String pname = element.elementText("pname");
					//System.out.println(id+":"+ pname);
					Painting painting = new Painting();
					painting.setId(Integer.parseInt(id));
					painting.setPname(pname);
					painting.setCategory(Integer.parseInt(element.elementText("category")));
					painting.setPrice(Integer.parseInt(element.elementText("price")));
					painting.setDescription(element.elementText("description"));
					painting.setPreview(element.elementText("preview"));
					data.add(painting);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public static List<Painting> getRowData(){
			return data;
		}
		
		public static void append(Painting painting) {
			//1.读取xml文件，得到Document对象
			SAXReader reader = new SAXReader();
			Writer writer = null;
			try {
				Document document = reader.read(dataFile);
				//2.创建新的painting节点
				Element root = document.getRootElement(); //获取跟节点
				Element p = root.addElement("painting");
				//3.创建painting节点的各个子节点
				p.addAttribute("id", String.valueOf(data.size()+1));
				p.addElement("pname").setText(painting.getPname());
				p.addElement("category").setText(painting.getCategory().toString());
				p.addElement("price").setText(painting.getPrice().toString());
				p.addElement("preview").setText(painting.getPreview());
				p.addElement("description").setText(painting.getDescription());
				//4.写入xml,完成追加操作
				writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
				document.write(writer);
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				 if(writer != null) {
					 try {
					 writer.close();
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 }
				 reload();  //内存与文件数据一致
			}
		}
		
		public static void update(Painting painting) {
			SAXReader reader = new SAXReader();
			Writer writer = null;
			try {
				Document document = reader.read(dataFile);
				//节点路径[@属性名=属性值]
				List<Node> nodes = document.selectNodes("/root/painting[@id="+painting.getId()+"]");
				if(nodes.size() == 0) {
					throw new RuntimeException("id="+painting.getId()+"编号油画不存在");
				}
				Element p = (Element)nodes.get(0);
				p.selectSingleNode("pname").setText(painting.getPname());
				p.selectSingleNode("category").setText(painting.getCategory().toString());
				p.selectSingleNode("price").setText(painting.getPrice().toString());
				p.selectSingleNode("preview").setText(painting.getPreview());
				p.selectSingleNode("description").setText(painting.getDescription());
				
				writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
				document.write(writer);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				 if(writer != null) {
					 try {
					 writer.close();
					 }catch(Exception e){
						 e.printStackTrace();
					 }
				 }
				 reload();  //内存与文件数据一致
			}
			
		}
		
		
		public static void main(String[] args) {
			//new XmlDataSource();
			
			//List<Painting> ps = XmlDataSource.getRowData();
			//System.out.println(ps);
			
			Painting p = new Painting();
			p.setPname("测试");
			p.setCategory(1);
			p.setPrice(4000);
			p.setPreview("/upload/10.jpg");
			p.setDescription("测试描述");
			XmlDataSource.append(p); 

		}
}
