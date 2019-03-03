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
	int x,y;					//x，y分别为坦克的x与y坐标
	int judgeTime=2;
	public static  final int SPEED=5,WIDTH=50,HEIGHT=50,ENEMYSPEED=2;
	ImageIcon imgLeft,imgRight,imgUp,imgDown,imgLU,imgLD,imgRD,imgRU;
	public enum Directory{left,up,down,right,lu,ld,ru,rd,stop}
	boolean bl=false,br=false,bu=false,bd=false;						//这几个布尔 型变量是为了记录按键状态而设定的
	boolean isGood;														//这个布尔型变量是为了判断坦克是敌方坦克还是我方坦克，true是我方，false是敌方
	public Directory direction=Directory.stop,lastDirection=null,ptDirection=Directory.up;	//ptDirection是炮筒方向,lastDirection是停止前最后一次方向
	TankClient tc;
	boolean tankLive=true;
	public static Random random=new Random();
	private int step=0;																//此变量用于控制电脑的方向变换频率
	Long time1=null,time;
	public Tank(int x,int y,TankClient tc,boolean isGood)							//获得TankClient的引用，使得可以得到TankClient的对象，并得到其中的属性。
	{
		this.x=x;
		this.y=y;
		this.tc=tc;
		this.isGood=isGood;
		imgLeft=new ImageIcon("左.jpg");
		imgRight=new ImageIcon("右.jpg");
		imgUp=new ImageIcon("上.jpg");
		imgDown=new ImageIcon("下.jpg");
		imgLD=new ImageIcon("左上.jpg");
		imgLU=new ImageIcon("左上.jpg");
		imgRD=new ImageIcon("右下.jpg");
		imgRU=new ImageIcon("右上.jpg");
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
		Color c=g.getColor();											//己方坦克为红色，敌方坦克为蓝色
		g.fillOval(x, y,WIDTH,HEIGHT);
		g.setColor(c);
		move();
		if(!isGood)														//此模块控制电脑开火频率
		{
			ptDirection=direction;
			if(ptDirection==Directory.stop)
				ptDirection=Directory.up;
			if(random.nextInt(200)>198)
				fire();
		}
		/*
		 * 下面的这一部分是炮筒在八个方向时的图形
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
			 * 下面这一小部分内容为控制电脑的自动寻路模块
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
				if(step==150)														//在此处控制方向变换频率
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
		int bullet_x,bullet_y;													//此bullet_x,bullet_y为子弹发出位置
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
