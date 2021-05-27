package guide.app.udsbtzyb;

import java.rmi.RemoteException;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;


public class UdFldMxStatus extends MboValueAdapter {

    public UdFldMxStatus(MboValue mbovalue) {
        super(mbovalue);
    }


    public void initValue() throws MXException, RemoteException {
        super.initValue();

        MboRemote mbo = this.getMboValue().getMbo();

        MboSetRemote udmxstatus = mbo.getMboSet("udsbtzybmx");
        if ((!udmxstatus.isEmpty())&&(udmxstatus.count() > 0)) {
            mbo.setValue("udmxstatus", "有明细",11L);
        }else{
            mbo.setValue("udmxstatus", "无明细",11L);
        }

    }

}