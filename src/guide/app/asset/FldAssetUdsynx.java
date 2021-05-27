package guide.app.asset;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FldAssetUdsynx extends MboValueAdapter {
    public FldAssetUdsynx(MboValue mbv) {
        super(mbv);
    }

    @Override
    public void initValue() throws MXException, RemoteException {
        super.initValue();
        MboRemote mbo = getMboValue().getMbo();
        if (mbo != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            if ((new Date().getTime() > mbo.getDate("INSTALLDATE").getTime())) {
                String format2 = sdf.format(new Date());                                //获取系统当前日
                String format1 = sdf.format(mbo.getDate("INSTALLDATE"));            //获取启用日期
                try {
                    Date d1 = sdf.parse(format1);
                    Date d2 = sdf.parse(format2);
                    long daysBetween = (d2.getTime() - d1.getTime() + 1000000) / (60 * 60 * 24 * 1000);
                    mbo.setValue("UDNXSY", Math.round(daysBetween / 365), 11L);
                    System.out.println("-------------------" + Math.round(daysBetween / 365));
                    System.out.println("-------------------daysBetween = " + daysBetween);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                mbo.setValue("UDNXSY", 0, 11L);
            }
        }
    }
}

