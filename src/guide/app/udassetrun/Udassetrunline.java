package guide.app.udassetrun;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.security.UserInfo;
import psdi.server.MXServer;
import psdi.util.MXException;

public class Udassetrunline extends Mbo
        implements MboRemote
{
    boolean isModified = false;
    private DecimalFormat df = new DecimalFormat("0.00");

    public Udassetrunline(MboSet ms) throws RemoteException {
        super(ms);
    }

    public void add() throws MXException, RemoteException {
        super.add();
        int linenum = (int)getThisMboSet().max("linenum") + 1;
        setValue("LINENUM", linenum, 2L);
        MboRemote owner = getOwner();
        if (owner != null) {
            if ("UDASSETRUN".equalsIgnoreCase(owner.getName())) {
                setValue("UDASSETRUNID", owner.getUniqueIDValue(), 11L);
            } else if ("ASSET".equalsIgnoreCase(owner.getName())) {
                    setValue("ASSETNUM", owner.getString("ASSETNUM"), 11L);
                }
        }
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        try {
            setValue("RLTS",getDaysOfMonth(format.parse(accDate))*24,11L);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /***
     *
     * @param date
     * @return 传入进来年月的一个天数
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public void modify() throws MXException, RemoteException
    {
        MboRemote owner = getOwner();
        if (!this.isModified) {
            this.isModified = true;
            if ((owner != null) && (!"ASSET".equalsIgnoreCase(owner.getName()))) {
                owner.setValue("changeby", getUserInfo().getUserName(), 11L);
                owner.setValue("changedate", MXServer.getMXServer().getDate(), 11L);
            }
        }
        if ((owner != null) && (owner.getName().equalsIgnoreCase("UDASSETRUN"))) {
            String[] attrStrings = { "gcpc", "guzh", "gzgzts",
                    "gztszy", "jxzyl", "jxzyld", "qmsyts", "rlts", "sl", "whts", "yyts" };
            for (int i = 0; i < attrStrings.length; i++)
                if (isModified(attrStrings[i]))
                    ((UDAssetRun)owner).setSumValue(getThisMboSet(), attrStrings[i]);
        }
    }
    //	设备利用率 = 设备使用台时/日历台时
    public void setLyl()
            throws RemoteException, MXException
    {
        if ((!isNull("GZTSZY")) && (!isNull("RLTS")) && (getDouble("RLTS") > 0.0D))
            setValue("Lyl", this.df.format(getDouble("GZTSZY") / getDouble("RLTS") * 100.0D), 11L);
        else
            setValue("Lyl", 0.0D, 11L);
    }
    //	设备可利用率 = 设备使用台时/（设备使用台时+设备故障台时+设备保养台时）
    public void setWhlForAsset() throws RemoteException, MXException {
        if ((!isNull("GZTSZY")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Whl", this.df.format( getDouble("GZTSZY")/(getDouble("GZTSZY") + getDouble("WHTS") +getDouble("GZGZTS")) * 100.0D), 11L);
        else
            setValue("Whl", 0.0D, 11L);
    }

    public void setWhlForShip() throws RemoteException, MXException {
        if ((!isNull("YYTS")) && (!isNull("WHTS")) && (!isNull("RLTS")) && (getDouble("RLTS") > 0.0D))
            setValue("Whl", this.df.format((getDouble("RLTS") - getDouble("WHTS") - getDouble("YYTS")) / getDouble("RLTS") * 100.0D), 11L);
        else
            setValue("Whl", 0.0D, 11L);
    }

    public void setWxcb() throws RemoteException, MXException {
        if ((!isNull("GZTSZY")) && (!isNull("GUZH")) && (getDouble("GUZH") > 0.0D))
            setValue("Wxcb", getDouble("GZTSZY") / getDouble("GUZH"), 11L);
        else
            setValue("Wxcb", 0.0D, 11L);
    }
    //	设备完好率 = （日历台时 - 故障台时 - 计划保养台时）/日历台时
    public void setGzpcForAsset() throws RemoteException, MXException {
        if (!isNull("RLTS") && (getDouble("RLTS") > 0.0D))
            setValue("Gzpc", this.df.format((getDouble("RLTS") - getDouble("GZGZTS")- getDouble("WHTS")) / getDouble("RLTS") * 100.0D), 11L);
        else
            setValue("Gzpc", 0.0D, 11L);
    }

    public void setGzpcForShip() throws RemoteException, MXException {
        if ((!isNull("GZTSZY")) && (!isNull("GZGZTS")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Gzpc", this.df.format((getDouble("GZTSZY") - getDouble("GZGZTS")) / getDouble("GZTSZY") * 100.0D), 11L);
        else
            setValue("Gzpc", 0.0D, 11L);
    }

    public void setTscl() throws RemoteException, MXException {
        if ((!isNull("JXZYL")) && (!isNull("GZTSZY")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Tscl", getDouble("JXZYL") / getDouble("GZTSZY"), 11L);
        else
            setValue("Tscl", 0.0D, 11L);
    }

    public void setTscldun() throws RemoteException, MXException {
        if ((!isNull("JXZYLD")) && (!isNull("GZTSZY")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Tscldun", getDouble("JXZYLD") / getDouble("GZTSZY"), 11L);
        else
            setValue("Tscldun", 0.0D, 11L);
    }
}