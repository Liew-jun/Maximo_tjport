package guide.app.udassetrun;

import psdi.mbo.*;
import psdi.util.MXException;

import java.rmi.RemoteException;

    public class FldAssetrunSumLj extends MboValueAdapter {
    public FldAssetrunSumLj(MboValue mbv) {
        super(mbv);
    }

    @Override
    public void initValue() throws MXException, RemoteException {
        super.initValue();
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

    }
}
