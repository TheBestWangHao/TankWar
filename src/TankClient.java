import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.Buffer;
import java.util.ArrayList;

//此版本已解决双闪烁现象
//此版本解决游戏界面大小改动麻烦问题，将界面大小设为public static final int的变量
//此版本完成坦克类的基本封装
//此版本完成坦克的八个方向的移动，八个方向的炮筒，以及多发子弹共存。但是还未完成消失出屏幕外的子弹对象的回收处理
public class TankClient extends Frame 
{
	int x=1000,y=800;																//x,y为玩家操纵坦克初始化的坐标
	public static final int SPEED=5;
	Image vrImage=null;
	public static final int GAME_WIDTH=1400,GAME_HEIGHT=1000;
	Tank myTank=new Tank(x,y,this,true);
	ArrayList<Bullet> butList=new ArrayList<Bullet>();							//用来存储子弹对象
	ArrayList<Bullet> enemyButList=new ArrayList<>();							//存储敌方子弹对象
	ArrayList<Explode> explodeList=new ArrayList<>();							//用来存储爆炸对象
	ArrayList<Tank> enemyTankList=new ArrayList<>();							//用来存储坦克对象
	public void paint(Graphics g)
	{
		myTank.draw(g);
		myTank.hitEachOther(enemyTankList);
		for(int i=0;i<enemyTankList.size();i++)
		{
			Tank t=enemyTankList.get(i);
			t.draw(g);

		}
		for(int i=0;i<butList.size();i++)
		{
			Bullet but=butList.get(i);
			if(but!=null)
			but.draw(g);
			but.hitTank(enemyTankList);
		}
		for(int i=0;i<enemyButList.size();i++)
		{
			Bullet but=enemyButList.get(i);
			if(but!=null)
			but.draw(g);
			but.hitTank(myTank);
		}
		for(int i=0;i<explodeList.size();i++)
		{
			Explode ep=explodeList.get(i);
			ep.draw(g);
		}
	}
	/*
	 * 此upDate方法解决了双闪烁问题，因为其创建了一个虚拟屏幕imgGs出来，现在imgGs上缓存，再将缓存转移到屏幕上
	 * @see java.awt.Container#update(java.awt.Graphics)
	 */
	public void update(Graphics g)
	{
		if(vrImage==null)
		{
			vrImage=this.createImage(GAME_WIDTH,GAME_HEIGHT);		//穿件虚拟屏幕imgGs
		}
		Graphics imgGs=vrImage.getGraphics();			  			//获得imgGs的画笔
		Color c=imgGs.getColor();
		imgGs.setColor(Color.gray);									//设置虚拟画布颜色
		imgGs.fillRect(0, 0,GAME_WIDTH,GAME_HEIGHT);				//设置与屏幕上界面一样大的虚拟画布
		imgGs.setColor(c);											//防止将画布上其他的东西的颜色染成跟画布底色一样
		paint(imgGs);
		g.drawImage(vrImage, 0,0,null);								//将画布上内容转移入屏幕上
	}
	public void lanchFrame()
	{
		for(int i=0;i<10;i++)
		{
			enemyTankList.add(new Tank(50*i, 50*i,this,false));
		}
		this.setLocation(150, 20);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("坦克大战");
		this.setBackground(Color.gray);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if(myTank.tankLive)
					myTank.keyPressed(e);
			}
			public void keyReleased(KeyEvent e)
			{
				if(myTank.tankLive)
					myTank.keyReleased(e);
			}
		});
		new Thread(new paintThread()).start();
	}
	
	public static void main(String[] args)
	{
		TankClient tc=new TankClient();
		tc.lanchFrame();
	}
	private class paintThread implements Runnable
	{	
		public void run()
		{
			while(true)
			{
				repaint();
				try 
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
