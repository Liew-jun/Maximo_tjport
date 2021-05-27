package guide.app.udassetrun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import guide.util.Tools;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.security.UserInfo;
import psdi.server.MXServer;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class UDAssetRun extends Mbo
        implements MboRemote {
    boolean isModified = false;
    private DecimalFormat df = new DecimalFormat("0.00");

    public UDAssetRun(MboSet ms) throws RemoteException {
        super(ms);
    }

    public void init() throws MXException {
        super.init();
        try {
            MboSetRemote udassetrunline = getMboSet("UDASSETRUNLINE");

            if ((!udassetrunline.isEmpty()) && (udassetrunline.count() > 0)) {
                if (getString("UDZT").equals("已提报")) {
                    String name = udassetrunline.getName();
                    System.out.println("name = " + name);
                    udassetrunline.setFlags(7L);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (toBeAdded())
            return;
        try {
            String status = getString("status");
            if ((Tools.checkWfinstance(this)) && (!Tools.checkWfassignment(this))) {
                setFlag(7L, true);
                return;
            }
            if ((status != null) && ((status.equalsIgnoreCase("已关闭")) || (status.equalsIgnoreCase("已取消")))) {
                setFlag(7L, true);
                return;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        UserInfo userInfo = getUserInfo();
        String userid = userInfo.getPersonId();
        MboRemote person = Tools.getLoginPerson(this, userid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        setValue("ymdate", sdf.format(MXServer.getMXServer().getDate()), 11L);
    }

    @Override
    protected void save() throws MXException, RemoteException {
        super.save();
        String udyear = String.valueOf(getInt("UDYEAR"));
        String udmonth = String.valueOf(getInt("UDMONTH"));
        String date = udyear + "-" + udmonth;
//        MboSetRemote udassetrunline = getMboSet("UDASSETRUNLINE");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            udassetrunline.setValue("RLTS)", getDaysOfMonth(sdf.parse(date)) * 24, 11L);
            setValue("RLTS", getDaysOfMonth(sdf.parse(date)) * 24, 11L);
            System.out.println("--------------" + getDaysOfMonth(sdf.parse(date)) * 24);
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

    public void modify() throws MXException, RemoteException {
        if (!this.isModified) {
            this.isModified = true;
            setValue("changeby", getUserInfo().getUserName(), 11L);
            setValue("changedate", MXServer.getMXServer().getDate(), 11L);
        }
        String apptype = getString("apptype");
        if ((apptype != null) && ("ASSETRUNM".equalsIgnoreCase(apptype))) {
            setWhlForAsset();
            setGzpcForAsset();
        } else if ((apptype != null) && ("ASSETRUND".equalsIgnoreCase(apptype))) {
            setWhlForShip();
            setGzpcForShip();
        }
        setWhlForAsset();
        setGzpcForAsset();
        setWxcb();
        setLyl();
        setTscl();
        setTscldun();
    }

    public void appValidate() throws MXException, RemoteException {
        super.appValidate();

        MboSetRemote otherSet = getMboSet("OTHERS");
        String apptype = getString("apptype");
        if ((!otherSet.isEmpty()) && (otherSet.count() > 0) && ("ASSETRUNM".equalsIgnoreCase(apptype))) {
            Object[] params = {getString("SITE.UDORGJC") + "该周期数据已存在！"};
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }

//        System.out.println("-s-s-s-s-s-s-");

        MboSetRemote lineSet = getMboSet("udassetrunline");
        MboRemote line = null;
        double zjsum = 0.0D;
        if (!lineSet.isEmpty()) {
            for (int i = 0; (line = lineSet.getMbo(i)) != null; i++) {
                if (!line.toBeDeleted()) zjsum += line.getDouble("GZTSZY");

            }
            setValue("zjsum", zjsum, 11L);
        }
    }

    public void canDelete() throws MXException, RemoteException {
        if (!"草稿".equals(getString("status")))
            throw new MXApplicationException("udassetrun", "OnlyDraftCanDeleteRecord");
        super.canDelete();
    }

    public void delete(long accessIdenfier) throws MXException, RemoteException {
        super.delete(accessIdenfier);
        MboSetRemote lineSet = getMboSet("UDASSETRUNLINE");
        if (!lineSet.isEmpty())
            lineSet.deleteAll(accessIdenfier);
    }

    public void undelete() throws MXException, RemoteException {
        super.undelete();
        MboSetRemote lineSet = getMboSet("UDASSETRUNLINE");
        if (!lineSet.isEmpty()) lineSet.undeleteAll();
    }

    public void setLyl() throws RemoteException, MXException {
        if ((!isNull("GZTSZY")) && (!isNull("RLTS")) && (getDouble("RLTS") > 0.0D))
            setValue("Lyl", this.df.format(getDouble("GZTSZY") / getDouble("RLTS") * 100.0D), 11L);
    }

    public void setWhlForAsset() throws RemoteException, MXException {
        if ((!isNull("WHTS")) && (!isNull("GZTSZY")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Whl", this.df.format(getDouble("GZTSZY") / (getDouble("GZTSZY") + getDouble("WHTS") + getDouble("GZGZTS")) * 100.0D), 11L);
//        System.out.println("可利用率");
    }

    public void setWhlForShip() throws RemoteException, MXException {
        if ((!isNull("YYTS")) && (!isNull("WHTS")) && (!isNull("RLTS")) && (getDouble("RLTS") > 0.0D))
            setValue("Whl", this.df.format((getDouble("RLTS") - getDouble("WHTS") - getDouble("YYTS")) / getDouble("RLTS") * 100.0D), 11L);
    }

    public void setWxcb() throws RemoteException, MXException {
        if ((!isNull("GZTSZY")) && (!isNull("GUZH")) && (getDouble("GUZH") > 0.0D))
            setValue("Wxcb", getDouble("GZTSZY") / getDouble("GUZH"), 11L);
    }

    public void setGzpcForAsset() throws RemoteException, MXException {
        if ((!isNull("GZTSZY")) && (!isNull("GZGZTS")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Gzpc", this.df.format((getDouble("GZTSZY") - getDouble("GZGZTS")) / getDouble("GZTSZY") * 100.0D), 11L);
    }

    public void setGzpcForShip() throws RemoteException, MXException {
        if ((!isNull("GZTSXJ")) && (!isNull("GZGZTS")) && (getDouble("GZTSXJ") > 0.0D))
            setValue("Gzpc", this.df.format((getDouble("GZTSXJ") - getDouble("GZGZTS")) / getDouble("GZTSXJ") * 100.0D), 11L);
    }

    public void setTscl() throws RemoteException, MXException {
        if ((!isNull("JXZYL")) && (!isNull("GZTSZY")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Tscl", getDouble("JXZYL") / getDouble("GZTSZY"), 11L);
    }

    public void setTscldun() throws RemoteException, MXException {
        if ((!isNull("JXZYLD")) && (!isNull("GZTSZY")) && (getDouble("GZTSZY") > 0.0D))
            setValue("Tscldun", getDouble("JXZYLD") / getDouble("GZTSZY"), 11L);
    }

    public void setSumValue(MboSetRemote lineSet, String attr) throws RemoteException, MXException {
        MboRemote line = null;
        double sum = 0.0D;
        if (!lineSet.isEmpty()) {
            for (int i = 0; (line = lineSet.getMbo(i)) != null; i++) {
                if (!line.toBeDeleted()) sum += line.getDouble(attr);
            }
            setValue(attr, sum, 2L);
        }
    }

    public long MergeZXJX2(MboSetRemote sourceSet) throws RemoteException, MXException {
        if (sourceSet.isEmpty()) {
            return 0L;
        }

        Vector sourceVector = sourceSet.getSelection();
        int vecSize = sourceVector.size();

        if (vecSize < 2) {
            return 0L;
        }

        MboRemote sourceMbo = null;
        String ymdate = ((MboRemote) sourceVector.get(0)).getString("ymdate");
        String description = "";
        String margeid = "";
        String mainTable = "udassetrun";
        String where = " apptype ='ASSETRUNM' and ymdate='" + ymdate + "' and siteid='ZSP' ";
        MboSetRemote targetSetRemote = getMboSet("#" + mainTable, mainTable, where);
        MboRemote newMbo = null;
        if ((!targetSetRemote.isEmpty()) && (targetSetRemote.count() > 0)) {
            newMbo = targetSetRemote.getMbo(0);
            newMbo.setFlag(7L, false);
        } else {
            newMbo = targetSetRemote.add(2L);
            newMbo.setValue("siteid", "ZSP", 2L);
            newMbo.setValue("apptype", "ASSETRUNM", 11L);
        }

        String[] doubleStrings = {"gcpc", "guzh", "gzgzts", "gztsxj", "gztszy", "jxzyl", "jxzyld", "qmsyts", "rlts", "sl", "whts", "yyts"};

        double[] doublevalues = {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};

        String[] linedoubleStrings = {"gcpc", "guzh", "gzgzts", "gztsxj", "gztszy", "jxzyl", "jxzyld", "qmsyts", "rlts", "sl", "whts", "yyts"};

        String[] lineStringStrings = {"siteid", "classparent", "classchild"};

        JSONArray linejsonArray = new JSONArray();

        for (int i = 0; i < vecSize; i++) {
            sourceMbo = (MboRemote) sourceVector.get(i);
            for (int j = 0; j < doubleStrings.length; j++) {
                doublevalues[j] += sourceMbo.getDouble(doubleStrings[j]);
            }
            description = description + "+" + sourceMbo.getString("site.udorgjc");
            margeid = margeid + "," + sourceMbo.getUniqueIDValue();

            MboSetRemote lineSetRemote = sourceMbo.getMboSet("udassetrunline");
            if ((!lineSetRemote.isEmpty()) && (lineSetRemote.count() > 0)) {
                for (int j = 0; j < lineSetRemote.count(); j++) {
                    MboRemote linesourceMbo = lineSetRemote.getMbo(j);
                    JSONObject jsonObjectl = new JSONObject();

                    for (int k = 0; k < linedoubleStrings.length; k++) {
                        jsonObjectl.put(linedoubleStrings[k], linesourceMbo.getString(linedoubleStrings[k]));
                    }
                    for (int k = 0; k < lineStringStrings.length; k++) {
                        jsonObjectl.put(lineStringStrings[k], linesourceMbo.getString(lineStringStrings[k]));
                    }
                    linejsonArray.add(jsonObjectl);
                }
            }
            lineSetRemote.close();
        }

        newMbo.setValue("description", description.substring(1) + " 合并单据", 11L);
        newMbo.setValue("margeid", margeid.substring(1), 11L);
        for (int j = 0; j < doubleStrings.length; j++) {
            newMbo.setValue(doubleStrings[j], doublevalues[j], 2L);
        }

        MboSetRemote lineSet = newMbo.getMboSet("udassetrunline");
        lineSet.deleteAll(11L);
        lineSet.save();
        for (int i = 0; i < linejsonArray.size(); i++) {
            MboRemote linesourceMbo = lineSet.add(2L);
            JSONObject jsonObjectl = (JSONObject) linejsonArray.get(i);
            for (int k = 0; k < lineStringStrings.length; k++) {
                linesourceMbo.setValue(lineStringStrings[k], jsonObjectl.get(lineStringStrings[k]).toString(), 11L);
            }
            for (int k = 0; k < linedoubleStrings.length; k++) {
                linesourceMbo.setValue(linedoubleStrings[k], jsonObjectl.get(linedoubleStrings[k]).toString(), 2L);
            }
        }
        lineSet.save();
        return newMbo.getUniqueIDValue();
    }

    public long MergeSHIP(MboSetRemote sourceSet) throws RemoteException, MXException {
        if (sourceSet.isEmpty()) {
            return 0L;
        }

        Vector sourceVector = sourceSet.getSelection();
        int vecSize = sourceVector.size();

        if (vecSize < 2) {
            return 0L;
        }

        MboRemote sourceMbo = null;
        String ymdate = ((MboRemote) sourceVector.get(0)).getString("ymdate");
        String description = "";

        String mainTable = "udassetrun";
        String where = " apptype ='ASSETRUND' and ymdate='" + ymdate + "' and siteid='ZSP' ";
        MboSetRemote targetSetRemote = getMboSet("#" + mainTable, mainTable, where);
        MboRemote newMbo = null;
        if ((!targetSetRemote.isEmpty()) && (targetSetRemote.count() > 0)) {
            newMbo = targetSetRemote.getMbo(0);
            newMbo.setFlag(7L, false);
        } else {
            newMbo = targetSetRemote.add(2L);
            newMbo.setValue("siteid", "ZSP", 2L);
            newMbo.setValue("apptype", "ASSETRUND", 11L);
        }

        String[] doubleStrings = {"rlts", "gztsxj", "gzgzts", "yyts", "whts", "qmsyts", "guzh"};

        double[] doublevalues = {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};

        String[] linedoubleStrings = {"rlts", "gztsxj", "gzgzts", "yyts", "whts", "qmsyts", "guzh"};

        String[] lineStringStrings = {"siteid", "assetnum", "bz"};

        JSONArray linejsonArray = new JSONArray();

        for (int i = 0; i < vecSize; i++) {
            sourceMbo = (MboRemote) sourceVector.get(i);
            for (int j = 0; j < doubleStrings.length; j++) {
                doublevalues[j] += sourceMbo.getDouble(doubleStrings[j]);
            }
            description = description + "+" + sourceMbo.getString("site.udorgjc");

            MboSetRemote lineSetRemote = sourceMbo.getMboSet("udassetrunline");
            if ((!lineSetRemote.isEmpty()) && (lineSetRemote.count() > 0)) {
                for (int j = 0; j < lineSetRemote.count(); j++) {
                    MboRemote linesourceMbo = lineSetRemote.getMbo(j);
                    JSONObject jsonObjectl = new JSONObject();

                    for (int k = 0; k < linedoubleStrings.length; k++) {
                        jsonObjectl.put(linedoubleStrings[k], linesourceMbo.getString(linedoubleStrings[k]));
                    }
                    for (int k = 0; k < lineStringStrings.length; k++) {
                        jsonObjectl.put(lineStringStrings[k], linesourceMbo.getString(lineStringStrings[k]));
                    }
                    linejsonArray.add(jsonObjectl);
                }
            }
            lineSetRemote.close();
        }

        newMbo.setValue("description", description.substring(1) + " 合并单据", 11L);

        for (int j = 0; j < doubleStrings.length; j++) {
            newMbo.setValue(doubleStrings[j], doublevalues[j], 2L);
        }

        MboSetRemote lineSet = newMbo.getMboSet("udassetrunline");
        lineSet.deleteAll(11L);
        lineSet.save();
        for (int i = 0; i < linejsonArray.size(); i++) {
            MboRemote linesourceMbo = lineSet.add(2L);
            JSONObject jsonObjectl = (JSONObject) linejsonArray.get(i);
            for (int k = 0; k < lineStringStrings.length; k++) {
                linesourceMbo.setValue(lineStringStrings[k], jsonObjectl.get(lineStringStrings[k]).toString(), 11L);
            }
            for (int k = 0; k < linedoubleStrings.length; k++) {
                linesourceMbo.setValue(linedoubleStrings[k], jsonObjectl.get(linedoubleStrings[k]).toString(), 2L);
            }
        }
        lineSet.save();
        return newMbo.getUniqueIDValue();
    }

    public long MergeZXJX(MboSetRemote sourceSet) throws RemoteException, MXException {
        if (sourceSet.isEmpty()) {
            return 0L;
        }

        Vector sourceVector = sourceSet.getSelection();
        int vecSize = sourceVector.size();

        if (vecSize < 1) {
            return 0L;
        }

        MboRemote sourceMbo = null;
        String ymdate = ((MboRemote) sourceVector.get(0)).getString("ymdate");
        String siteid = getInsertSite();
        String description = "";
        String margeid = "";
        String mainTable = "udassetrun";
        String where = " apptype ='ASSETRUNM' and ymdate='" + ymdate + "' and siteid='" + siteid + "' and ismerge=1 ";
        MboSetRemote targetSetRemote = getMboSet("#" + mainTable, mainTable, where);
        MboRemote newMbo = null;
        if ((!targetSetRemote.isEmpty()) && (targetSetRemote.count() > 0)) {
            newMbo = targetSetRemote.getMbo(0);
            newMbo.setFlag(7L, false);
        } else {
            newMbo = targetSetRemote.add(2L);
            newMbo.setValue("siteid", siteid, 2L);
            newMbo.setValue("ismerge", true, 11L);
            newMbo.setValue("apptype", "ASSETRUNM", 11L);
        }

        String[] doubleStrings = {"gcpc", "guzh", "gzgzts", "gztsxj", "gztszy", "jxzyl", "jxzyld", "qmsyts", "rlts", "sl", "whts", "yyts"};

        double[] doublevalues = {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};

        String[] linedoubleStrings = {"gcpc", "guzh", "gzgzts", "gztsxj", "gztszy", "jxzyl", "jxzyld", "qmsyts", "rlts", "sl", "whts", "yyts"};

        ArrayList classlist = new ArrayList();

        for (int i = 0; i < vecSize; i++) {
            sourceMbo = (MboRemote) sourceVector.get(i);
            for (int j = 0; j < doubleStrings.length; j++) {
                doublevalues[j] += sourceMbo.getDouble(doubleStrings[j]);
            }
            description = description + "+" + sourceMbo.getString("site.udorgjc");
            margeid = margeid + "," + sourceMbo.getUniqueIDValue();

            MboSetRemote lineSetRemote = sourceMbo.getMboSet("udassetrunline");
            if ((!lineSetRemote.isEmpty()) && (lineSetRemote.count() > 0)) {
                for (int j = 0; j < lineSetRemote.count(); j++) {
                    MboRemote linesourceMbo = lineSetRemote.getMbo(j);
                    String classname = linesourceMbo.getString("classparent") + "@" + linesourceMbo.getString("classchild");

                    if (!classlist.contains(classname)) classlist.add(classname);
                }
            }
            lineSetRemote.close();
        }

        newMbo.setValue("description", description.substring(1) + " 合并单据", 11L);

        for (int j = 0; j < doubleStrings.length; j++) {
            newMbo.setValue(doubleStrings[j], doublevalues[j], 2L);
        }

        MboSetRemote lineSet = newMbo.getMboSet("udassetrunline");
        lineSet.deleteAll(11L);
        lineSet.save();
        for (int i = 0; i < classlist.size(); i++) {
            MboRemote linesourceMbo = lineSet.add(2L);
            String classname = (String) classlist.get(i);
            linesourceMbo.setValue("classparent", classname.substring(0, classname.indexOf("@")));
            linesourceMbo.setValue("classchild", classname.substring(classname.indexOf("@") + 1));
            linesourceMbo.setValue("siteid", siteid);
            String lineSetWhere = " udassetrunid in (" + margeid.substring(1) + ") and classparent||'@'||classchild ='" + classname + "'" + " and assetnum in (select assetnum from asset where sendgf=1 ) ";

            MboSetRemote sameClassSetRemote = linesourceMbo.getMboSet("#udassetrunline", "udassetrunline", lineSetWhere);
            sameClassSetRemote.reset();
            for (int k = 0; k < linedoubleStrings.length; k++) {
                linesourceMbo.setValue(linedoubleStrings[k], sameClassSetRemote.sum(linedoubleStrings[k]), 2L);
            }
            sameClassSetRemote.close();
        }
        lineSet.save();
        return newMbo.getUniqueIDValue();
    }
}