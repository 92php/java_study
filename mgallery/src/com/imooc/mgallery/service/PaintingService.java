package com.imooc.mgallery.service;

import java.util.List;

import com.imooc.mgallery.dao.PaintingDao;
import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.utils.PageModel;

/**
 * 油画服务类
 * @author Administrator
 *
 */
public class PaintingService {
	private PaintingDao paintingDao = new PaintingDao();
	

	public PageModel pagination(int page ,int rows,String...strings) {
		if(rows == 0) {
			throw new RuntimeException("无效的rows参数");
		}
		if(strings.length == 0 || strings[0] == null) {
			return paintingDao.pagination(page, rows);
		}else {
			return paintingDao.pagination(Integer.parseInt(strings[0]),page, rows);
		}	
	}
	
	
	public void create(Painting painting) {
		paintingDao.create(painting);
	}
	
	public Painting findById(Integer id) {
		Painting p = paintingDao.findById(id);
		if(p == null) {
			throw new RuntimeException("[id="+id+"]油画不存在");
		}
		return p;
	}
	
	public void delete(Integer id) {

	}
	
	public void update(Painting newPainting,Integer isPreviewModified) {
		//在原有的数据上进行覆盖
		Painting oldPainting = this.findById(newPainting.getId());
		oldPainting.setPname(newPainting.getPname());
		oldPainting.setCategory(newPainting.getCategory());
		oldPainting.setPrice(newPainting.getPrice());
		oldPainting.setDescription(newPainting.getDescription());
		if(isPreviewModified == 1) {
			oldPainting.setPreview(newPainting.getPreview());
		}
		oldPainting.setPname(newPainting.getPname());
		paintingDao.update(oldPainting);
	}
	
	public static void main(String[] args) {
		PaintingService paintingService = new PaintingService();
		PageModel pageModel = paintingService.pagination(2, 3);
		List<Painting> paintingList = pageModel.getPageData();
		for(Painting painting : paintingList) {
			System.out.println(painting.getPname());
		}
		System.out.println(pageModel.getPageStartRow() + ":" + pageModel.getPageEndRow());
	}
}
