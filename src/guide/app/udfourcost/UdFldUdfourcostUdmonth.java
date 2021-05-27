package guide.app.udfourcost;

import psdi.mbo.Mbo;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

import java.rmi.RemoteException;
import java.text.DecimalFormat;

import static psdi.workflow.util.Resources.getString;

public class UdFldUdfourcostUdmonth extends MboValueAdapter {
    public UdFldUdfourcostUdmonth(MboValue mbv) {
        super(mbv);
    }

    @Override
    public void action() throws MXException, RemoteException {
        super.action();
        Mbo mbo = getMboValue().getMbo();
        int udmonth = getMboValue("UDMONTH").getInt();
        if ((udmonth>0)&&(udmonth<=12)){
            DecimalFormat df=new DecimalFormat("00");
            mbo.setValue("UDMONTH",df.format(udmonth),11L);
        }else {
            Object[] params = { getString("SITE.UDORGJC") + "该周期数据的月份填写错误！" };
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }
    }
}
