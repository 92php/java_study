package com.imooc.mgallery.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.service.PaintingService;
import com.imooc.mgallery.utils.PageModel;

/**
 * Servlet implementation class ManagementController
 */
@WebServlet("/management")
public class ManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	private PaintingService paintingService = new PaintingService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManagementController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		String method = request.getParameter("method");
		if(method.equals("list")) {
			this.list(request,response);
		}else if(method.equals("delete")) {
			this.delete(request,response);
		}else if(method.equals("show_create")) {
			this.showCreatePage(request,response);
		}else if(method.equals("create")) {
			this.create(request,response);
		}else if(method.equals("show_update")) {
			this.showUpdatePage(request,response);
		}else if(method.equals("update")) {
			this.update(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String p = request.getParameter("p");
		String r = request.getParameter("r");
		if(p == null) {
			p = "1";
		}
		if(r == null) {
			r = "6";
		} 
		PageModel pageModel = paintingService.pagination(Integer.parseInt(p), Integer.parseInt(r));
		request.setAttribute("pageModel", pageModel);
		request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
	}
	
	protected void showCreatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(request, response);
	}
	
	protected void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//文件上传时的数据与标准表单完全不同
		//String pname = request.getParameter("pname");
		
		//1.初始化FileUpload组件
		FileItemFactory factory = new DiskFileItemFactory();
		/**
		 * FileItemFactory 用于将前端的数据转换为一个个FileItem对象
		 * ServletFileUpload 则是为FileUpload组件提供java web的http请求解析
		 */
		ServletFileUpload sf = new ServletFileUpload(factory);
		//2.遍历所有的FileItem
		try {
			List<FileItem> formData = sf.parseRequest(factory);
			Painting painting = new Painting();
			for(FileItem fi:formData) {
				if(fi.isFormField()) {
					//System.out.println("普通输入项："+ fi.getFieldName() + ":" + fi.getString("UTF-8"));
					switch(fi.getFieldName()) {
					case "pname":
						painting.setPname(fi.getString("UTF-8"));
						break;
					case "category":
						painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
						break;
					case "price":
						painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
						break;
					case "description":
						painting.setDescription(fi.getString("UTF-8"));
						break;
					default:
						break;
					}
				}else {
					//System.out.println("文件上传项："+ fi.getFieldName());
					//3.文件保存服务器目录
					String path = request.getServletContext().getRealPath("/upload");
					//System.out.println("上传文件目录："+path);
					//String fileName  = "test.jpg";
					String fileName = UUID.randomUUID().toString();
					String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
					fi.write(new File(path,fileName + suffix));
					painting.setPreview("upload/"+fileName+suffix);
				}
			}
			paintingService.create(painting); //新增功能
			//响应重定向
			response.sendRedirect("/management?method=list"); //返回列表页
		}catch(Exception e) {
			e.printStackTrace();
		}

		}
	
	
		protected void showUpdatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String id = request.getParameter("id");
			Painting painting = paintingService.findById(Integer.parseInt(id));
			request.setAttribute("painting", painting);
			request.getRequestDispatcher("/WEB-INF/jsp/update.jsp").forward(request, response);
		}
		
		protected void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		}
			
		protected void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String id = request.getParameter("id");
			PrintWriter out = response.getWriter();
			try {
				paintingService.delete(Integer.parseInt(id));
				out.println("{\"result\":\"ok\"}");
			}catch(Exception e) {
				e.printStackTrace();
				out.println("{\"result\":\""+e.getMessage()+"\"}");
			}
		}
		
		
		
		
		
	}

}
