package guide.app.udassetrun;

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

    @Override
    public void action() throws RemoteException, MXException {
        super.action();
        Mbo mbo = getMboValue().getMbo();
//        设备编码 == 系统字段
        if (mbo!=null){
            mbo.setValue("UDXTZD",mbo.getString("ASSETNUM"),11L);
        }
    }
//    public void action() throws MXException, RemoteException
//    {
//        super.action();
////        System.out.println("--------assetnum004-------");
//
//        Mbo mbo = getMboValue().getMbo();
//
//        //设备编码 == 系统字段
//        if (mbo!=null){
//            mbo.setValue("UDXTZD",mbo.getString("ASSETNUM"),11L);
//        }
//
//        double gztszy = 0.0D;
//        double jxzyld = 0.0D;
//        double JXZYL = 0.0D;
//
//        MboRemote Owner = mbo.getOwner();
//        if (Owner != null) {
//            MboSetRemote otherSet = mbo.getMboSet("$UDASSETRUNLINE", "UDASSETRUNLINE"
//                    , "siteid='" + mbo.getString("siteid")
//                            + "' and assetnum !='" + mbo.getString("assetnum")
//                            + "' and udassetrunlineid !='" + mbo.getInt("udassetrunlineid")
//                            + "' "
//                            + "and udassetrunid in(select udassetrunid from udassetrun where to_date(udyear,'yyyy')=to_date(" + Owner.getString("udyear")
//                            + ",'yyyy'))");
//            System.out.println("---------"+otherSet.count());
//            if ((!otherSet.isEmpty()) && (otherSet.count() > 0)) {
//                gztszy = otherSet.sum("gztszy");
//                jxzyld = otherSet.sum("jxzyld");
//                JXZYL = otherSet.sum("JXZYL");
//                System.out.println("###########");
//            }
//        }
//
//        mbo.setValue("udcumulativerunningtime", gztszy + mbo.getDouble("gztsxj"), 11L);
//        mbo.setValue("udcumulativeoperatingtons", jxzyld + mbo.getDouble("jxzyld"), 11L);
//        mbo.setValue("UDLJXL",JXZYL + mbo.getDouble("JXZYL"),11L);
//    }

    public MboSetRemote getList() throws MXException, RemoteException
    {
        MboSetRemote sourceMboSet = super.getList();
        MboRemote localMbo = getMboValue().getMbo();
        MboRemote ownerMbo = localMbo.getOwner();
        String where = "siteid = '" + localMbo.getString("siteid") + "' ";
        System.out.println("--------assetnum001-------" + ownerMbo.getName());
        if (ownerMbo != null) {
            System.out.println("--------assetnum002-------");
            String udapptype = ownerMbo.getString("apptype");
            if ((udapptype != null) && (udapptype.equals("ASSETRUNM")))
                where = where + " and apptype ='101' ";
            else if ((udapptype != null) && (udapptype.equals("ASSETRUNS"))) {
                where = where + " and apptype ='CBTZ' ";
            }
        }
        System.out.println("--------assetnum003-------");
        sourceMboSet.setWhere(where);
        sourceMboSet.reset();
        return sourceMboSet;
    }
}