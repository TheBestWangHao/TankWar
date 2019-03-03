import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Bullet
{
	public static final int SPEED=8,WIDTH=20,HEIGHT=20;
	Tank.Directory direction;
	public int x,y;																			//��x,yΪ�ӵ�����
	TankClient tc;																			//��ΪTankClient������
	Boolean live=true;
	Boolean isGood;																			//trueΪ������falseΪ�з�
	public Bullet(int x,int y,Tank.Directory direction,TankClient tc,Boolean isGood) 
	{
		this.x=x;
		this.y=y;
		this.direction=direction;
		this.tc=tc;
		this.isGood=isGood;
	}
	public void draw(Graphics g)
	{

		isLive();
		Color c=g.getColor();
		if(isGood)
			g.setColor(Color.white);
		else
			g.setColor(Color.BLACK);
		g.fillOval(x, y,WIDTH,HEIGHT);
		g.setColor(c);
		move();
	}
	public void move()
	{
		switch(direction)
		{
		case left:x-=SPEED;break;
		case right:x+=SPEED;break;
		case up:y-=SPEED;break;
		case down:y+=SPEED;break;
		case lu:x-=SPEED;y-=SPEED;break;
		case ru:x+=SPEED;y-=SPEED;break;
		case ld:x-=SPEED;y+=SPEED;break;
		case rd:x+=SPEED;y+=SPEED;break;
		case stop:break;
		}
	}
	public void isLive()
	{
		if(isGood)
		{
			if(x<0||y<0||x>tc.GAME_WIDTH||y>tc.GAME_HEIGHT||(!live))
				tc.butList.remove(this);
		}
		else
		{
			if(x<0||y<0||x>tc.GAME_WIDTH||y>tc.GAME_HEIGHT||(!live))
				tc.enemyButList.remove(this);
		}
	}
	public Rectangle getRect()
	{
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public boolean hitTank(ArrayList<Tank> tankList)								//��Ϊ�����ӵ����е��Ե��ж�
	{
			for(int i=0;i<tankList.size();i++)
			{
				Tank t=tankList.get(i);
				if(this.getRect().intersects(t.getRect())&&t.tankLive)
				{
					t.tankLive=false;
					live=false;
					tc.explodeList.add(new Explode(t.x, t.y, tc));
					return true;
				}
			}
			return false;
	}
	public boolean hitTank(Tank t)													//��Ϊ�����ӵ����м������ж�
	{
		if(this.getRect().intersects(t.getRect())&&t.tankLive)
		{
			t.tankLive=false;
			live=false;
			tc.explodeList.add(new Explode(t.x, t.y, tc));
			return true;
		}
		return false;
	}
}
