package lab3;

import java.util.*;

/**
 * 描述：对医院点评过用1表示，0代表没点评过。
 * 基于物品的协同过滤算法[ItemCF]
 */
public class hospital {

    //系统用户
    private static String[] users={"Tom","Bob","Amy","Alice","Jerry"};
    //和这些用户相关的医院
    private static String[] hospitals={"hospital1","hospital2","hospital3",
                                         "hospital4","hospital5",
                                         "hospital6","hospital7"};
    //用户点评医院情况
    private static Integer[][] allUserhospitalsCommentList={
            {1,1,1,0,1,0,0},
            {0,1,1,0,0,1,0},
            {1,0,1,1,1,1,1},
            {1,1,1,1,1,0,0},
            {1,1,0,1,0,1,1}
    };
    //用户点评医院情况，行转列
    private static Integer[][] allhospitalsCommentList=new Integer[allUserhospitalsCommentList[0].length][allUserhospitalsCommentList.length];
    //医院相似度
    private static HashMap<String,Double> hospitalsABSimilaritys=null;
    //待推荐医院相似度列表
    private static HashMap<Integer,Object> hospitalsSimilaritys=null;
    //用户所在的位置
    private static Integer targetUserIndex=null;
    //目标用户点评过的医院
    private static List<Integer> targetUserCommentedhospitals=null;
    //推荐医院
    private static  List<Map.Entry<Integer, Object>> recommlist=null;

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String user=scanner.nextLine();
        while (user!=null && !"exit".equals(user)){
            targetUserIndex=getUserIndex(user);
            if(targetUserIndex==null){
                System.out.println("没有搜索到此用户，请重新输入：");
            }else{
                //转换目标用户医院点评列表
                targetUserCommentedhospitals=Arrays.asList(allUserhospitalsCommentList[targetUserIndex]);
                //计算医院相似度
                calcAlldepartmentsSimilaritys();
                //获取全部待推荐医院
                calcRecommenddepartments();
                //输出推荐医院
                System.out.print("推荐医院列表：");
                for (Map.Entry<Integer, Object> item:recommlist){
                    System.out.print(hospitals[item.getKey()]+"  ");
                }
                System.out.println();
            }

            user=scanner.nextLine();
        }

    }

    /**
     * 获取全部推荐医院
     */
    private static void calcRecommenddepartments(){
        hospitalsSimilaritys=new HashMap<>();
        for (int i=0;i<targetUserCommentedhospitals.size()-1;i++){
            for (int j=i+1;j<targetUserCommentedhospitals.size();j++){
                Object similarity=null;
                if(targetUserCommentedhospitals.get(i)==1 &&  targetUserCommentedhospitals.get(j)==0 && ( hospitalsABSimilaritys.get(i+""+j)!=null || hospitalsABSimilaritys.get(j+""+i)!=null)){
                    similarity=hospitalsABSimilaritys.get(i+""+j)!=null?hospitalsABSimilaritys.get(i+""+j):hospitalsABSimilaritys.get(j+""+i);
                    hospitalsSimilaritys.put(j,similarity);
                }else if(targetUserCommentedhospitals.get(i)==0 &&  targetUserCommentedhospitals.get(j)==1 && (hospitalsABSimilaritys.get(i+""+j)!=null || hospitalsABSimilaritys.get(j+""+i)!=null)){
                    similarity=hospitalsABSimilaritys.get(i+""+j)!=null?hospitalsABSimilaritys.get(i+""+j):hospitalsABSimilaritys.get(j+""+i);
                    hospitalsSimilaritys.put(i,similarity);
                }
            }
        }

        recommlist = new ArrayList<Map.Entry<Integer, Object>>(hospitalsSimilaritys.entrySet());
        Collections.sort(recommlist, new Comparator<Map.Entry<Integer, Object>>() {
            @Override
            public int compare(Map.Entry<Integer, Object> o1, Map.Entry<Integer, Object> o2) {
                return o1.getValue().toString().compareTo(o2.getValue().toString());
            }
        });

        System.out.println("待推荐相似度医院列表："+recommlist);
    }

    /**
     * 计算全部物品间的相似度
     */
    private static void calcAlldepartmentsSimilaritys(){
        converRow2Col();
        hospitalsABSimilaritys=new HashMap<>();
        for (int i=0;i<allhospitalsCommentList.length-1;i++){
            for (int j=i+1;j<allhospitalsCommentList.length;j++){
                hospitalsABSimilaritys.put(i+""+j,calcTwoMovieSimilarity(allhospitalsCommentList[i],allhospitalsCommentList[j]));
            }
        }

        System.out.println("医院相似度："+hospitalsABSimilaritys);
    }


    /**
     * 根据医院全部点评数据，计算两个医院相似度
     * @param movie1Stars
     * @param movie2Starts
     * @return
     */
    private static double calcTwoMovieSimilarity(Integer[] movie1Stars,Integer[] movie2Starts){
        float sum=0;
        for(int i=0;i<movie1Stars.length;i++){
            sum+=Math.pow(movie1Stars[i]-movie2Starts[i],2);
        }
        return Math.sqrt(sum);
    }

    /**
     * 数组行转列
     */
    private static void converRow2Col(){
        for (int i=0;i<allUserhospitalsCommentList[0].length;i++){
            for(int j=0;j<allUserhospitalsCommentList.length;j++){
                allhospitalsCommentList[i][j]=allUserhospitalsCommentList[j][i];
            }
        }
        System.out.println("电影点评转行列："+Arrays.deepToString(allhospitalsCommentList));
    }

    /**
     * 查找用户所在的位置
     * @param user
     * @return
     */
    private static Integer getUserIndex(String user){
        if(user==null || "".contains(user)){
            return null;
        }

        for(int i=0;i<users.length;i++){
            if(user.equals(users[i])){
                return i;
            }
        }

        return null;
    }
}




