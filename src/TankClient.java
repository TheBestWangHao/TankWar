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

//�˰汾�ѽ��˫��˸����
//�˰汾�����Ϸ�����С�Ķ��鷳���⣬�������С��Ϊpublic static final int�ı���
//�˰汾���̹����Ļ�����װ
//�˰汾���̹�˵İ˸�������ƶ����˸��������Ͳ���Լ��෢�ӵ����档���ǻ�δ�����ʧ����Ļ����ӵ�����Ļ��մ���
public class TankClient extends Frame 
{
	int x=1000,y=800;																//x,yΪ��Ҳ���̹�˳�ʼ��������
	public static final int SPEED=5;
	Image vrImage=null;
	public static final int GAME_WIDTH=1400,GAME_HEIGHT=1000;
	Tank myTank=new Tank(x,y,this,true);
	ArrayList<Bullet> butList=new ArrayList<Bullet>();							//�����洢�ӵ�����
	ArrayList<Bullet> enemyButList=new ArrayList<>();							//�洢�з��ӵ�����
	ArrayList<Explode> explodeList=new ArrayList<>();							//�����洢��ը����
	ArrayList<Tank> enemyTankList=new ArrayList<>();							//�����洢̹�˶���
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
	 * ��upDate���������˫��˸���⣬��Ϊ�䴴����һ��������ĻimgGs����������imgGs�ϻ��棬�ٽ�����ת�Ƶ���Ļ��
	 * @see java.awt.Container#update(java.awt.Graphics)
	 */
	public void update(Graphics g)
	{
		if(vrImage==null)
		{
			vrImage=this.createImage(GAME_WIDTH,GAME_HEIGHT);		//����������ĻimgGs
		}
		Graphics imgGs=vrImage.getGraphics();			  			//���imgGs�Ļ���
		Color c=imgGs.getColor();
		imgGs.setColor(Color.gray);									//�������⻭����ɫ
		imgGs.fillRect(0, 0,GAME_WIDTH,GAME_HEIGHT);				//��������Ļ�Ͻ���һ��������⻭��
		imgGs.setColor(c);											//��ֹ�������������Ķ�������ɫȾ�ɸ�������ɫһ��
		paint(imgGs);
		g.drawImage(vrImage, 0,0,null);								//������������ת������Ļ��
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
		this.setTitle("̹�˴�ս");
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
