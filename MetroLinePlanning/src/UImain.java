import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class UImain extends JDialog implements ActionListener{
	private JPanel panel = new JPanel(null);
	private JButton btn1 = new JButton("读取地铁文件");
	private JButton btn2 = new JButton("查看指定线路");
	private JButton btn3 = new JButton("查询换乘信息");
	Set<String> line = null;
	List<String> station = null;
	List<String> lis = null;
	List<String> list = null;
	public UImain(Set line, List station, List lis, List list) { 
		this.line=line;
		this.station=station;
		this.list=list;
		this.lis=lis;
		Font f = new Font("黑体",Font.PLAIN,20); 
		btn1.setFont(f);
        btn1.setLocation(20, 20);
        btn1.setSize(760, 70);
        btn2.setFont(f);
        btn2.setLocation(20, 110);
        btn2.setSize(360, 290);
        btn3.setFont(f);
        btn3.setLocation(420, 110);
        btn3.setSize(360, 290);
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setSize(800, 450);
        this.setResizable(false);
		// 屏幕居中显示
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);

		this.validate();
		this.setVisible(true);
		
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		List<String> l1 = new ArrayList<>(line);
		if (e.getSource() == this.btn1) {			
			UImap map = new UImap(l1,station);
			map.setVisible(true);
			JOptionPane.showMessageDialog(null, "读取成功", "成功", JOptionPane.OK_OPTION);
		}
		else if (e.getSource() == this.btn2) {
			UIline ll = new UIline(l1,station);
			ll.setVisible(true);
		}
		else if (e.getSource() == this.btn3) {
			UIstation ls = new UIstation(lis, list);
			ls.setVisible(true);
		}
	}

}

