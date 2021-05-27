package guide.app.asset;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import psdi.mbo.Mbo;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class UdFldUdjyzq extends MboValueAdapter
{
    public UdFldUdjyzq(MboValue mbv)
    {
        super(mbv);
    }
    public void action() throws MXException, RemoteException {
        super.action();
        Mbo mbo = getMboValue().getMbo();
        Date UDJYLASTDATE = mbo.getDate("UDJYLASTDATE");
        int UDJYZQ = mbo.getInt("UDJYZQ");
//        System.out.println("n/" + UDJYZQ);
        if ((UDJYLASTDATE != null) && (UDJYZQ > 0))
        {
            Calendar c = Calendar.getInstance();
            c.setTime(UDJYLASTDATE);
            c.add(2, UDJYZQ);
            Date UDJYNEXTDATE = c.getTime();
            mbo.setValue("UDJYNEXTDATE", UDJYNEXTDATE, 11L);
//            System.out.println("n/" + UDJYLASTDATE);
        }
    }
}