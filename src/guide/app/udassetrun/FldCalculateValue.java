package guide.app.udassetrun;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FldCalculateValue extends MboValueAdapter {
    public FldCalculateValue(MboValue mbv) {
        super(mbv);
    }

    public void action() throws MXException, RemoteException {
        super.action();
        Mbo mbo1 = getMboValue().getMbo();
        double gztszy = 0.0D;
        double jxzyld = 0.0D;
        double JXZYL = 0.0D;

        MboRemote Owner = mbo1.getOwner();
        if (Owner != null) {
            MboSetRemote otherSet = mbo1.getMboSet("$UDASSETRUNLINE", "UDASSETRUNLINE"
                    , "siteid='" + mbo1.getString("siteid")
//                            + "' and assetnum !='" + mbo1.getString("assetnum")
                            + "' and udassetrunlineid !='" + mbo1.getInt("udassetrunlineid")
                            + "' "
                            + "and udassetrunid in(select udassetrunid from udassetrun where to_date(udyear,'yyyy')=to_date(" + Owner.getString("udyear")
                            + ",'yyyy'))");

            if ((!otherSet.isEmpty()) && (otherSet.count() > 0)) {
                gztszy = otherSet.sum("gztszy");
                jxzyld = otherSet.sum("jxzyld");
                JXZYL = otherSet.sum("JXZYL");

            }
        }

        mbo1.setValue("UDXNLJTS", gztszy + mbo1.getDouble("gztszy"), 11L);
        mbo1.setValue("UDXNLJCZD", jxzyld + mbo1.getDouble("jxzyld"), 11L);
        mbo1.setValue("UDXNLJXL", JXZYL + mbo1.getDouble("JXZYL"), 11L);

        MboValue availBalFldValue = getMboValue();

        if (availBalFldValue.isNull())
            return;
        String attributeName = availBalFldValue.getAttributeName();
        Udassetrunline mbo = (Udassetrunline) availBalFldValue.getMbo();

        if (mbo != null) {
            String apptype = "";
            MboRemote owner = mbo.getOwner();
            if (owner != null)
                apptype = owner.getString("apptype");
            if ("RLTS".equalsIgnoreCase(attributeName)) {
                mbo.setLyl();
                mbo.setGzpcForAsset();
            } else if ("WHTS".equalsIgnoreCase(attributeName)) {
                mbo.setWhlForAsset();
                mbo.setGzpcForAsset();
            } else if ("YYTS".equalsIgnoreCase(attributeName)) {
                mbo.setWhlForShip();
            } else if ("GUZH".equalsIgnoreCase(attributeName)) {
                mbo.setWxcb();
            } else if ("GZTSXJ".equalsIgnoreCase(attributeName)) {
                if ("ASSETRUNS".equalsIgnoreCase(apptype))
                    mbo.setGzpcForShip();
            } else if ("GZGZTS".equalsIgnoreCase(attributeName)) {
                mbo.setWhlForAsset();
                mbo.setGzpcForAsset();
            } else if ("GZTSZY".equalsIgnoreCase(attributeName)) {
                mbo.setWxcb();
                mbo.setLyl();
                mbo.setWhlForAsset();
                mbo.setTscl();
                mbo.setTscldun();
                if ("ASSETRUNM".equalsIgnoreCase(apptype))
                    mbo.setWhlForAsset();
            } else if ("JXZYL".equalsIgnoreCase(attributeName)) {
                mbo.setTscl();
            } else if ("JXZYLD".equalsIgnoreCase(attributeName)) {
                mbo.setTscldun();
            }
        }
    }
}