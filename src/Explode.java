import java.awt.Color;
import java.awt.Graphics;

public class Explode 
{
	public static final int[] boomSize={5,8,15,18,20,25,29,35,42,49,42,39,36,30,25,21,17,13,6};
	public int x,y;
	TankClient tc;
	int step=0;
	public Explode(int x,int y,TankClient tc)
	{
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	public void draw(Graphics g)
	{
		if(step==boomSize.length)
		{
			tc.explodeList.remove(this);
			return;
		}
		Color c=g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y,boomSize[step],boomSize[step]);
		g.setColor(c);
		step++;
	}
}
