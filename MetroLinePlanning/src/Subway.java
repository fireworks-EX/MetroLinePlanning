import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Subway {
	public class ENode {//定义边节点
        int ivex;       // 该边所指向的顶点的位置
        ENode nextEdge; // 指向下一条弧的指针
    }

    public class VNode {	//定义顶点节点
    	final static int inf = Integer.MAX_VALUE; //代表距离无限大
        String data;        // 顶点信息（站点名字）
        ENode firstEdge;    // 指向该顶点所连接的边
        int dist;			// 距离
        boolean flag;		// 标记（是否被访问过,dijkstra算法中用到）
        VNode parent;		// 父节点（即前一站）
        
        public VNode() {		//无参构造器
            this.dist = inf;	//初始化距离为无限大
            this.flag = false;	//初始化节点未访问
            this.parent=null;	//初始化父结点为空
        }
    }
   
    private static VNode[] mVexs;  	// 顶点数组（构造邻接表）

    public Subway(List l1, List l2) {		//构造地铁线邻接表，l1为顶点信息，l2为边信息
        int vlen = l1.size();				//顶点数（即站点数）
        int elen = l2.size();				//边数
        mVexs = new VNode[vlen];			//构造顶点数组
        for (int i = 0; i < mVexs.length; i++) {	//存入站点信息
            mVexs[i] = new VNode();
            mVexs[i].data = (String) l1.get(i);
            mVexs[i].firstEdge = null;
        }
        for (int i = 0; i < elen; i++) {     		//存入边信息
        	String str = (String) l2.get(i);
            String[] strArr = str.split(",");
            String t1 = strArr[0];
            String t2 = strArr[1];
            int p1 = getPosition(t1);
            int p2 = getPosition(t2);
            
            ENode node1 = new ENode();				//将边节点加入邻接表
            node1.ivex = p2;
            if(mVexs[p1].firstEdge == null) mVexs[p1].firstEdge = node1;
            else linkLast(mVexs[p1].firstEdge, node1);
            
            ENode node2 = new ENode();				//因为地铁线是无向图，所以两个节点间要互相存一次
            node2.ivex = p1;
            if(mVexs[p2].firstEdge == null) mVexs[p2].firstEdge = node2;
            else linkLast(mVexs[p2].firstEdge, node2);
        }
    }

    private void linkLast(ENode list, ENode node) {//将边节点连接到相应邻接表的尾部
        ENode p = list;
        while(p.nextEdge!=null)p = p.nextEdge;
        p.nextEdge = node;
    }

    private static int getPosition(String sub) {	//获取某站点在地铁线中的位置
        for(int i=0; i<mVexs.length; i++)
            if(mVexs[i].data.equals(sub))
                return i;
        return -1;
    }

    public void printGraph() {		//打印邻接表（无向图），用于测试
        for (int i = 0; i < mVexs.length; i++) {
            System.out.printf("%d(%s): ", i, mVexs[i].data);
            ENode node = mVexs[i].firstEdge;
            while (node != null) {
                System.out.printf("%d(%s) ", node.ivex, mVexs[node.ivex].data);
                node = node.nextEdge;
            }
            System.out.printf("\n");
        }
    }

	public static List<String> dijkstra(List list, String s1, String s2){ //计算最短路径，list为地铁线路信息,  s1为起点站,  s2为终点站
		List<String> path = new ArrayList<String>();
		String str = null;
		if(s1.equals(s2)) {
			str="["+s1+"] to ["+s2+"]\n经过站点数: 1";
			path.add(str);
			path.add(s1);
			return path;
		}
		//一系列初始化操作		
		for(int i=0;i<mVexs.length;i++) {
			mVexs[i].dist=VNode.inf;
			mVexs[i].flag=false;
			mVexs[i].parent=null;
		}
        VNode start = mVexs[getPosition(s1)];
        VNode dest = mVexs[getPosition(s2)];        
    	String name=null;
    	String temp=null;
        start.parent=null;
        start.dist=0;
        start.flag=true;
        
        bfs(mVexs[getPosition(s1)], s2);        //从起点开始更新路径
        int shortest_length = dest.dist;
        
        while((dest.parent!=null)&&(!dest.data.equals(start.data))){ //从目的节点开始回溯
        	flag: for(int i=0;i<list.size();i++) {
        		if(((String) list.get(i)).equals(dest.data)) {
        			if(((String) list.get(i-1)).equals(dest.parent.data)) {  //匹配该节点所在地铁线路
        				for(int k=i;k>=0;k--) {
        					if(((String) list.get(k)).contains("线")) {
        						temp=(String) list.get(k);
        						break flag;
        					}
        				}
        			}else if((i+1)<list.size() && ((String) list.get(i+1)).equals(dest.parent.data)) {
        				for(int k=i;k>=0;k--) {
        					if(((String) list.get(k)).contains("线")) {
        						temp=(String) list.get(k);
        						break flag;
        					}
        				}
        			}
        		}
        	}
        	if(name==null)name=temp;
        	if(!name.equals(temp)) {        //若是地铁线路改变则path增加换乘线路名
        		path.add("-----换乘"+name+"------");
            	name=temp;   	
        	}
        	path.add(dest.data);	//path添加站点名
            dest = dest.parent;
        } 
        path.add(start.data);
        shortest_length+=1;		//算上自己这站
        str="["+mVexs[getPosition(s1)].data +"] to ["+mVexs[getPosition(s2)].data+"]\n经过站点数:"+shortest_length;
        path.add(str);
        Collections.reverse(path);
        return path;
    }
    
    private static void bfs(VNode v1, String s){  //更新路径信息
        List<VNode> queue = new LinkedList<VNode>();
        queue.add(v1);
        while(true) {
        	int count=queue.size();
        	for(int i=0;i<count;i++) {
        		VNode v = queue.get(0);
        		if(v != null) {
        			ENode node = v.firstEdge;
        			while (node != null) {
        				VNode childVNode = mVexs[node.ivex];
        	            if(childVNode.flag==false)
        	            {
        	                childVNode.flag=true;		//将标志置为true，表示该节点已被访问
        	                childVNode.dist=v.dist+1;	//距离+1
        	                childVNode.parent=v;		//将该节点设为父结点（为了便于回溯）
        	                if(childVNode.data.equals(s)) return;
        	                queue.add(childVNode);
        	            }
        	            node = node.nextEdge;
        			}
        		}
        		queue.remove(0);
        	}
        }

    }
    
	public static String readJsonData(String pactFile){	//读入json格式的地铁路线信息
		StringBuffer strbuffer = new StringBuffer();
		File myFile = new File(pactFile);
		if (!myFile.exists()) {
			System.err.println("没有找到文件: " + pactFile);
		}
		try {
			FileInputStream fis = new FileInputStream(pactFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fis, "GBK");
			BufferedReader in  = new BufferedReader(inputStreamReader);			
			String str;
			while ((str = in.readLine()) != null) {
				strbuffer.append(str);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		return strbuffer.toString();
	}
	
	public static String getPath(String fileName) {		//获取文件路径
		String path = Subway.class.getResource("").toString() + fileName;
		path = path.replace("file:/", "");
		path = path.replace("/", "//");
		return path;
	}
    
	public static boolean save(String path, String sets) throws IOException {	//保存文件
		FileWriter fw;
    	File f=new File(path);
		if (!f.exists()) {
			f.createNewFile();
		}
    	FileOutputStream fos1=new FileOutputStream(f);
    	OutputStreamWriter dos1=new OutputStreamWriter(fos1);
    	dos1.write(sets);
    	dos1.write("\n");
    	dos1.close();

		return true;
	}

    public static void main(String[] args) throws IOException {
    	List<String> list = new ArrayList<String>();     //存储地铁信息
    	Set<String> line = new LinkedHashSet<String>();  //存储线路信息
    	List<String> station = new ArrayList<String>();  //存储站点信息
    	String rfileName = "subway.txt";				 //本题目默认地铁文件信息名
		String path = Subway.getPath(rfileName);		 //获取路径
		String jsonString = Subway.readJsonData(path);   //读取地铁站点信息
		JSONArray lineArr = JSON.parseArray(jsonString);
		for(int i = 0; i < lineArr.size(); i++) {
			JSONObject jsonObj = lineArr.getJSONObject(i);
			JSONArray stationArr =  jsonObj.getJSONArray("Station");
//			System.out.print( "\n" + jsonObj.getString("Line") + " : ");
			list.add(jsonObj.getString("Line"));
			line.add(jsonObj.getString("Line"));
			String temp="";
			for(int j = 0; j < stationArr.size(); j++) {
				String tmpSta = stationArr.getString(j); 
				temp=temp+" "+stationArr.getString(j);
//				System.out.print(tmpSta + ' ');
				list.add(tmpSta);
			}
			station.add(temp);
		}
    	Set<String> set1 = new LinkedHashSet<String>(list);		//存储站点信息
    	Set<String> set2 = new LinkedHashSet<String>();			//存储边信息
    	for(int i=0;i<list.size();i++) {
    		if(list.get(i).contains("线")) {
    			String s1 = list.get(i);
    			set1.remove(s1);
    		}else if( (i+1)==list.size() || list.get(i+1).contains("线")) {
    			continue;
    		}else {
    			String s2 = list.get(i)+","+list.get(i+1);
    			String s3 = list.get(i+1)+","+list.get(i);
    			if(set2.contains(s3)) continue;
    			else set2.add(s2);
    		}
    	}
    	List<String> l1 = new ArrayList<>(set1);			//存储站点信息
    	List<String> l2 = new ArrayList<>(set2);        	//存储边信息
        
        Subway pG;
        pG = new Subway(l1, l2);
        //pG.printGraph();   // 打印图，用于测试
        UImain ui = new UImain(line, station, l1, list);
			
    }   
}

