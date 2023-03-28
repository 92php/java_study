package com.imooc.mgallery.dao;

import java.util.ArrayList;
import java.util.List;

import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.utils.PageModel;
import com.imooc.mgallery.utils.XmlDataSource;

/**
 * �ͻ����ݷ��ʶ���
 * @author Administrator
 *
 */
public class PaintingDao {
	
	public PageModel pagination(int page , int rows) {
		//Painting�ͻ����󼯺�
		List<Painting> list = XmlDataSource.getRowData();
		//PageModel ��ҳ����õ���ҳ���� 
		PageModel pageModel = new PageModel(list,page,rows);
		return pageModel;
	}
	
	public PageModel pagination(int category,int page , int rows) {
		List<Painting> list = XmlDataSource.getRowData();
		List<Painting> categoryList = new ArrayList();
		for(Painting p:list) {
			if(p.getCategory() == category) {
				categoryList.add(p);
			}
		}
		PageModel pageModel = new PageModel(categoryList,page,rows);
		return pageModel;
	}
	
	public void create(Painting painting) {
		XmlDataSource.append(painting);
	}
	
	public Painting findById(Integer id) {
		List<Painting> data = XmlDataSource.getRowData();
		Painting painting = null;
		for(Painting p : data) {
			if(p.getId() == id) {
				painting = p;
				break;
			}
		}
		return painting;
	}
	
	public void update(Painting painting) {
		
	}
}
