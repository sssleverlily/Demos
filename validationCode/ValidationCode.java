import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@WebServlet(name = "ValidationCode")
public class ValidationCode extends HttpServlet {
   //图形验证码集合，系统会从字符串中自动选择一些字符作为验证码
    private static String codeChars="1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //返回一个随机颜色
    private static Color getRandomColor(int minColor,int maxColor)
    {
        Random random=new Random();
        if(minColor>255)
            minColor=255;
        if(maxColor>255)
            maxColor=255;
        int red=minColor+random.nextInt(maxColor-minColor);
        int green=minColor+random.nextInt(maxColor-minColor);
        int blue=minColor+random.nextInt(maxColor-minColor);
        return new Color(red,blue,green);
    }
    protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException
    {
        int charsLength=codeChars.length();
        //下面三条语句都可以关闭浏览器的缓冲区，由于浏览器的版本不同，对这三条语句的支持也不同
        response.setHeader("ragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.addDateHeader("Expires",0);
        //设置图形的大小
        int width=90,height=20;
        BufferedImage bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //获得用于输出文字的Graphics对象
        Graphics g=bufferedImage.getGraphics();
        Random random=new Random();
        //随即设置字体颜色
        g.setColor(getRandomColor(120,180));
        //保存最后生成的验证码
        StringBuilder validationCode =new StringBuilder();
        //验证码的随机字体
        String[]fontNames={"Times New Roman","Book antiqua","Arial"};
        //随机生成4个验证码啊
        for(int i=0;i<4;i++){
            //随机设置当前验证码的字体
            g.setFont(new Font(fontNames[random.nextInt(3)],Font.ITALIC,height));
        //随机获取当前验证码的字符
            char codeChar=codeChars.charAt(random.nextInt(charsLength));
            validationCode.append(codeChar);
            //随机设置当前验证码的颜色
            g.setColor(getRandomColor(10,100));
            //在图形上输出验证码字符，xy都是随机生成的
            g.drawString(String.valueOf(codeChar),16*i+random.nextInt(7),height-random.nextInt(6));

        }
        //获得HttpSession对象
        HttpSession session=request.getSession();
        session.setMaxInactiveInterval(1*60);//设置session对象一分钟后失效
        //将验证码保存在session中，key为validation_code
        session.setAttribute("validation_code",validationCode.toString());
        g.dispose();//关闭该对象
        OutputStream os=response.getOutputStream();
        ImageIO.write(bufferedImage,"JPEG",os);//以JPEG的格式向客户端发起验证码



    }
}
