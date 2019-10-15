import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UImap extends JDialog{
	private JPanel toolBar = new JPanel();
	private String[] tableTitles={"线路名","站点名"};
	private Object title[]=tableTitles;
	private Object data[][];
	DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable(model);
	private void reload(List l1, List l2){
		int count=l1.size();
		data =  new Object[count][2];
		for(int i=0;i<count;i++){
			data[i][0]=l1.get(i);
			data[i][1]=l2.get(i);
		}
		model.setDataVector(data,title);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(800);
		table.setRowHeight(20);
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		this.table.validate();
		this.table.repaint();
	}
	public UImap(List l1, List l2){

		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
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
				int i=UImap.this.table.getSelectedRow();
				if(i<0) {
					return;
				}
			}
	    	
	    });

		this.reload(l1, l2);

	}
}
