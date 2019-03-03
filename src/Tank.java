import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.swing.ImageIcon;



public class Tank
{
	int x,y;					//x��y�ֱ�Ϊ̹�˵�x��y����
	int judgeTime=2;
	public static  final int SPEED=5,WIDTH=50,HEIGHT=50,ENEMYSPEED=2;
	ImageIcon imgLeft,imgRight,imgUp,imgDown,imgLU,imgLD,imgRD,imgRU;
	public enum Directory{left,up,down,right,lu,ld,ru,rd,stop}
	boolean bl=false,br=false,bu=false,bd=false;						//�⼸������ �ͱ�����Ϊ�˼�¼����״̬���趨��
	boolean isGood;														//��������ͱ�����Ϊ���ж�̹���ǵз�̹�˻����ҷ�̹�ˣ�true���ҷ���false�ǵз�
	public Directory direction=Directory.stop,lastDirection=null,ptDirection=Directory.up;	//ptDirection����Ͳ����,lastDirection��ֹͣǰ���һ�η���
	TankClient tc;
	boolean tankLive=true;
	public static Random random=new Random();
	private int step=0;																//�˱������ڿ��Ƶ��Եķ���任Ƶ��
	Long time1=null,time;
	public Tank(int x,int y,TankClient tc,boolean isGood)							//���TankClient�����ã�ʹ�ÿ��Եõ�TankClient�Ķ��󣬲��õ����е����ԡ�
	{
		this.x=x;
		this.y=y;
		this.tc=tc;
		this.isGood=isGood;
		imgLeft=new ImageIcon("��.jpg");
		imgRight=new ImageIcon("��.jpg");
		imgUp=new ImageIcon("��.jpg");
		imgDown=new ImageIcon("��.jpg");
		imgLD=new ImageIcon("����.jpg");
		imgLU=new ImageIcon("����.jpg");
		imgRD=new ImageIcon("����.jpg");
		imgRU=new ImageIcon("����.jpg");
	}
	public void draw(Graphics g)
	{
		if(!isGood)
		{
			if(!tankLive)
			{
				tc.enemyTankList.remove(this);
				return;
			}
			g.setColor(Color.BLUE);
		}
		else
		{
			if(!tankLive)
				return;
			g.setColor(Color.RED);
		}
		Color c=g.getColor();											//����̹��Ϊ��ɫ���з�̹��Ϊ��ɫ
		g.fillOval(x, y,WIDTH,HEIGHT);
		g.setColor(c);
		move();
		if(!isGood)														//��ģ����Ƶ��Կ���Ƶ��
		{
			ptDirection=direction;
			if(ptDirection==Directory.stop)
				ptDirection=Directory.up;
			if(random.nextInt(200)>198)
				fire();
		}
		/*
		 * �������һ��������Ͳ�ڰ˸�����ʱ��ͼ��
		 */
		switch(ptDirection)
		{
			case up: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2,y+HEIGHT/2-40);break;
			case down: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2,y+HEIGHT/2+40);break;
			case left: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2-40,y+HEIGHT/2);break;
			case right: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2+40,y+HEIGHT/2);break;
			case lu: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2-40,y+HEIGHT/2-40);break;
			case ld: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2-40,y+HEIGHT/2+40);break;
			case ru: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2+40,y+HEIGHT/2-40);break;
			case rd: g.drawLine(x+WIDTH/2,y+HEIGHT/2,x+WIDTH/2+40,y+HEIGHT/2+40);break;
		}
	}
	public void move()
	{
		if(!isGood)
		{
			/*
			 * ������һС��������Ϊ���Ƶ��Ե��Զ�Ѱ·ģ��
			 */
			if(step==0)														
			{
				Directory[] directions=Directory.values();
				direction=directions[random.nextInt(directions.length)];
				step++;
			}
			else
			{
				step++;
				if(step==150)														//�ڴ˴����Ʒ���任Ƶ��
					step=0;
			}
			switch(direction)
			{
			case left:   if(x>0)													x-=ENEMYSPEED;break;
			case right:  if(x<tc.GAME_WIDTH-WIDTH)									x+=ENEMYSPEED;break;
			case up:     if(y>25)													y-=ENEMYSPEED;break;
			case down:	 if(y<tc.GAME_HEIGHT-HEIGHT)								y+=ENEMYSPEED;break;
			case lu:	 if(x>0&&y>25)												{x-=ENEMYSPEED;y-=ENEMYSPEED;}break;
			case ru:	 if(x<tc.GAME_WIDTH-WIDTH&&y>25)							{x+=ENEMYSPEED;y-=ENEMYSPEED;}break;
			case ld:	 if(x>0&&y<tc.GAME_HEIGHT-HEIGHT)							{x-=ENEMYSPEED;y+=ENEMYSPEED;}break;
			case rd:	 if(x<tc.GAME_WIDTH-WIDTH&&y<tc.GAME_HEIGHT-HEIGHT)			{x+=ENEMYSPEED;y+=ENEMYSPEED;}break;
			case stop:																		 break;
			}
		}
		else
		{
			switch(direction)
			{
			case left:   if(x>0)													x-=SPEED;break;
			case right:  if(x<tc.GAME_WIDTH-WIDTH)									x+=SPEED;break;
			case up:     if(y>25)													y-=SPEED;break;
			case down:	 if(y<tc.GAME_HEIGHT-HEIGHT)								y+=SPEED;break;
			case lu:	 if(x>0&&y>25)												{x-=SPEED;y-=SPEED;}break;
			case ru:	 if(x<tc.GAME_WIDTH-WIDTH&&y>25)							{x+=SPEED;y-=SPEED;}break;
			case ld:	 if(x>0&&y<tc.GAME_HEIGHT-HEIGHT)							{x-=SPEED;y+=SPEED;}break;
			case rd:	 if(x<tc.GAME_WIDTH-WIDTH&&y<tc.GAME_HEIGHT-HEIGHT)			{x+=SPEED;y+=SPEED;}break;
			case stop:																		 break;
			}
		}
	}

	public void keyPressed(java.awt.event.KeyEvent e)
	{
		int keyValue=e.getKeyCode();
		switch(keyValue)
		{
			case KeyEvent.VK_RIGHT: br=true;break;
			case KeyEvent.VK_LEFT: bl=true;break;
			case KeyEvent.VK_UP: bu=true;break;
			case KeyEvent.VK_DOWN: bd=true;break;
		}
		isDirection();
	}
	public void keyReleased(KeyEvent e)
	{
		int keyValue=e.getKeyCode();
		switch(keyValue)
		{
			case KeyEvent.VK_RIGHT: br=false;break;
			case KeyEvent.VK_LEFT: bl=false;break;
			case KeyEvent.VK_UP: bu=false;break;
			case KeyEvent.VK_DOWN: bd=false;break;
			case KeyEvent.VK_SPACE:	fire();
		}
		isDirection();

	}
	public void isDirection()
	{
		judgeTime++;
		if((bl&&bu)&&(!br&&!bd))
			direction=Directory.lu;
		else if((bl&&bd)&&(!br&&!bu))
			direction=Directory.ld;
		else if((br&&bu)&&(!bl&&!bd))
			direction=Directory.ru;
		else if((bd&&br)&&(!bl&&!bu))
			direction=Directory.rd;
		else if(br&&(!bd&&!bl&&!bu))
			direction=Directory.right;
		else if(bl&&(!bd&&!bu&&!br))
			direction=Directory.left;
		else if(bu&&(!bl&&!br&&!bd))
			direction=Directory.up;
		else if(bd&&(!bu&&!br&&!bl))
			direction=Directory.down;
		else if(!bd&&!br&&!bu&&!bl)
			direction=Directory.stop;
		 if(direction!=Directory.stop)
			ptDirection=direction;
		 if(direction==Directory.lu||direction==Directory.ld||direction==Directory.ru||direction==Directory.rd)
		 {
			 lastDirection=direction;
			 judgeTime=0;
		 }
		 if(time1==null)
			 time1=Calendar.getInstance().getTimeInMillis();
		 else
		 {
			 time=Calendar.getInstance().getTimeInMillis()-time1;
			 time1=Calendar.getInstance().getTimeInMillis();
			 if(time<60000&&(direction==Directory.stop)&&(judgeTime==2))
			 {
				 ptDirection=lastDirection;
			 }
		 }
	}
	public void fire()
	{
		int bullet_x,bullet_y;													//��bullet_x,bullet_yΪ�ӵ�����λ��
		bullet_x=x+WIDTH/2-Bullet.WIDTH/2;
		bullet_y=y+WIDTH/2-Bullet.HEIGHT/2;
		if(isGood)
			tc.butList.add(new Bullet(bullet_x, bullet_y,ptDirection,tc,true));
		else
			tc.enemyButList.add(new Bullet(bullet_x, bullet_y,ptDirection,tc,false));
	}
	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public boolean hitEachOther(ArrayList<Tank> tankList)
	{
		for(int i=0;i<tankList.size();i++)
		{
			Tank t=tankList.get(i);
			if(this.getRect().intersects(t.getRect())&&t.tankLive&&this.tankLive)
			{
				tc.explodeList.add(new Explode(x,y,tc));
				tc.explodeList.add(new Explode(t.x, t.y,t.tc));
				this.tankLive=false;
				t.tankLive=false;
				return true;
			}
		}
		return false;
	}
}
