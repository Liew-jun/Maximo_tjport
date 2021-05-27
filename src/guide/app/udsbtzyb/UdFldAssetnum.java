package guide.app.udsbtzyb;

import guide.app.asset.FldComAssetnum;
import java.io.PrintStream;
import java.rmi.RemoteException;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.util.MXException;

public class UdFldAssetnum extends FldComAssetnum
{
    public UdFldAssetnum(MboValue mbv)
            throws MXException, RemoteException
    {
        super(mbv);
    }

    public void action() throws MXException, RemoteException
    {
        super.action();

        Mbo mbo = getMboValue().getMbo();
        double gztsxj = 0.0D;
        double jxzyld = 0.0D;

        MboRemote Owner = mbo.getOwner();
        if (Owner != null) {
            MboSetRemote otherSet = mbo.getMboSet("UDSBTZYBMX", "UDSBTZYBMX", "siteid='" + mbo.getString("siteid") + "' and assetnum='" + mbo.getString("assetnum") + "' and udsbtzybmxid!='" + mbo.getInt("udsbtzybmxid") + "' " + "and udsbtzybid in(select udsbtzybid from udsbtzyb where to_date(udyear||'-'||lpad(udmonth,2,0),'yyyy-mm')<to_date(" + Owner.getString("udyear") + "||'-'||lpad(" + Owner.getString("udmonth") + ",2,0),'yyyy-mm'))");

            if ((!otherSet.isEmpty()) && (otherSet.count() > 0)) {
                gztsxj = otherSet.sum("gztsxj");
                jxzyld = otherSet.sum("jxzyld");
                System.out.println("gztsxj a = " + gztsxj);
                System.out.println("jxzyld a = " + jxzyld);
            }
        }
        mbo.setValue("udcumulativerunningtime", gztsxj + mbo.getDouble("gztsxj"), 11L);
        mbo.setValue("udcumulativeoperatingtons", jxzyld + mbo.getDouble("jxzyld"), 11L);
    }

    public MboSetRemote getList() throws MXException, RemoteException
    {
        MboSetRemote sourceMboSet = super.getList();
        MboRemote localMbo = getMboValue().getMbo();
        MboRemote ownerMbo = localMbo.getOwner();
        String where = "siteid = '" + localMbo.getString("siteid") + "' ";
        if (ownerMbo != null) {
            String udapptype = ownerMbo.getString("apptype");
            if ((udapptype != null) && (udapptype.equals("udsbtzyb")))
                where = where + " and apptype ='SBTZ' ";
            else if ((udapptype != null) && (udapptype.equals("udsbtzyb"))) {
                where = where + " and apptype ='CBTZ' ";
            }
        }
        sourceMboSet.setWhere(where);
        sourceMboSet.reset();
        return sourceMboSet;
    }
}