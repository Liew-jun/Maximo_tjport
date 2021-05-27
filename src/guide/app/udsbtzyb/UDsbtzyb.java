package guide.app.udsbtzyb;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.server.MXServer;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class UDsbtzyb extends Mbo
        implements MboRemote
{
    public UDsbtzyb(MboSet ms)
            throws MXException, RemoteException, RemoteException
    {
        super(ms);
    }

    public void init()
            throws MXException
    {
        super.init();
        try {
            MboSetRemote udassetrunline = getMboSet("UDSBTZYBMX");

            if ((!udassetrunline.isEmpty()) && (udassetrunline.count() > 0))
            {
                if (getString("STATUS").equals("待提报")) {
                    String name = udassetrunline.getName();
                    System.out.println("name = " + name);
                    udassetrunline.setFlags(7L);
                }
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void add() throws MXException, RemoteException
    {
        super.add();

        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        c.add(2, -1);

        String year = Integer.toString(c.get(1));
        Integer month = Integer.parseInt(Integer.toString(c.get(2) + 1));
        DecimalFormat df=new DecimalFormat("00");               //设置月份格式
        setValue("UDYEAR", year, 11L);
        setValue("UDMONTH", df.format(month), 11L);

        System.out.println("year = " + year);
        System.out.println("month = " + month);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        setValue("ymdate", sdf.format(MXServer.getMXServer().getDate()), 11L);

        System.out.println("\nadd===================================");
    }

    public void appValidate()
            throws MXException, RemoteException
    {
        super.appValidate();
        MboSetRemote otherSet = getMboSet("OTHERS");
        if ((!otherSet.isEmpty()) && (otherSet.count() > 0)) {
            Object[] params = { getString("SITEID.UDORGJC") + "该周期数据已存在！" };
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }
        System.out.println("\nappValidate===================================");
    }

    public void save() throws MXException, RemoteException
    {
        super.save();
        System.out.println("\nsave===================================");
    }
}