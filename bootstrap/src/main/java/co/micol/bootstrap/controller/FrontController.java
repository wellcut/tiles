package co.micol.bootstrap.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.micol.bootstrap.common.Command;
import co.micol.bootstrap.main.command.MainCommand;


@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HashMap<String, Command> map = new HashMap<String, Command>();
 
    public FrontController() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException{
    	map.put("/main.do", new MainCommand());
    }
    
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 요청을 분석하고, 수행할 Command를 찾아서 수행하고, 결과를 돌려준다.
		request.setCharacterEncoding("utf-8");  //한글깨짐 방지
		String uri = request.getRequestURI();   //호출한 URI를 구함
		String contextPath = request.getContextPath(); //root 를 구함
		String page = uri.substring(contextPath.length()); //요청한 페이지 구함
		
		Command command = map.get(page);  //수행할 command를 가져온다
		String viewPage = command.exec(request, response);
		
		if(!viewPage.endsWith(".do")) {
			
			//viewPage = "WEB-INF/views/" + viewPage + ".jsp";
			viewPage += ".tiles";
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}else {
			response.sendRedirect(viewPage);   //결과가 *.do이면 위임해버림
		}		
	}

}
