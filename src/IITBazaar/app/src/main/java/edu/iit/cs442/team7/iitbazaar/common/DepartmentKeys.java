package edu.iit.cs442.team7.iitbazaar.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jnosek@hawk.iit.edu">Janusz M. Nosek</a>
 */


public enum DepartmentKeys {


    ONESTP("1STP","One Stop"),

    ACE("ACE","Armour College of Engineering"),

    ACP("ACP","Access"),

    ACT("ACT","Controllers Office"),

    ADM("ADM","Undergraduate Admissions Office"),

    ALM("ALM","Alumni Relations"),

    AMY("AMY","Army ROTC"),

    AR("AR","Academic Resource Center"),

    ARC("ARC","Architecture"),

    AS("AS","Air Force Science"),

    ATH("ATH","Athletics Office"),

    AUX("AUX","Auxiliary Services Administration"),

    BOK("BOK","Bookstore - Barnes and Noble"),

    BUR("BUR","Student Accounting"),

    CE("CE","Civil"),

    CHE("CHE","Chemical and Biological Engineering"),



    CM("CM","Marketing and Communications"),

    CMC("CMC","Career Management Center"),

    CMP("CMP","Chicago Area Health Medical Careers"),

    CNS("CNS","Systems and Technology Services"),

    COM("COM","Community Affairs and Outreach"),

    COR("COR","Corporate Relations"),

    CS("CS","Computer Science Department"),

    DMC("DMC","Digital Media Center"),

    ECE("ECE","Electrical Computer Engineering"),

    ETH("ETH","Center for Study of Ethics"),

    EXA("EXA","External Affairs"),

    FA("FA","Financial Aid Office"),

    FAC("FAC","Facilities"),

    FD("FD","Sodexo"),

    FIN("FIN","Finance and CFO Office"),

    GA("GA","Graduate and Professional Admission"),

    GC("GC","General Counsel"),

    GCA("GCA","Grant and Contract Accounting"),

    GE("GE","Graduate and Professional Recruitme"),

    GRD("GRD","Graduate College"),

    HOU("HOU","Housing and Residential Services"),

    HR("HR","Human Resources"),

    HUM("HUM","Humanities"),

    ID("ID","School of Design"),

    IIR("IIR","Institutional Info and Research"),

    IMAN("IMAN","Information Technology Management P"),

    INT("INT","International Center"),

    ITC("ITC","Professional Learning Programs"),

    ITR("ITR","IIT Research Institute"),

    ITV("ITV","Extended Learning"),



    LFM("LFM","Stuart School of Business"),

    LIB("LIB","Galvin Library"),

    LSC("LSC","CK Law Offices"),

    LWB("LWB","CK Administration and Finance"),

    LWC("LWC","CK Admissions"),

    LWD("LWD","CK Career Services"),

    LWE("LWE","CK CALI"),

    LWF("LWF","CK Continuing Legal Education"),

    LWG("LWG","CK Computer Center"),

    LWH("LWH","CK Dean of Law"),

    LWJ("LWJ","CK Faculty Support"),

    LWK("LWK","Financial Aid Downtown Campus"),

    LWL("LWL","CK Library"),

    LWM("LWM","CK Engineering"),

    LWN("LWN","CK Public Affairs"),

    LWO("LWO","CK Registrar"),

    LWP("LWP","CK Law and the Workplace"),

    LWQ("LWQ","CK Academic Affairs"),

    LWR("LWR","CK Alumni"),

    LWT("LWT","CK International Law"),

    LWU("LWU","CK Development Office"),

    LWV("LWV","CK ISLAT"),

    LWW("LWW","CK College Service Center"),

    LWX("LWX","CK Financial Services LLM"),

    LWY("LWY","CK Legal Writing Program"),

    MAE("MAE","Mechanical"),

    MAN("MAN","Industrial Technology and Managemnt"),

    MSE("MSE","Mathematics and Science Education"),

    MTC("MTC","National Center for Food and Safety"),

    MTH("MTH","Applied Mathematics"),

    NS("NS","Naval Science"),

    OLS("OLS","IIT Online Admin"),

    OS("OS","Office Services"),

    PAY("PAY","Payroll Office"),

    PO("PO","Post Office"),

    PRE("PRE","President's Office"),

    PRI("PRI","Biomedical Engineering"),

    PRZ("PRZ","Pritzker Institute"),

    PSY("PSY","Psychology"),

    PUR("PUR","Purchasing and Procurement Office"),

    PVT("PVT","Provost Office"),

    RIC("RIC","Rice Campus"),

    SAF("SAF","Public Safety"),

    SCDI("SCDI","SCDI"),

    SEO("SEO","Student Employment Office"),

    SRR("SRR","Registrar's Office"),

    SS("SS","Social Sciences Department"),

    STA("STA","Student Affairs"),

    SWC("SWC","Student Health and Wellness Center"),

    TEL("TEL","Telecommunication Services"),

    TI("TI","Technology Infrastructure"),

    TS("TS","Technology Services"),

    UAA("UAA","Undergraduate Academic Affairs"),

    VPB("VPB","Business and Operations"),

    VPI("VPI","Institutional Advancement"),

    VPR("VPR","International Affairs"),

    WIS("WIS","WISER"),

    ACED("ACED","Advancing Career and Education Program"),
    AMAT("AMAT","Applied Mathematics"),
    ARCH("ARCH","Architecture"),
    BADM("BADM","Business Administration"),
    BCHS("BCHS","Biochemistry"),
    BCPS("BCPS","Biological, Chemical, and Physical Sciences"),
    BIOL("BIOL","Biology"),
    BMED("BMED","Biomedical Engineering"),
    CAEE("CAEE","Civil, Architectural and Environmental Engineering"),
    CEPD("CEPD","CEPD"),
    CHBE("CHBE","Chemical and Biological Engineering"),
    CHEM("CHEM","Chemistry"),
    CSCI("CSCI","Computer Science"),
    CSLD("CSLD","CSLD"),

    EECE("EECE","Electrical and Computer Engineering"),
    FDSN("FDSN","Food Science and Nutrition"),
    HUMA("HUMA","Humanities"),
    IDES("IDES","Institute of Design"),
    INTM("INTM"," Industrial Technology and Management"),
    ITCP("ITCP","ITCP"),
    ITMG("ITMG","Information Technology and Management"),
    LAW("LAW","Law"),
    LHSD("LHSD","Human Sciences"),
    MILS("MILS","Master in Information and Library Systems"),
    MMAE("MMAE","Mechanical, Materials and Aerospace Engineering"),
    MSED("MSED","Mathematics and Science Education"),
    PHYS("PHYS","Physics"),
    PSYC("PSYC","Psychology"),
    SSCI("SSCI","Social Sciences"),
    UGED("UGED","--"),

    UNKNOWN_DEPT("-","--");

    private final String deptShort;
    private final String deptLong;

    private static Map<String,DepartmentKeys> deptShortLookup = new HashMap<>();
    private static Map<String,DepartmentKeys> deptLongLookup = new HashMap<>();


    static{
        for(final DepartmentKeys dept: DepartmentKeys.values() ){

            deptShortLookup.put(dept.deptShort, dept);
            deptLongLookup.put(dept.deptLong, dept);
        }

    }

    DepartmentKeys(String deptShort, String deptLong) {
        this.deptShort = deptShort;
        this.deptLong = deptLong;
    }


    public String getDeptLongName() { return deptShort; }
    public String getDeptShortName() { return deptLong; }

    public DepartmentKeys getByLongName(String longName){

        final DepartmentKeys dKey = deptShortLookup.get(longName);
        return null == dKey ? UNKNOWN_DEPT : dKey;
    }

    public DepartmentKeys getByShortName (String shortName){
        final DepartmentKeys dKey = deptLongLookup.get(shortName);
        return null == dKey ? UNKNOWN_DEPT : dKey;

    }





}
