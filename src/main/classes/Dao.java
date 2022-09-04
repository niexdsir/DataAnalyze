import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Dao {
    public String login(int Id, String Password) throws SQLException {
        String sql = "select * from user ";
        Connection conn = Dbutil.getConnection();
        Statement st = null;
        List<UserBean> list = new ArrayList<>();
        UserBean bean = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            st.executeQuery(sql);
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String pass = rs.getString("password");
                String type = rs.getString("type");
                bean = new UserBean(id, pass, type);
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Dbutil.close(st, conn);
        }
        for(int i=0;i<list.size();i++){
            if(list.get(i).getId()==Id&&list.get(i).getPassword().equals(Password)){
                return list.get(i).getType();
            }
        }
        return "登录错误";
    }
    public void CreataTab(String TableName) throws SQLException {
        String sql="create table "+TableName+" (ywname varchar(40),zwname varchar(40),danwei varchar(10),zdtype varchar(10),beizhu varchar(50),ispri varchar(2),crtime varchar(20),chtime varchar(20),user varchar(20))";
        Connection conn=Dbutil.getConnection();
        PreparedStatement pt = conn.prepareStatement(sql);
        Statement st=null;
        ResultSet rs=null;
        try
        {
            st=conn.createStatement();
            st.executeUpdate(sql);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Dbutil.close(rs, st, conn);
        }
    }
    public void FirInputData(List<List<Object>> rowlist,String TableName) throws SQLException, InterruptedException {
        int len=rowlist.get(0).size();
        Utils utils=new Utils();
        String sql="";
        Connection conn=null;
        conn=Dbutil.getConnection();
        Statement st=null;
        ResultSet rs=null;
        String danwei="";
        String type="";
        String ispri="是";
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String crtime=formatter.format(date);
        for(int i=0;i<len;i++){
            if(rowlist.get(1).get(i).equals("")){
                danwei="无";
            }
            else danwei=(String)rowlist.get(1).get(i);
            if(utils.isInteger((String)rowlist.get(3).get(i))){
                type="Int";
            }
            else if(utils.isDouble((String)rowlist.get(3).get(i))){
                type="float";
            }
            else if(utils.isValidDate((String)rowlist.get(3).get(i))){
                type="datatime";
            }
            else type="varchar";
            sql="insert into "+TableName+" values ('"+rowlist.get(2).get(i)+"','"+rowlist.get(0).get(i)+"','"+danwei+"','"+type+"','"+"无"+"','"+ispri+"','"+crtime+"','"+crtime+"','"+"user"+"')";


            try
            {
                st=conn.createStatement();
                st.executeUpdate(sql);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {



            }
            if(ispri.equals("是"))ispri="否";
        }
        Dbutil.close(rs, st, conn);


    }
    public List<SqlTableBean> getTableAll(String TableName){
        String sql="select * from  "+TableName+"";
        Connection conn =Dbutil.getConnection();
        Statement st=null;
        List<SqlTableBean> list=new ArrayList<>();
        ResultSet rs=null;
        SqlTableBean bean=null;
        try {
            st=conn.createStatement();
            st.executeQuery(sql);
            rs=st.executeQuery(sql);
            while(rs.next()) {
               String ywname=rs.getString("ywname");
               String zwname=rs.getString("zwname");
               String danwei=rs.getString("danwei");
               String zdtype=rs.getString("zdtype");
               String beizhu=rs.getString("beizhu");
               String ispri=rs.getString("ispri");
               String crtime=rs.getString("crtime");
               String chtime=rs.getString("chtime");
               String user=rs.getString("user");
               bean =new SqlTableBean(ywname,zwname,danwei,zdtype,beizhu,ispri,crtime,chtime,user);
               list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
        return list;
    }
    public void FirInsertToHis(String TableName,String user) throws SQLException {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String crtime=formatter.format(date);
        String sql="insert into hisdata (tabname,crtime,user) values('"+TableName+"','"+crtime+"','"+user+"')";
        Connection conn=Dbutil.getConnection();
        PreparedStatement pt = conn.prepareStatement(sql);
        Statement st=null;
        ResultSet rs=null;
        try
        {
            st=conn.createStatement();
            st.executeUpdate(sql);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Dbutil.close(rs, st, conn);
        }
    }
    public List<HisTabBean> HisTab(){
        String sql="select * from hisdata";
        Connection conn =Dbutil.getConnection();
        Statement st=null;
        List<HisTabBean> list=new ArrayList<>();
        ResultSet rs=null;
        HisTabBean bean=null;
        try {
            st=conn.createStatement();
            st.executeQuery(sql);
            rs=st.executeQuery(sql);
            while(rs.next()) {
                int id=rs.getInt("tabid");
                String tabname=rs.getString("tabname");
                String crtime=rs.getString("crtime");
                int user=rs.getInt("user");
                bean =new HisTabBean(id,tabname,crtime,user);
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
        return list;
    }
    public void ChangePopWindowBeizhu(String TabName,String ywname,String Value){
        String sql="update "+TabName+" set beizhu='"+Value+"' where ywname='"+ywname+"'";
        Connection conn =Dbutil.getConnection();
        Statement st=null;
        try {
            st=conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String chtime=formatter.format(date);
        sql="update "+TabName+" set chtime='"+chtime+"' where ywname='"+ywname+"'";
        conn =Dbutil.getConnection();
        st=null;
        try {
            st=conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
    }
    public void DeleteOnHistory(String TabId,String Tabname){
        String sql="drop table "+Tabname;
        Connection conn =Dbutil.getConnection();
        Statement st=null;
        try {
            st=conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
        sql="delete from hisdata where tabid ="+TabId;
        conn =Dbutil.getConnection();
        st=null;
        try {
            st=conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
    }
    public void ChangeTabName(String TabName,String NewName){
        String sql="alter table "+TabName+" rename to "+NewName;
        Connection conn =Dbutil.getConnection();
        Statement st=null;
        try {
            st=conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
        sql="update hisdata set tabname = '"+NewName+"' where tabname ='"+TabName+"'";
        conn =Dbutil.getConnection();
        st=null;
        try {
            st=conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
    }
    public void OutData(String TName){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String OutTime=formatter.format(date);
        String OutName=OutTime;
        String sql="select *  into outfile 'D:/mysqlexport/"+OutName+".csv' fields terminated by ',' lines terminated by '\r\n' from(select '英文名称','中文名称','单位','字段类型','备注','主键' union select ywname,zwname,danwei,zdtype,beizhu,ispri from "+TName+")b";
        Connection conn =Dbutil.getConnection();
        Statement st=null;
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch(SQLException se) {
            se.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(rs!=null)
                    rs.close();
                if(ps!=null)
                    ps.close();
                if(conn!=null)
                    conn.close();
            }catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }
    public void DeleteOneZiDuan(String TName,String YWName){
        String sql="delete from "+TName+" where ywname = '"+YWName+"'";
        Connection conn =Dbutil.getConnection();
        Statement st=null;
        try {
            st=conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            Dbutil.close(st, conn);
        }
    }
}
