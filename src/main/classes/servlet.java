import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.formula.functions.Na;

@WebServlet(name = "servlet", value = "/servlet")
public class servlet extends HttpServlet {
    Dao dao=new Dao();
    private static final long serialVersionUID = 1L;

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String res = dao.login(Integer.parseInt(id), password);
        System.out.println(res + "RESSS");
        if (res.equals("登录错误")) {
            PrintWriter out = response.getWriter();
            out.print("<script>alert('登录失败!');window.location.href='Login.html'</script>");
        }
        else {
            HttpSession session = request.getSession();
            session.setAttribute("id", id);
            PrintWriter out = response.getWriter();
            out.print("<script>window.location.href='UserDataIn.html'</script>");
        }
    }
    public void tecon( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException, InterruptedException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }
        String fileName="";
        String filePath="";
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 中文处理
        upload.setHeaderEncoding("UTF-8");

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;


        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")

            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        fileName= new File(item.getName()).getName();
                        filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        System.out.println(filePath);
                        // 保存文件到硬盘
                        item.write(storeFile);
                        request.setAttribute("message",
                                "文件上传成功!");
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "错误信息: " + ex.getMessage());
        }
        HttpSession session = request.getSession();
        String id=(String) session.getAttribute("id");
        char[] nameArr=fileName.toCharArray();
        String filename="";
        for (int i = 0; i < nameArr.length; i++) {
            if(nameArr[i]!='('&&nameArr[i]!=')'&&nameArr[i]!='-'&&nameArr[i]!='、')filename=filename+nameArr[i];
        }
        String[] spstr=filename.split("\\.");

        StringBuffer strbuff = new StringBuffer();
        System.out.println(spstr.length);
        for (int i = 0; i < spstr.length-1; i++) {
            strbuff.append(spstr[i]);
        }
        String TableName=strbuff.toString();
        dao.CreataTab(TableName);
        ReadExcel r=new ReadExcel();
        List<List<Object>> rowlist=r.ExcelToRowList(filePath);
        dao.FirInputData(rowlist,TableName);
        dao.FirInsertToHis(TableName,id);
        JSONObject ob=new JSONObject();
        ob.put("code", 0);
        ob.put("msg", "");
        ob.put("count",1);
        ob.put("data","'filename':'AAA'");
        PrintWriter out = response.getWriter();
        out.write(ob.toString());
    }
    public void Ttable( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("连接了生成表格");
        /*
        PrintWriter out = response.getWriter();
        String json="{'code': 0,'msg':'','count':100,'data':[{'id':'1','username':'name','sex':'男'}]}";
        return json;

         */
        List<SqlTableBean> list=dao.getTableAll("firtable");
        JSONArray json=new JSONArray();
        for(int i=0;i<list.size();i++){
            JSONObject ob=new JSONObject();
            ob.put("ywname",list.get(i).getYwname());
            ob.put("zwname",list.get(i).getZwname());
            ob.put("danwei",list.get(i).getDanwei());
            ob.put("zdtype",list.get(i).getZdtype());
            ob.put("beizhu",list.get(i).getBeizhu());
            ob.put("ispri",list.get(i).getIspri());
            ob.put("chtime",list.get(i).getChtime());
            json.add(ob);
        }
        JSONObject ob=new JSONObject();
        ob.put("code", 0);
        ob.put("msg", "");
        ob.put("count",1);
        ob.put("data",json);
        PrintWriter out = response.getWriter();
        out.write(ob.toString());
    }
    public void HisTab( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        List<HisTabBean> list=dao.HisTab();
        JSONArray json=new JSONArray();
        for(int i=0;i<list.size();i++){
            JSONObject ob=new JSONObject();
            ob.put("tabid",list.get(i).getId());
            ob.put("tabname",list.get(i).getTabname());
            ob.put("crtime",list.get(i).getCrtime());
            ob.put("user",list.get(i).getUser());
            json.add(ob);
        }
        JSONObject ob=new JSONObject();
        ob.put("code", 0);
        ob.put("msg", "");
        ob.put("count",1);
        ob.put("data",json);
        PrintWriter out = response.getWriter();
        out.write(ob.toString());
    }
    public void PopTable( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String tabname=request.getParameter("tabname");
        List<SqlTableBean> list=dao.getTableAll(tabname);
        JSONArray json=new JSONArray();
        for(int i=0;i<list.size();i++){
            JSONObject ob=new JSONObject();
            ob.put("ywname",list.get(i).getYwname());
            ob.put("zwname",list.get(i).getZwname());
            ob.put("danwei",list.get(i).getDanwei());
            ob.put("zdtype",list.get(i).getZdtype());
            ob.put("beizhu",list.get(i).getBeizhu());
            ob.put("ispri",list.get(i).getIspri());
            ob.put("chtime",list.get(i).getChtime());
            json.add(ob);
        }
        JSONObject ob=new JSONObject();
        ob.put("code", 0);
        ob.put("msg", "");
        ob.put("count",1);
        ob.put("data",json);
        PrintWriter out = response.getWriter();
        out.write(ob.toString());
    }
    public void ChangePopWindowBeizhu( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String TabName=request.getParameter("tabname");
        String ywname=request.getParameter("ywname");
        String Value=request.getParameter("value");
        dao.ChangePopWindowBeizhu(TabName,ywname,Value);
    }
    public void DeleteOneHistory( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String TabId=request.getParameter("tabid");
        String TabName=request.getParameter("tabname");
        dao.DeleteOnHistory(TabId,TabName);
    }
    public void ChangeTabName( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String TabName=request.getParameter("tabname");
        String NewName=request.getParameter("NewName");
        dao.ChangeTabName(TabName,NewName);
    }
    public void OutData( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String TName=request.getParameter("tabname");
        dao.OutData(TName);
    }
    public void DeleteOneZiDuan( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String TName=request.getParameter("tabname");
        String YWName=request.getParameter("ywname");
        dao.DeleteOneZiDuan(TName,YWName);
    }
    public void getDBConAndData( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        String IP=request.getParameter("IP");
        String Name=request.getParameter("Name");
        String Password=request.getParameter("Password");
        String Port=request.getParameter("Port");
        String DBName=request.getParameter("DBName");
        DBManage dbManage=new DBManage(IP,Name,Password,Port,DBName);
        PrintWriter out = response.getWriter();
        System.out.println("结果:"+dbManage.TCon());
        if(!dbManage.TCon()){
            out.write("false");
        }
        else {
            JSONArray BigArray=new JSONArray();
            List<String> TabNames = dbManage.getTableNames();
            for (int i = 0; i < TabNames.size(); i++) {
                JSONObject ob=new JSONObject();
                ob.put("title",TabNames.get(i));
                JSONArray ClArray=new JSONArray();
                List<String> CName=dbManage.getColumnNames(TabNames.get(i));
                for(int j=0;j<CName.size();j++){
                    JSONObject jsonOb=new JSONObject();
                    jsonOb.put("title",CName.get(j));
                    ClArray.add(jsonOb);
                }
                ob.put("children",ClArray);
                BigArray.add(ob);
            }
            System.out.println("String:"+BigArray.toString());
            out.write(BigArray.toString());
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String method=request.getParameter("method");
        if(method.equals("tecon")){
            try {
                tecon(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("getDBConAndData")){
            try {
                getDBConAndData(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("DeleteOneZiDuan")){
            try {
                DeleteOneZiDuan(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("OutData")){
            try {
                OutData(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("Ttable")){
            try {
                Ttable(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("login")){
            try {
                login(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("HisTab")){
            try {
                HisTab(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("PopTable")){
            try {
                PopTable(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("ChangePopWindowBeizhu")){
            try {
                ChangePopWindowBeizhu(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("DeleteOneHistory")){
            try {
                DeleteOneHistory(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("ChangeTabName")){
            try {
                ChangeTabName(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
