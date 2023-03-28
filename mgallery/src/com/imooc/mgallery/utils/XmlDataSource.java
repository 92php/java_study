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
 * ��xml�ļ�����ת����java����
 * @author Administrator
 *
 */
public class XmlDataSource {
		//ͨ��static��̬�ؼ��ֱ�֤����ȫ��Ψһ
		private static List<Painting> data = new ArrayList();
		private static String dataFile;
		static {
			//�õ�painting.xml��������·��
			dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
			reload();
			
		}
		
		
		private static void reload() {
			URLDecoder decoder = new URLDecoder();
			try {
				dataFile = decoder.decode(dataFile,"UTF-8");
				//System.out.println(dataFile);
				//����Dom4j��xml���н���
				SAXReader reader = new SAXReader();
				//1.��ȡdocument�ĵ�����
				Document document = reader.read(dataFile);
				//2.Xpath�õ�xml�ڵ㼯��
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
			//1.��ȡxml�ļ����õ�Document����
			SAXReader reader = new SAXReader();
			Writer writer = null;
			try {
				Document document = reader.read(dataFile);
				//2.�����µ�painting�ڵ�
				Element root = document.getRootElement(); //��ȡ���ڵ�
				Element p = root.addElement("painting");
				//3.����painting�ڵ�ĸ����ӽڵ�
				p.addAttribute("id", String.valueOf(data.size()+1));
				p.addElement("pname").setText(painting.getPname());
				p.addElement("category").setText(painting.getCategory().toString());
				p.addElement("price").setText(painting.getPrice().toString());
				p.addElement("preview").setText(painting.getPreview());
				p.addElement("description").setText(painting.getDescription());
				//4.д��xml,���׷�Ӳ���
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
				 reload();  //�ڴ����ļ�����һ��
			}
		}
		
		public static void update(Painting painting) {
			SAXReader reader = new SAXReader();
			Writer writer = null;
			try {
				Document document = reader.read(dataFile);
				//�ڵ�·��[@������=����ֵ]
				List<Node> nodes = document.selectNodes("/root/painting[@id="+painting.getId()+"]");
				if(nodes.size() == 0) {
					throw new RuntimeException("id="+painting.getId()+"����ͻ�������");
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
				 reload();  //�ڴ����ļ�����һ��
			}
			
		}
		
		
		public static void main(String[] args) {
			//new XmlDataSource();
			
			//List<Painting> ps = XmlDataSource.getRowData();
			//System.out.println(ps);
			
			Painting p = new Painting();
			p.setPname("����");
			p.setCategory(1);
			p.setPrice(4000);
			p.setPreview("/upload/10.jpg");
			p.setDescription("��������");
			XmlDataSource.append(p); 

		}
}
