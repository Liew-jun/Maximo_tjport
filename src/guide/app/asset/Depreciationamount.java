package guide.app.asset;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import psdi.server.SimpleCronTask;
import psdi.util.MXException;

public class Depreciationamount extends SimpleCronTask
{
    public Depreciationamount()
            throws RemoteException, MXException
    {
    }

    public void cronAction()
    {
        PreparedStatement pstm = null;
        Connection conn = null;
        try {
            try {
                conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.180:1521:tjport", "tjport ", "maximo");
            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
            String sql = " update asset set  udyzjje = yz * udzjl + udyzjje where apptype = 'SBTZ' and udyzjje > 0 and udzjl > 0 ";
            pstm = conn.prepareStatement(sql);
            pstm.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstm != null)
                pstm.close();
        }
        catch (SQLException e2) {
            e2.printStackTrace();

            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
        }
        finally
        {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
        }
    }
}