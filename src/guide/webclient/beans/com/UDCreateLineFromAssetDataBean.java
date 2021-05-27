package guide.webclient.beans.com;

import java.rmi.RemoteException;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;
import psdi.webclient.system.controller.AppInstance;
import psdi.webclient.system.session.WebClientSession;

public class UDCreateLineFromAssetDataBean extends UDExcelimpDataBean
{
    public int udcreSbtzybmx()
            throws RemoteException, MXException
    {
        MboRemote mbo = this.app.getAppBean().getMbo();
        MboSetRemote childSet = mbo.getMboSet("UDSBTZYBMX");
        if ((!childSet.isEmpty()) && (childSet.count() > 0)) {
            Object[] params = { "已有月报明细数据，请删除明细数据后再进行此操作！" };
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }
        MboSetRemote assetSet = mbo.getMboSet("CREASSET");
        if ((!assetSet.isEmpty()) && (assetSet.count() > 0)) {
            MboRemote asset = null;
            MboRemote child = null;
            for (int i = 0; (asset = assetSet.getMbo(i)) != null; i++) {
                child = childSet.add();
                child.setValue("udsbtzybid", mbo.getInt("udsbtzybid"), 2L);
                child.setValue("assetnum", asset.getString("assetnum"), 2L);
            }
            this.app.getAppBean().save();
            this.clientSession.showMessageBox(this.clientSession.getCurrentEvent(), "提示", "月报明细数据已生成！", 1);
        } else {
            this.clientSession.showMessageBox(this.clientSession.getCurrentEvent(), "提示", "没有满足生成条件的设备！", 1);
        }

        return 1;
    }

    public int udcreAssetrunline() throws RemoteException, MXException {
        MboRemote mbo = this.app.getAppBean().getMbo();
        MboSetRemote childSet = mbo.getMboSet("UDASSETRUNLINE");
        if ((!childSet.isEmpty()) && (childSet.count() > 0)) {
            Object[] params = { "已有月报明细数据，请删除明细数据后再进行此操作！" };
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }
        MboSetRemote assetSet = mbo.getMboSet("CREASSET");
        if ((!assetSet.isEmpty()) && (assetSet.count() > 0)) {
            MboRemote asset = null;
            MboRemote child = null;
            for (int i = 0; (asset = assetSet.getMbo(i)) != null; i++) {
                child = childSet.add();
                child.setValue("udassetrunid", mbo.getInt("udassetrunid"), 2L);
                child.setValue("assetnum", asset.getString("assetnum"), 2L);
            }
            this.app.getAppBean().save();
            this.clientSession.showMessageBox(this.clientSession.getCurrentEvent(), "提示", "月报明细数据已生成！", 1);
        } else {
            this.clientSession.showMessageBox(this.clientSession.getCurrentEvent(), "提示", "没有满足生成条件的设备！", 1);
        }

        return 1;
    }

    public int udcreAssetrundline() throws RemoteException, MXException {
        MboRemote mbo = this.app.getAppBean().getMbo();
        MboSetRemote childSet = mbo.getMboSet("UDASSETRUNLINE");
        if ((!childSet.isEmpty()) && (childSet.count() > 0)) {
            Object[] params = { "已有日报明细数据，请删除明细数据后再进行此操作！" };
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }
        MboSetRemote assetSet = mbo.getMboSet("CREASSET");
        if ((!assetSet.isEmpty()) && (assetSet.count() > 0)) {
            MboRemote asset = null;
            MboRemote child = null;
            for (int i = 0; (asset = assetSet.getMbo(i)) != null; i++) {
                child = childSet.add();
                child.setValue("udassetrunid", mbo.getInt("udassetrunid"), 2L);
                child.setValue("assetnum", asset.getString("assetnum"), 2L);
            }
            this.app.getAppBean().save();
            this.clientSession.showMessageBox(this.clientSession.getCurrentEvent(), "提示", "日报明细数据已生成！", 1);
        } else {
            this.clientSession.showMessageBox(this.clientSession.getCurrentEvent(), "提示", "没有满足生成条件的设备！", 1);
        }

        return 1;
    }
}