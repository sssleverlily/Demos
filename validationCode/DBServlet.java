import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@javax.servlet.annotation.WebServlet(name = "DBServlet")
public class DBServlet extends javax.servlet.http.HttpServlet {
    protected Connection conn=null;
    protected ResultSet execSQL(String sql,Object...args)throws  Exception
    {
        PreparedStatement pStmt=conn.prepareStatement(sql);
        //为pStmt对象设置sql参数值
        for(int i=0;i<args.length;i++){
            pStmt.setObject(i+1,args[i]);
        }
        pStmt.execute();
        //返回结果集
        return pStmt.getResultSet();
    }
    //核对用户输入的验证码是否合法
    protected boolean checkValidactionCode(HttpServletRequest request,String validationCode)
    {
        //从HttpSession对象中获得系统随机生成的验证码
        String validationCodeSession =(String)request.getSession().getAttribute("validation_code");
        if(validationCodeSession==null){
            request.setAttribute("info","验证码过期,请重新刷新界面");
            request.setAttribute("codeError","验证码过期，请重新刷新界面");
            return false;
        }
        //将用户输入的验证码和系统生成的进行比较
        if(!validationCode.equalsIgnoreCase(validationCodeSession))
        {
            request.setAttribute("info","验证码输入不正确");
            request.setAttribute("codeError","验证码输入不正确");
            return false;
        }
        return true;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException
    {
        try {
            if(conn==null){
                javax.naming.Context context=new javax.naming.InitialContext();
                //获取数据源
                javax.sql.DataSource ds=(javax.sql.DataSource)context.lookup("");
                conn=ds.getConnection();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy(){
        try{
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
