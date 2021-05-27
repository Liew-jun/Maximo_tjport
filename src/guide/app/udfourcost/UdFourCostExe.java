package guide.app.udfourcost;

import guide.util.Tools;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.security.UserInfo;
import psdi.server.MXServer;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class UdFourCostExe extends Mbo
        implements MboRemote {
    public UdFourCostExe(MboSet ms)
            throws MXException, RemoteException, RemoteException {
        super(ms);
    }

    public void add() throws MXException, RemoteException {
        super.add();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(2, -1);
        String year = Integer.toString(c.get(1));
        Integer month = Integer.parseInt(Integer.toString(c.get(2) + 1));
        DecimalFormat df = new DecimalFormat("00");               //设置月份格式
        setValue("UDYEAR", year, 11L);
        setValue("UDMONTH", df.format(month), 11L);
//        System.out.println("year = " + year);
//        System.out.println("month = " + month);
        MboSetRemote msc = getMboSet("$CFY"
                , "udassetrunline"
                , "siteid  = :siteid and classchild in ('101105511') "
                        + "and :udyear in (select udassetrun.udyear from udassetrun   where udassetrun.udassetrunid = udassetrunline.udassetrunid) and :udmonth in (select udassetrun.udmonth from udassetrun  where udassetrun.udassetrunid = udassetrunline.udassetrunid)  "
                        + " and :siteid = " + getString("SITEID")
                        + " and :udfourcostexeid = " + getString("UDFOURCOSTEXEID")
        );
        if (!msc.isEmpty() && msc.count() > 0) {
            System.out.println(" 数据集  = " + msc.count());
            double udfuelcost = msc.sum("udfuelcost");                      // 燃料费
            double udelectricitybill = msc.sum("udelectricitybill");        // 电费
            double udmaterial = msc.sum("udmaterial");                      // 材料费
            double udexternalrepair = msc.sum("udexternalrepair");          //维修费
            setValue("UDAQRLF", udfuelcost, 11L);
            setValue("UDAQDF", udelectricitybill, 11L);
            setValue("UDAQCLF", udmaterial, 11L);
            setValue("UDAQWXF", udexternalrepair, 11L);
        }
        MboSetRemote msl = getMboSet("$LFY"
                , "udassetrunline"
                , "siteid  = :siteid and classchild in ('101101122','101105514') "
                        + "and :udyear in (select udassetrun.udyear from udassetrun   where udassetrun.udassetrunid = udassetrunline.udassetrunid) and :udmonth in (select udassetrun.udmonth from udassetrun  where udassetrun.udassetrunid = udassetrunline.udassetrunid)  "
                        + " and :siteid = " + getString("SITEID")
                        + " and :udfourcostexeid = " + getString("UDFOURCOSTEXEID")
        );
        if (!msl.isEmpty() && msl.count() > 0) {
            System.out.println(" 数据集  = " + msl.count());
            double udfuelcost = msl.sum("udfuelcost"); // 燃料费
            double udelectricitybill = msl.sum("udelectricitybill"); // 电费
            double udmaterial = msl.sum("udmaterial"); // 材料费
            double udexternalrepair = msl.sum("udexternalrepair"); //维修费
            setValue("UDZMDRLF", udfuelcost, 11L);
            setValue("UDZMDDF", udelectricitybill, 11L);
            setValue("UDZMDCLF", udmaterial, 11L);
            setValue("UDZMDWXF", udexternalrepair, 11L);
        }

        MboSetRemote msg = getMboSet("$GFY"
                , "udassetrunline"
                , "siteid  = :siteid and classchild in ('101105513') "
                        + "and :udyear in (select udassetrun.udyear from udassetrun   where udassetrun.udassetrunid = udassetrunline.udassetrunid) and :udmonth in (select udassetrun.udmonth from udassetrun  where udassetrun.udassetrunid = udassetrunline.udassetrunid)  "
                        + " and :siteid = " + getString("SITEID")
                        + " and :udfourcostexeid = " + getString("UDFOURCOSTEXEID")
        );
        if (!msg.isEmpty() && msg.count() > 0) {
            System.out.println(" 数据集  = " + msg.count());
            double udfuelcost = msg.sum("udfuelcost"); // 燃料费
            double udelectricitybill = msg.sum("udelectricitybill"); // 电费
            double udmaterial = msg.sum("udmaterial"); // 材料费
            double udexternalrepair = msg.sum("udexternalrepair"); //维修费
            setValue("UDCQRLF", udfuelcost, 11L);
            setValue("UDCQDF", udelectricitybill, 11L);
            setValue("UDCQCLF", udmaterial, 11L);
            setValue("UDCQWXF", udexternalrepair, 11L);
        }

        MboSetRemote msm = getMboSet("$MFY"
                , "udassetrunline"
                , "siteid  = :siteid and classchild in ('101101131','101101132','101101136') "
                        + "and :udyear in (select udassetrun.udyear from udassetrun   where udassetrun.udassetrunid = udassetrunline.udassetrunid) and :udmonth in (select udassetrun.udmonth from udassetrun  where udassetrun.udassetrunid = udassetrunline.udassetrunid)  "
                        + " and :siteid = " + getString("SITEID")
                        + " and :udfourcostexeid = " + getString("UDFOURCOSTEXEID")
        );
        if (!msm.isEmpty() && msm.count() > 0) {
            System.out.println(" 数据集  = " + msm.count());
            double udfuelcost = msm.sum("udfuelcost"); // 燃料费
            double udelectricitybill = msm.sum("udelectricitybill"); // 电费
            double udmaterial = msm.sum("udmaterial"); // 材料费
            double udexternalrepair = msm.sum("udexternalrepair"); //维修费
            setValue("UDMJRLF", udfuelcost, 11L);
            setValue("UDMJDF", udelectricitybill, 11L);
            setValue("UDMJCLF", udmaterial, 11L);
            setValue("UDMJWXF", udexternalrepair, 11L);
        }

        MboSetRemote msn = getMboSet("$NFY"
                , "udassetrunline"
                , "siteid  = :siteid and classchild in ('101104413','101104423','101104427','101104462','101104463','101104464','101104456','101102221','101102224','101104482','101104483','101104484') "
                        + "and :udyear in (select udassetrun.udyear from udassetrun   where udassetrun.udassetrunid = udassetrunline.udassetrunid) and :udmonth in (select udassetrun.udmonth from udassetrun  where udassetrun.udassetrunid = udassetrunline.udassetrunid)  "
                        + " and :siteid = " + getString("SITEID")
                        + " and :udfourcostexeid = " + getString("UDFOURCOSTEXEID")
        );
        if (!msn.isEmpty() && msn.count() > 0) {
            System.out.println(" 数据集  = " + msn.count());
            double udfuelcost = msn.sum("udfuelcost"); // 燃料费
            double udelectricitybill = msn.sum("udelectricitybill"); // 电费
            double udmaterial = msn.sum("udmaterial"); // 材料费
            double udexternalrepair = msn.sum("udexternalrepair"); //维修费
            setValue("UDAQRLF", udfuelcost, 11L);
            setValue("UDLXSBDF", udelectricitybill, 11L);
            setValue("UDLXSBCLF", udmaterial, 11L);
            setValue("UDLXSBWXF", udexternalrepair, 11L);
        }

        UserInfo userInfo = getUserInfo();
        String userid = userInfo.getPersonId();
        MboRemote person = Tools.getLoginPerson(this, userid);
        if ((person != null) && (!person.isNull("persongroup"))) {
            setValue("persongroup", person.getString("persongroup"), 11L);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        setValue("ymdate", sdf.format(MXServer.getMXServer().getDate()), 11L);
        String siteid = getString("siteid");
        String apptype = getThisMboSet().getApp();
        if ((apptype != null) && (apptype.equalsIgnoreCase("ASSETRUNS")) && (siteid != null) && (!siteid.equalsIgnoreCase("ZSP"))) {
            System.out.println("s-s-s-s-s-s-s-s");
            MboSetRemote cbSetRemote = getMboSet("asset_cb");
            if ((!cbSetRemote.isEmpty()) && (cbSetRemote.count() > 0)) {
                MboSetRemote lineSetRemote = getMboSet("udassetrunline");
                for (int i = 0; i < cbSetRemote.count(); i++) {
                    MboRemote lineRemote = lineSetRemote.add();
                    lineRemote.setValue("assetnum", cbSetRemote.getMbo(i).getString("assetnum"));
                }
            }
            cbSetRemote.close();
        } else if ((apptype != null) && (apptype.equalsIgnoreCase("assetrunm")) && (siteid != null) && (!siteid.equalsIgnoreCase("ZSP"))) {
            System.out.println("c-c-c-c-c-c-c-c");
            MboSetRemote sbSetRemote = getMboSet("sbgf");
            if ((!sbSetRemote.isEmpty()) && (sbSetRemote.count() > 0)) {
                MboSetRemote lineSetRemote = getMboSet("udassetrunline");
                for (int i = 0; i < sbSetRemote.count(); i++) {
                    MboRemote sbRemote = sbSetRemote.getMbo(i);
                    MboRemote lineRemote = lineSetRemote.add();
                    lineRemote.setValue("classparent", sbRemote.getString("classparent"));
                    lineRemote.setValue("classchild", sbRemote.getString("classchild"));
                    lineRemote.setValue("assetnum", sbRemote.getString("assetnum"));
                }
            }
            sbSetRemote.close();
        }
    }

    @Override
    public void init() throws MXException {
        super.init();
        try {
            if (getString("STATUS").equals("已提报")) {
                this.setFlag(7L, true);
                System.out.println("全部设置只读");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void appValidate() throws MXException, RemoteException {
        super.appValidate();
        MboSetRemote otherSet = getMboSet("OTHERS");            //通过关系”OTHERS“找到对应的数据
        if (toBeAdded()) {
            if ((!otherSet.isEmpty()) && (otherSet.count() > 0)) {
                Object[] params = {getString("SITE.UDORGJC") + "该周期数据已存在！"};
                throw new MXApplicationException("instantmessaging", "tsdimexception", params);
            }
        }
        System.out.println("\nappValidate===================================");
    }

    public void save() throws MXException, RemoteException {
        super.save();
        System.out.println("\nsave===================================");

        setValue("UDHJWXF", getDouble("UDAQWXF") + getDouble("UDCQWXF") + getDouble("UDMJWXF") + getDouble("UDLXSBWXF") + getDouble("UDLJWXF") + getDouble("UDQTWXF"), 11L);

        setValue("UDHJCLF", getDouble("UDAQCLF") + getDouble("UDCQCLF") + getDouble("UDMJCLF") + getDouble("UDLXSBCLF") + getDouble("UDLJCLF") + getDouble("UDQTCLF"), 11L);

        setValue("UDHJRLF", getDouble("UDAQRLF") + getDouble("UDCQRLF") + getDouble("UDMJRLF") + getDouble("UDLXSBRLF") + getDouble("UDLJRLF") + getDouble("UDQTRLF"), 11L);

        setValue("UDHJDF", getDouble("UDAQDF") + getDouble("UDCQDF") + getDouble("UDMJDF") + getDouble("UDLXSBDF") + getDouble("UDLJDF") + getDouble("UDQTDF"), 11L);

        setValue("UDGJWXF", getDouble("UDAQWXF") + getDouble("UDCQWXF") + getDouble("UDMJWXF") + getDouble("UDLXSBWXF"), 11L);

        setValue("UDGJCLF", getDouble("UDAQCLF") + getDouble("UDCQCLF") + getDouble("UDMJCLF") + getDouble("UDLXSBCLF"), 11L);

        setValue("UDGJRLF", getDouble("UDAQRLF") + getDouble("UDCQRLF") + getDouble("UDMJRLF") + getDouble("UDLXSBRLF"), 11L);

        setValue("UDGJDF", getDouble("UDAQDF") + getDouble("UDCQDF") + getDouble("UDMJDF") + getDouble("UDLXSBDF"), 11L);
    }

}