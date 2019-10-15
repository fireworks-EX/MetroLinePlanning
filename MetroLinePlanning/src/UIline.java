import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class UIline extends JDialog implements ActionListener{
	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private JLabel Line= new JLabel("线路：");
	private String[] listData = null;
	private JComboBox<String> line= null;	
	private Button btnSear = new Button("查询");
	private String[] tableTitles={"线路详情"};
	private Object title[]=tableTitles;
	private Object data[][];
	DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable(model);
	List<String> l1 = null;
	List<String> l2 = null;
	String[] station = null;
	private void reload(List list){
		int count=list.size();
		data =  new Object[count][1];
		for(int i=0;i<count;i++){
			data[i][0]=list.get(i);
		}
		model.setDataVector(data,title);
		table.setRowHeight(20);
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		this.table.validate();
		this.table.repaint();
	}
	
	public UIline(List l1, List l2) {
		this.l1=l1;
		this.l2=l2;
		listData = (String[]) l1.toArray(new String[l1.size()]);
		line = new JComboBox<String>(listData);	
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(Line);
		toolBar.add(line);
		toolBar.add(btnSear);	
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
				int i=UIline.this.table.getSelectedRow();
				if(i<0) {
					return;
				}
			}
	    	
	    });
 		
		this.btnSear.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnSear) {
			String s = (String) line.getSelectedItem();
			for(int i=0;i<l1.size();i++) {
				if(s.equals(l1.get(i))) {
		        	String str = l2.get(i);
		            station = str.split(" ");
		            break;
				}
			}
			List<String> list=Arrays.asList(station);
			List<String> arrList = new ArrayList<String>(list); //
			arrList.remove(0);
			reload(arrList);
		}	
	}
}
