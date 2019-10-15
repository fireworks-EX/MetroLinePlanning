import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UIstation extends JDialog implements ActionListener{
	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private JLabel Start = new JLabel("起点站: ");
	private JLabel End = new JLabel("终点站: ");
	private String[] listData = null;
	private JComboBox<String> start = null;	
	private JComboBox<String> end = null;
	private Button btnSear = new Button("查询");
	private Button btnSave = new Button("保存");
	private String[] tableTitles={"线路详情"};
	private Object title[]=tableTitles;
	private Object data[][];
	DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable(model);
	List<String> l1 = null;
	List<String> list = null;
	List<String> result = null;
	String[] station = null;
	String[] path = null;
	private void reload(String[] ss){
		int count=ss.length;
		data =  new Object[count][1];
		for(int i=0;i<count;i++){
			data[i][0]=ss[i];
		}
		model.setDataVector(data,title);
		table.setRowHeight(20);
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		this.table.validate();
		this.table.repaint();
	}
	
	public UIstation(List l1, List list) {
		this.l1=l1;
		this.list=list;
		listData = (String[]) l1.toArray(new String[l1.size()]);
		start = new JComboBox<String>(listData);
		end = new JComboBox<String>(listData);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(Start);
		toolBar.add(start);
		toolBar.add(End);
		toolBar.add(end);
		toolBar.add(btnSear);
		toolBar.add(btnSave);
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.setSize(1024, 480);
		// 屏幕居中显示
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);

		this.getContentPane().add(new JScrollPane(this.table), BorderLayout.SOUTH);
	    this.table.addMouseListener(new MouseAdapter (){

			@Override
			public void mouseClicked(MouseEvent e) {
				int i=UIstation.this.table.getSelectedRow();
				if(i<0) {
					return;
				}
			}
	    	
	    });
 		
		this.btnSear.addActionListener(this);
		this.btnSave.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnSear) {
			String s1 = (String) start.getSelectedItem();
			String s2 = (String) end.getSelectedItem();
			result = Subway.dijkstra(list, s1, s2);
			path = (String[]) result.toArray(new String[result.size()]);
			reload(path);
		}	
		else if(e.getSource()==this.btnSave) {
			String s1 = (String) start.getSelectedItem();
			String s2 = (String) end.getSelectedItem();
			String info = "";
			result = Subway.dijkstra(list, s1, s2);
			for(int i=0;i<result.size();i++) {
				info = info+result.get(i)+"\n";
			}
			String wfileName = "station.txt";
			String savepath = Subway.getPath(wfileName);
			try {
				Subway.save(savepath, info);
				JOptionPane.showMessageDialog(null, "保存成功,\n路径:"+savepath, "成功", JOptionPane.OK_OPTION);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
