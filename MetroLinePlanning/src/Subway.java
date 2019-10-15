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
	public class ENode {//����߽ڵ�
        int ivex;       // �ñ���ָ��Ķ����λ��
        ENode nextEdge; // ָ����һ������ָ��
    }

    public class VNode {	//���嶥��ڵ�
    	final static int inf = Integer.MAX_VALUE; //����������޴�
        String data;        // ������Ϣ��վ�����֣�
        ENode firstEdge;    // ָ��ö��������ӵı�
        int dist;			// ����
        boolean flag;		// ��ǣ��Ƿ񱻷��ʹ�,dijkstra�㷨���õ���
        VNode parent;		// ���ڵ㣨��ǰһվ��
        
        public VNode() {		//�޲ι�����
            this.dist = inf;	//��ʼ������Ϊ���޴�
            this.flag = false;	//��ʼ���ڵ�δ����
            this.parent=null;	//��ʼ�������Ϊ��
        }
    }
   
    private static VNode[] mVexs;  	// �������飨�����ڽӱ�

    public Subway(List l1, List l2) {		//����������ڽӱ�l1Ϊ������Ϣ��l2Ϊ����Ϣ
        int vlen = l1.size();				//����������վ������
        int elen = l2.size();				//����
        mVexs = new VNode[vlen];			//���춥������
        for (int i = 0; i < mVexs.length; i++) {	//����վ����Ϣ
            mVexs[i] = new VNode();
            mVexs[i].data = (String) l1.get(i);
            mVexs[i].firstEdge = null;
        }
        for (int i = 0; i < elen; i++) {     		//�������Ϣ
        	String str = (String) l2.get(i);
            String[] strArr = str.split(",");
            String t1 = strArr[0];
            String t2 = strArr[1];
            int p1 = getPosition(t1);
            int p2 = getPosition(t2);
            
            ENode node1 = new ENode();				//���߽ڵ�����ڽӱ�
            node1.ivex = p2;
            if(mVexs[p1].firstEdge == null) mVexs[p1].firstEdge = node1;
            else linkLast(mVexs[p1].firstEdge, node1);
            
            ENode node2 = new ENode();				//��Ϊ������������ͼ�����������ڵ��Ҫ�����һ��
            node2.ivex = p1;
            if(mVexs[p2].firstEdge == null) mVexs[p2].firstEdge = node2;
            else linkLast(mVexs[p2].firstEdge, node2);
        }
    }

    private void linkLast(ENode list, ENode node) {//���߽ڵ����ӵ���Ӧ�ڽӱ��β��
        ENode p = list;
        while(p.nextEdge!=null)p = p.nextEdge;
        p.nextEdge = node;
    }

    private static int getPosition(String sub) {	//��ȡĳվ���ڵ������е�λ��
        for(int i=0; i<mVexs.length; i++)
            if(mVexs[i].data.equals(sub))
                return i;
        return -1;
    }

    public void printGraph() {		//��ӡ�ڽӱ�����ͼ�������ڲ���
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

	public static List<String> dijkstra(List list, String s1, String s2){ //�������·����listΪ������·��Ϣ,  s1Ϊ���վ,  s2Ϊ�յ�վ
		List<String> path = new ArrayList<String>();
		String str = null;
		if(s1.equals(s2)) {
			str="["+s1+"] to ["+s2+"]\n����վ����: 1";
			path.add(str);
			path.add(s1);
			return path;
		}
		//һϵ�г�ʼ������		
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
        
        bfs(mVexs[getPosition(s1)], s2);        //����㿪ʼ����·��
        int shortest_length = dest.dist;
        
        while((dest.parent!=null)&&(!dest.data.equals(start.data))){ //��Ŀ�Ľڵ㿪ʼ����
        	flag: for(int i=0;i<list.size();i++) {
        		if(((String) list.get(i)).equals(dest.data)) {
        			if(((String) list.get(i-1)).equals(dest.parent.data)) {  //ƥ��ýڵ����ڵ�����·
        				for(int k=i;k>=0;k--) {
        					if(((String) list.get(k)).contains("��")) {
        						temp=(String) list.get(k);
        						break flag;
        					}
        				}
        			}else if((i+1)<list.size() && ((String) list.get(i+1)).equals(dest.parent.data)) {
        				for(int k=i;k>=0;k--) {
        					if(((String) list.get(k)).contains("��")) {
        						temp=(String) list.get(k);
        						break flag;
        					}
        				}
        			}
        		}
        	}
        	if(name==null)name=temp;
        	if(!name.equals(temp)) {        //���ǵ�����·�ı���path���ӻ�����·��
        		path.add("-----����"+name+"------");
            	name=temp;   	
        	}
        	path.add(dest.data);	//path���վ����
            dest = dest.parent;
        } 
        path.add(start.data);
        shortest_length+=1;		//�����Լ���վ
        str="["+mVexs[getPosition(s1)].data +"] to ["+mVexs[getPosition(s2)].data+"]\n����վ����:"+shortest_length;
        path.add(str);
        Collections.reverse(path);
        return path;
    }
    
    private static void bfs(VNode v1, String s){  //����·����Ϣ
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
        	                childVNode.flag=true;		//����־��Ϊtrue����ʾ�ýڵ��ѱ�����
        	                childVNode.dist=v.dist+1;	//����+1
        	                childVNode.parent=v;		//���ýڵ���Ϊ����㣨Ϊ�˱��ڻ��ݣ�
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
    
	public static String readJsonData(String pactFile){	//����json��ʽ�ĵ���·����Ϣ
		StringBuffer strbuffer = new StringBuffer();
		File myFile = new File(pactFile);
		if (!myFile.exists()) {
			System.err.println("û���ҵ��ļ�: " + pactFile);
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
	
	public static String getPath(String fileName) {		//��ȡ�ļ�·��
		String path = Subway.class.getResource("").toString() + fileName;
		path = path.replace("file:/", "");
		path = path.replace("/", "//");
		return path;
	}
    
	public static boolean save(String path, String sets) throws IOException {	//�����ļ�
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
    	List<String> list = new ArrayList<String>();     //�洢������Ϣ
    	Set<String> line = new LinkedHashSet<String>();  //�洢��·��Ϣ
    	List<String> station = new ArrayList<String>();  //�洢վ����Ϣ
    	String rfileName = "subway.txt";				 //����ĿĬ�ϵ����ļ���Ϣ��
		String path = Subway.getPath(rfileName);		 //��ȡ·��
		String jsonString = Subway.readJsonData(path);   //��ȡ����վ����Ϣ
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
    	Set<String> set1 = new LinkedHashSet<String>(list);		//�洢վ����Ϣ
    	Set<String> set2 = new LinkedHashSet<String>();			//�洢����Ϣ
    	for(int i=0;i<list.size();i++) {
    		if(list.get(i).contains("��")) {
    			String s1 = list.get(i);
    			set1.remove(s1);
    		}else if( (i+1)==list.size() || list.get(i+1).contains("��")) {
    			continue;
    		}else {
    			String s2 = list.get(i)+","+list.get(i+1);
    			String s3 = list.get(i+1)+","+list.get(i);
    			if(set2.contains(s3)) continue;
    			else set2.add(s2);
    		}
    	}
    	List<String> l1 = new ArrayList<>(set1);			//�洢վ����Ϣ
    	List<String> l2 = new ArrayList<>(set2);        	//�洢����Ϣ
        
        Subway pG;
        pG = new Subway(l1, l2);
        //pG.printGraph();   // ��ӡͼ�����ڲ���
        UImain ui = new UImain(line, station, l1, list);
			
    }   
}

