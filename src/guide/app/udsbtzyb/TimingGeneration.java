package guide.app.udsbtzyb;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.server.MXServer;
import psdi.server.SimpleCronTask;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.controls.PortletDataInstance;
import psdi.webclient.system.session.WebClientSession;

import java.rmi.RemoteException;

public class TimingGeneration extends SimpleCronTask {

    MboSetRemote childSet = null;
    MboSetRemote assetSet = null;
    MboRemote mbo = null;
    @Override
    public void cronAction() {
        try {
            childSet = MXServer.getMXServer().getMboSet("UDSBTZYBMX",this.getRunasUserInfo());
            if ((!childSet.isEmpty()) && (childSet.count() > 0)) {
                Object[] params = { "已有月报明细数据，请删除明细数据后再进行此操作！" };
                throw new MXApplicationException("instantmessaging", "tsdimexception", params);
            }

            assetSet = MXServer.getMXServer().getMboSet("CREASSET",this.getRunasUserInfo());
            if ((!assetSet.isEmpty()) && (assetSet.count() > 0)) {
                MboRemote asset = null;
                MboRemote child = null;
                for (int i = 0; (asset = assetSet.getMbo(i)) != null; i++) {
                    child = childSet.add();
                    child.setValue("udsbtzybid", mbo.getInt("udsbtzybid"), 2L);
                    child.setValue("assetnum", asset.getString("assetnum"), 2L);
                }
            }
            childSet.save();
            childSet.close();
        } catch (MXException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
