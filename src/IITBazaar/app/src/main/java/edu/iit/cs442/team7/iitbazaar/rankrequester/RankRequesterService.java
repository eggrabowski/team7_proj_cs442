package edu.iit.cs442.team7.iitbazaar.rankrequester;

import android.support.v4.util.Pair;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static edu.iit.cs442.team7.iitbazaar.rankrequester.PhoneBookKeys.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.iit.cs442.team7.iitbazaar.common.RankResultCode;


/**
 * Created by jmn on 11/4/15.
 */
public class RankRequesterService {


    public final String PHONEBOOK_URL_PREFIX = "http://phonebook.iit.edu/search_id.cgi?menu=fss&listing=exact&fname=";
    //first name
    public final String PHONEBOOK_URL_INFIX = "+&lname=";
    //last name
    public final String PHONEBOOK_URL_SUFFIX = "&exactSearch=Search+Exact";

   private final  String emailNodeKey = "email";


   // public final String testUrl = "http://phonebook.iit.edu/search_id.cgi?menu=fss&listing=all&fname=&lname=y&allSearch=Search+All";

    private HashMap<String,HashMap<PhoneBookKeys,String>> staffAndFactulty;
    private HashMap<String,HashMap<PhoneBookKeys,String>> students;

    List<List<String>> testList = new LinkedList();


    public Pair<RankResultCode,String> getDepartment(String firstName, String lastName, String email) {

        // Get the XML
        final String url;

        boolean isHeader = false;
        boolean isStaff = false;
        boolean isSkipRow = false;
        boolean isNoMatches = false;
        List <String>studentCursorList;
        List <String>studentHeaderList = null;
        List <String>staffCursorList;
        List <String>staffHeaderList = null;

        List <String>studentMatch = null;

        List <String>staffMatch = null;




        List <List<String>>rowList = null;
        List <String>colList = null;







        try {

                url = PHONEBOOK_URL_PREFIX + firstName + PHONEBOOK_URL_INFIX + PHONEBOOK_URL_SUFFIX;

            Document doc = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla")
                    .maxBodySize(0)
                    .timeout(600000)
                    .get();



            Element table = doc.select("table").first();

            Elements rows = table.select("tr tr tr");

            rowList = new ArrayList<>();

  //          System.out.println("negat");
            int emailNode = -1;
//            String emailNodeKey = "email";

            for (Element row : rows) {
                colList = new ArrayList<>();

                // Get the first column for the row


                Elements cols = row.select("td");

//                System.out.println("columns in row = "+cols.size());



                for (Element col : cols) {

                    // Get the text for the element
                    final String colText = col.text();


                //    System.out.println("new colList -> "+colText);

                    final PhoneBookKeys cursor = PhoneBookKeys.getByValue(colText);


                    if (cursor == STAFF_AND_FACULTY) {

                     //   System.out.println("negat");
                        emailNode = -1;

                        isStaff = true;
                        isHeader = true;

                        isSkipRow = true;
                        break;
                    }


                    if (cursor == NO_MATCHES) {

                   //     System.out.println("negat");
                        emailNode = -1;


                        isHeader = false;


                        isNoMatches = true;
                        break;

                    }


                    if (cursor == STUDENT) {


                      //  System.out.println("negat");
                        emailNode = -1;
                        isStaff = false;
                        isHeader = true;

                        isSkipRow = true;
                        break;

                    }


                    if (isStaff && null!=staffMatch){
                        isHeader = false;
                        isSkipRow = true;
                        break;
                    }

                    if (!isStaff && null!=studentMatch){
                        isHeader = false;
                        isSkipRow = true;
                        break;
                    }

                    if (cursor == BLANK) {

                        colList.add("");
                        continue;

                    }




                    colList.add(colText);



                }


                boolean isEmpty = false;
                int cSize = colList.size();

                if (cSize>0) {

                    if ("".equals(colList.get(0))) {

                        for (int i = 0; i < cSize; i++) {

                            isEmpty = (isEmpty | "".equals(colList.get(i)));

                        }
                    }
                }


                if (!isEmpty) {

                    if (!isNoMatches) {

                        if (!isSkipRow) {


                            if (isHeader) {


                                if (isStaff) {
                               //     System.out.println("staff header row - > " + colList);

                                    staffHeaderList = colList;

                                    int staffHeaderSize = staffHeaderList.size();

                                    for (int i = 0; i<staffHeaderSize; i++){


                                        if (staffHeaderList.get(i).trim().toLowerCase().contains(emailNodeKey)){

                                            emailNode = i;
                                            break;
                                        }

                                    }


                                    if (emailNode == -1){
                                 //       System.out.println("email node not found");
                                    }


                                } else {
                                 //   System.out.println("student header row - > " + colList);
                                    studentHeaderList = colList;



                                    int studentHeaderSize = studentHeaderList.size();

                                    for (int i = 0; i<studentHeaderSize; i++){

                                        if (studentHeaderList.get(i).trim().toLowerCase().contains(emailNodeKey)){

                                            emailNode = i;
                                            break;
                                        }

                                    }

                                    if (emailNode == -1){
                               //        System.out.println("email node not found");
                                    }



                                }

                                isHeader = false;
                            } else {


                                if (isStaff) {
                                    staffCursorList = colList;



                                   // System.out.println("staff normal row - > " + colList);

                                    if (staffCursorList.get(emailNode).equals(email)){
                                     //   System.out.println("staffmatch !");
                                        staffMatch = staffCursorList;
                                    }

                                } else {



                                    studentCursorList = colList;
                                 //   System.out.println("student normal row - > " + colList);


                                    try {
                                        if (studentCursorList.get(emailNode).equals(email)) {
                                    //        System.out.println("studentmatch !");
                                            studentMatch = studentCursorList;
                                        }
                                    }catch (IndexOutOfBoundsException iob){
                                        System.err.println(studentCursorList);
                                    }
                                }

                            }
                        }
                        isSkipRow = false;
                    }else {


                        if (isStaff) {
                           staffCursorList = null;
                            staffHeaderList = null;
                        } else {


                            studentCursorList = null;
                            studentHeaderList = null;
                        }


                    }
                    isNoMatches = false;
                }
                else{
                   // System.out.println("empt");
                }


                    //rowadd


            }



        } catch (java.net.SocketTimeoutException jse) {
            jse.printStackTrace();
        } catch (java.net.UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }


        //student priority ...assuming a student is also staff
        if (null != studentMatch){


            int studentSize = studentHeaderList.size();
            int deptPos = -1;
            int levelPos = -1;
            for (int i = 0; i < studentSize; i++){

                if (studentHeaderList.get(i).trim().toLowerCase().contains("department")){
                    deptPos = i;
                }

                if (studentHeaderList.get(i).trim().toLowerCase().contains("level")){
                    levelPos = i;
                }

            }


            if (deptPos+levelPos > -1){
                //good


                String dept = studentMatch.get(deptPos);
                String level = studentMatch.get(levelPos);

                String rank = normalizeMajor(dept,level);

                return new Pair<RankResultCode,String>(RankResultCode.PHONEBOOK_STUDENT,rank);


            }
            else{
                //very very bad
                return new Pair<RankResultCode,String>(RankResultCode.ERROR,null);
            }




           // studentHeaderList
           // studentMatch
        }


        if (null != staffMatch){

            //ran out of time

            return new Pair<RankResultCode,String>(RankResultCode.UNKNOWN,null);

            // staffHeaderList
            // staffMatch
        }


        return new Pair<RankResultCode,String>(RankResultCode.UNKNOWN,null);




    }




    private String normalizeMajor(String rawDepartment, String rawLevel){

        return rawDepartment;

    }


  //  row - > [Staff & Faculty Results]	[Exact Matches]	[<Show All Matches>]
  //  row - > [Last Name]	[First Name]	[Title]	[Department]	[Address]	[Phone/Fax]	[Email/U-ID]
  //  row - > [ ]	[ ]	[ ]	[ ]	[ ]	[ ]	[ ]
  //  row - > [No Matches]
  //  row - > [Student Results]	[ ]	[Exact Matches       <Show All Matches>]
  //  row - > [Last Name]	[First Name]	[Department]	[Level]	[U-ID]	[Email]
  //  row - > [ ]	[ ]	[ ]	[ ]	[ ]	[ ]
  //  row - > [Nosek]	[Janusz]	[CSCI]	[Graduate]	[jnosek]	[jnosek@hawk.iit.edu]



    /*
    row - > [Staff & Faculty Results]	[Exact Matches]	[<Show All Matches>]
    row - > [Last Name]	[First Name]	[Title]	[Department]	[Address]	[Phone/Fax]	[Email/U-ID]
    row - > [ ]	[ ]	[ ]	[ ]	[ ]	[ ]	[ ]
    row - > [Johnson]	[Antawan]	[Facilities Worker II]	[Service Contracts]	[MH 200]	[312.567.3320(P) ..(F)]	[ajohns49@iit.edu]
    row - > [Johnson]	[Craig]	[Head Mach Shop/Coord Dsgn Stud]	[Dean of Engineering]	[E1 047]	[312.567.3201(P) 312.567.7230(F)]	[johnsoncr@iit.edu]
    row - > [Johnson]	[Dawn]	[Administrator FSPCA]	[IFSH Education]	[MF 228]	[708.563.8188(P) ..(F)]	[djohns39@iit.edu]
    row - > [Johnson]	[Elizabeth]	[Instructor]	[Office of Professional Development]	[TS 2037]	[312.906.6599(P) ..(F)]	[ejohns20@iit.edu]
    row - > [Johnson]	[Kara]	[Assistant Director]	[International Center]	[MC 202]	[312.567.3680(P) 312.567.3687(F)]	[kjohns49@iit.edu]
    row - > [Johnson]	[Kari]	[Prof of Legal Res & Writ]	[CK Faculty]	[DT 719]	[312.906.5164(P) 312.906.5280(F)]	[kjohnson@kentlaw.iit.edu]
    row - > [Johnson]	[Leslie]	[Studio Assistant Professor]	[Architecture]	[CR 005]	[312.567.3230(P) 312.567.5820(F)]	[johnsonl@iit.edu]
    row - > [Johnson]	[Michael]	[Staff Attorney]	[CK Law Offices]	[DT 663]	[312.906.5289(P) 312.906.5299(F)]	[mjohns12@kentlaw.iit.edu]
    row - > [Johnson]	[Oscar]	[Shop Technician]	[Civil, Architectural and Env Eng]	[AM 114]	[312.567.3903(P) ..(F)]	[stan.johnson@iit.edu]
    row - > [Johnson]	[Porter]	[Emeritus Professor]	[Physics Department]	[LS 148A]	[312.567.3443(P) 312.567.3494(F)]	[johnsonpo@iit.edu]
    row - > [Johnson]	[Tiffany]	[Event Planner]	[Stuart School of Business]	[IT 13F4-2]	[312.906.6543(P) 312.906.6549(F)]	[tjohns21@stuart.iit.edu]
    row - > [Johnson]	[William]	[]	[IIT Research Institute]	[LR]	[312.567.4941(P) ..(F)]	[wjohnson@iitri.org]
    row - > [Student Results]	[ ]	[Exact Matches       <Show All Matches>]
    row - > [Last Name]	[First Name]	[Department]	[Level]	[U-ID]	[Email]
    row - > [ ]	[ ]	[ ]	[ ]	[ ]	[ ]
    row - > [Johnson]	[Blake]	[MMAE]	[Undergraduate]	[bjohns39]	[bjohns39@hawk.iit.edu]
    row - > [Johnson]	[Brandon]	[LAW]	[LAW]	[bjohns35]	[bjohns35@kentlaw.iit.edu]
    row - > [JOHNSON]	[BRUCE]	[CEPD]	[Continuing Education]	[bjohns41]	[bjohns41@hawk.iit.edu]
    row - > [Johnson]	[Christopher]	[LAW]	[LAW]	[cjohns46]	[cjohns46@kentlaw.iit.edu]
    row - > [Johnson]	[Christopher]	[INTM]	[Undergraduate]	[cjohns56]	[cjohns56@hawk.iit.edu]
    row - > [Johnson]	[Cori]	[EECE]	[Graduate]	[cjohns29]	[cjohns29@hawk.iit.edu]
    row - > [Johnson]	[Daniel]	[CEPD]	[Continuing Education]	[djohns22]	[djohns22@hawk.iit.edu]
    row - > [Johnson]	[David]	[CEPD]	[Continuing Education]	[djohns29]	[djohns29@hawk.iit.edu]
    row - > [Johnson]	[Elizabeth]	[CEPD]	[Continuing Education]	[ejohns20]	[ejohns20@iit.edu]
    row - > [Johnson]	[Glenna]	[CEPD]	[Continuing Education]	[gjohnso4]	[gjohnso4@hawk.iit.edu]
    row - > [Johnson]	[Graham]	[CEPD]	[Continuing Education]	[gjohnso1]	[gjohnso1@hawk.iit.edu]
    row - > [Johnson]	[James]	[EECE]	[Graduate]	[jjohns70]	[jjohns70@hawk.iit.edu]
    row - > [Johnson]	[Kathleen]	[CEPD]	[Continuing Education]	[kjohns25]	[kjohns25@hawk.iit.edu]
    row - > [Johnson]	[Kirsten]	[LAW]	[LAW]	[kjohns52]	[kjohns52@kentlaw.iit.edu]
    row - > [Johnson]	[Kristina]	[PSYC]	[Graduate]	[kjohns53]	[kjohns53@hawk.iit.edu]
    row - > [Johnson]	[Lindani]	[MMAE]	[Undergraduate]	[ljohns27]	[ljohns27@hawk.iit.edu]
    row - > [Johnson]	[Mary]	[FDSN]	[Graduate]	[mjohns39]	[mjohns39@hawk.iit.edu]
    row - > [Johnson]	[Neil]	[LAW]	[LAW]	[njohns14]	[njohns14@kentlaw.iit.edu]
    row - > [Johnson]	[Raiven]	[CSCI]	[Undergraduate]	[rjohns42]	[rjohns42@hawk.iit.edu]
    row - > [Johnson]	[Reginald]	[MMAE]	[Undergraduate]	[rjohns37]	[rjohns37@hawk.iit.edu]
    row - > [Johnson]	[Robin]	[CEPD]	[Continuing Education]	[rjohns44]	[rjohns44@hawk.iit.edu]
    row - > [Johnson]	[Ruth]	[CEPD]	[Graduate Certificate Program]	[rjohns13]	[rjohns13@hawk.iit.edu]
    row - > [Johnson]	[Timothy]	[LAW]	[LAW]	[tjohns29]	[tjohns29@kentlaw.iit.edu]
    row - > [Johnson]	[Tom]	[CEPD]	[Continuing Education]	[tjohns17]	[tjohns17@hawk.iit.edu]
    row - > [Johnson]	[Yvonne]	[CEPD]	[Continuing Education]	[yjohnso5]	[yjohnso5@hawk.iit.edu]
    row - > [Johnson]	[Zephan]	[CSCI]	[Undergraduate]	[zjohnson]	[zjohnson@hawk.iit.edu]*/


/*    row - > [Staff & Faculty Results]	[All Matches]	[ ]
    row - > [Last Name]	[First Name]	[Title]	[Department]	[Address]	[Phone/Fax]	[Email/U-ID]
    row - > [ ]	[ ]	[ ]	[ ]	[ ]	[ ]	[ ]
    row - > [No Matches]
    row - > [Student Results]	[ ]	[All Matches        ]
    row - > [Last Name]	[First Name]	[Department]	[Level]	[U-ID]	[Email]
    row - > [ ]	[ ]	[ ]	[ ]	[ ]	[ ]
    row - > [No Matches]*/

}
